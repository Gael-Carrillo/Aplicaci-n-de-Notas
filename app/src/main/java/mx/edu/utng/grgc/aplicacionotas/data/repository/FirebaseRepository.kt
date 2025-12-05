package mx.edu.utng.grgc.aplicacionotas.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import mx.edu.utng.grgc.aplicacionotas.data.models.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 🏪 Repositorio de Firebase - El Almacén Central
 *
 * Este es el ÚNICO lugar que habla con Firebase.
 * Es como el gerente de una tienda que maneja todo.
 *
 * Responsabilidades:
 * - Autenticación (login/registro)
 * - CRUD de notas (crear, leer, actualizar, eliminar)
 * - Gestión de categorías
 * - Sincronización en tiempo real
 */
class FirebaseRepository {

    // ============================================
    // INSTANCIAS DE FIREBASE
    // ============================================

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Referencias a las colecciones (como "mesas" en una base de datos)
    private val usersCollection = firestore.collection("users")
    private val notesCollection = firestore.collection("notes")
    private val categoriesCollection = firestore.collection("categories")

    // ============================================
    // AUTENTICACIÓN DE USUARIOS
    // ============================================

    /**
     * 📝 Registrar un nuevo usuario
     *
     * Proceso:
     * 1. Crea el usuario en Firebase Auth
     * 2. Guarda información adicional en Firestore
     *
     * @param nombre Nombre completo del usuario
     * @param email Correo electrónico
     * @param password Contraseña (mínimo 6 caracteres)
     * @return Result con el usuario creado o error
     */
    suspend fun registerUser(
        nombre: String,
        email: String,
        password: String
    ): Result<User> {
        return try {
            // Paso 1: Crear usuario en Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: throw Exception("Error al crear usuario en Authentication")

            // Paso 2: Crear documento de usuario en Firestore
            val user = User(
                id = firebaseUser.uid,
                nombre = nombre,
                email = email
            )

            usersCollection.document(firebaseUser.uid)
                .set(user)
                .await()

            // Paso 3: Crear categorías predeterminadas para este usuario
            createDefaultCategoriesForUser()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception("Error al registrar: ${e.message}"))
        }
    }

    /**
     * 🔐 Iniciar sesión
     *
     * @param email Correo electrónico
     * @param password Contraseña
     * @return Result con el usuario o error
     */
    suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        return try {
            // Paso 1: Autenticar con Firebase Auth
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: throw Exception("Usuario no encontrado")

            // Paso 2: Obtener datos del usuario desde Firestore
            val userDoc = usersCollection.document(firebaseUser.uid).get().await()
            val user = userDoc.toObject(User::class.java)
                ?: throw Exception("Datos de usuario no encontrados")

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception("Error al iniciar sesión: ${e.message}"))
        }
    }

    /**
     * 🚪 Cerrar sesión
     */
    fun logout() {
        auth.signOut()
    }

    /**
     * 👤 Obtener ID del usuario actual
     */
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    /**
     * 👤 Obtener usuario actual completo
     */
    suspend fun getCurrentUser(): User? {
        return try {
            val userId = getCurrentUserId() ?: return null
            val userDoc = usersCollection.document(userId).get().await()
            userDoc.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // ============================================
    // OPERACIONES CON NOTAS
    // ============================================

    /**
     * ✏️ Crear una nueva nota
     *
     * @return Result con la nota creada o error
     */
    suspend fun createNote(
        title: String,
        content: String,
        categoryId: String,
        priority: Priority,
        reminderDate: String?
    ): Result<Note> {
        return try {
            val userId = getCurrentUserId()
                ?: throw Exception("Usuario no autenticado")

            val note = Note(
                title = title,
                content = content,
                categoryId = categoryId,
                priority = priority.name,
                userId = userId,
                reminderDate = reminderDate
            )

            val docRef = notesCollection.add(note).await()

            Result.success(note.copy(id = docRef.id))
        } catch (e: Exception) {
            Result.failure(Exception("Error al crear nota: ${e.message}"))
        }
    }

    /**
     * 📋 Obtener todas las notas del usuario actual
     *
     * Retorna un Flow que se actualiza automáticamente
     * cuando hay cambios en Firebase (Tiempo Real)
     */
    fun getUserNotesFlow(): Flow<List<Note>> = callbackFlow {
        val userId = getCurrentUserId()
        if (userId == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val subscription = notesCollection
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val notes = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Note::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                trySend(notes)
            }

        awaitClose { subscription.remove() }
    }

    /**
     * 📝 Actualizar una nota existente
     */
    suspend fun updateNote(
        noteId: String,
        title: String,
        content: String,
        categoryId: String,
        priority: Priority,
        reminderDate: String?
    ): Result<Unit> {
        return try {
            val updates = hashMapOf<String, Any?>(
                "title" to title,
                "content" to content,
                "categoryId" to categoryId,
                "priority" to priority.name,
                "reminderDate" to reminderDate
            )

            notesCollection.document(noteId).update(updates).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al actualizar: ${e.message}"))
        }
    }

    /**
     * 🗑️ Eliminar una nota
     */
    suspend fun deleteNote(noteId: String): Result<Unit> {
        return try {
            notesCollection.document(noteId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al eliminar: ${e.message}"))
        }
    }

    /**
     * 🔍 Buscar notas por texto
     */
    suspend fun searchNotes(query: String): Result<List<Note>> {
        return try {
            val userId = getCurrentUserId()
                ?: throw Exception("Usuario no autenticado")

            // Obtener todas las notas del usuario
            val snapshot = notesCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()

            // Filtrar localmente por título o contenido
            val notes = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Note::class.java)?.copy(id = doc.id)
            }.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.content.contains(query, ignoreCase = true)
            }

            Result.success(notes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ============================================
    // OPERACIONES CON CATEGORÍAS
    // ============================================

    /**
     * 🏷️ Obtener todas las categorías
     *
     * Retorna un Flow que se actualiza en tiempo real
     */
    fun getCategoriesFlow(): Flow<List<Category>> = callbackFlow {
        val subscription = categoriesCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val categories = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Category::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                trySend(categories)
            }

        awaitClose { subscription.remove() }
    }

    /**
     * 📚 Obtener categorías (versión suspend)
     */
    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val snapshot = categoriesCollection.get().await()
            val categories = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Category::class.java)?.copy(id = doc.id)
            }

            // Si no hay categorías, crear las predeterminadas
            if (categories.isEmpty()) {
                createDefaultCategoriesForUser()
                return getCategories()
            }

            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 🆕 Crear una nueva categoría
     */
    suspend fun createCategory(
        nombre: String,
        colorHex: String,
        emoji: String
    ): Result<Category> {
        return try {
            val category = Category(
                nombre = nombre,
                colorHex = colorHex,
                emoji = emoji
            )

            val docRef = categoriesCollection.add(category).await()

            Result.success(category.copy(id = docRef.id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 🎨 Crear categorías predeterminadas
     *
     * Se ejecuta automáticamente al registrar un usuario
     */
    private suspend fun createDefaultCategoriesForUser() {
        try {
            DefaultCategories.getAll().forEach { categoryMap ->
                categoriesCollection.add(categoryMap).await()
            }
        } catch (e: Exception) {
            // Si falla, no es crítico
            println("Error al crear categorías predeterminadas: ${e.message}")
        }
    }

    /**
     * 🔍 Obtener una categoría por ID
     */
    suspend fun getCategoryById(categoryId: String): Result<Category?> {
        return try {
            val doc = categoriesCollection.document(categoryId).get().await()
            val category = doc.toObject(Category::class.java)?.copy(id = doc.id)
            Result.success(category)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ============================================
    // FILTROS Y CONSULTAS AVANZADAS
    // ============================================

    /**
     * 🏷️ Obtener notas por categoría
     */
    suspend fun getNotesByCategory(categoryId: String): Result<List<Note>> {
        return try {
            val userId = getCurrentUserId()
                ?: throw Exception("Usuario no autenticado")

            val snapshot = notesCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("categoryId", categoryId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val notes = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Note::class.java)?.copy(id = doc.id)
            }

            Result.success(notes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * ⭐ Obtener notas por prioridad
     */
    suspend fun getNotesByPriority(priority: Priority): Result<List<Note>> {
        return try {
            val userId = getCurrentUserId()
                ?: throw Exception("Usuario no autenticado")

            val snapshot = notesCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("priority", priority.name)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val notes = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Note::class.java)?.copy(id = doc.id)
            }

            Result.success(notes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * ➕ Crear nueva categoría
     */
    suspend fun createCategory(category: Category): Result<String> = suspendCoroutine { continuation ->
        val userId = auth.currentUser?.uid
        if (userId == null) {
            continuation.resume(Result.failure(Exception("Usuario no autenticado")))
            return@suspendCoroutine
        }

        val categoryData = hashMapOf(
            "nombre" to category.nombre,
            "colorHex" to category.colorHex,
            "emoji" to category.emoji,
            "userId" to userId,
            "createdAt" to System.currentTimeMillis()
        )

        firestore.collection("categories")
            .add(categoryData)
            .addOnSuccessListener { documentRef ->
                continuation.resume(Result.success(documentRef.id))
            }
            .addOnFailureListener { e ->
                continuation.resume(Result.failure(e))
            }
    }

    /**
     * ✏️ Actualizar categoría existente
     */
    suspend fun updateCategory(categoryId: String, nombre: String, colorHex: String, emoji: String): Result<Unit> =
        suspendCoroutine { continuation ->
            val updates = hashMapOf(
                "nombre" to nombre,
                "colorHex" to colorHex,
                "emoji" to emoji,
                "updatedAt" to System.currentTimeMillis()
            )

            firestore.collection("categories")
                .document(categoryId)
                .update(updates as Map<String, Any>)
                .addOnSuccessListener {
                    continuation.resume(Result.success(Unit))
                }
                .addOnFailureListener { e ->
                    continuation.resume(Result.failure(e))
                }
        }

    /**
     * 🗑️ Eliminar categoría (solo si no tiene notas asociadas)
     */
    suspend fun deleteCategory(categoryId: String): Result<Unit> = suspendCoroutine { continuation ->
        val userId = auth.currentUser?.uid
        if (userId == null) {
            continuation.resume(Result.failure(Exception("Usuario no autenticado")))
            return@suspendCoroutine
        }

        // Primero verificar si hay notas con esta categoría
        firestore.collection("notes")
            .whereEqualTo("userId", userId)
            .whereEqualTo("categoryId", categoryId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    continuation.resume(Result.failure(Exception("No puedes eliminar una categoría que tiene notas")))
                } else {
                    // Si no hay notas, eliminar la categoría
                    firestore.collection("categories")
                        .document(categoryId)
                        .delete()
                        .addOnSuccessListener {
                            continuation.resume(Result.success(Unit))
                        }
                        .addOnFailureListener { e ->
                            continuation.resume(Result.failure(e))
                        }
                }
            }
            .addOnFailureListener { e ->
                continuation.resume(Result.failure(e))
            }
    }
}
