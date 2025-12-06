# 📝 Aplicación de Notas

Aplicación móvil Android para gestionar notas personales con categorías y prioridades, desarrollada en **Kotlin** con **Firebase**.

---
## dejare un link para que descarguen el manual completo
https://drive.google.com/drive/folders/1g9OZqVounVnuPw_v7FIvQEQ9XLudIh8r?usp=sharing

---

## 📹 Video de tik tok

https://vt.tiktok.com/ZSfwAtbry/

---

---
## ⬇️ descarga aplicacion 
https://gael-carrillo.github.io/palicacion-apk-notas/

## 🚀 Características

- ✅ Crear, editar y eliminar notas  
- 🏷️ Organización por categorías  
- ⭐ Sistema de prioridades para notas  
- 🔐 Autenticación de usuarios (Login/Registro)  
- ☁️ Sincronización en la nube con Firebase  
- 🔔 Notificaciones push mediante Firebase Cloud Messaging  
- 🎨 Interfaz moderna con Jetpack Compose  
- 🌙 Temas personalizables  

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Kotlin  
- **UI Framework:** Jetpack Compose  
- **Arquitectura:** MVVM (Model-View-ViewModel)  
- **Base de datos:** Firebase Firestore  
- **Autenticación:** Firebase Authentication  
- **Notificaciones:** Firebase Cloud Messaging  
- **Navegación:** Jetpack Navigation Compose  
- **Inyección de dependencias:** Hilt/Dagger (opcional)  

---

## 📋 Requisitos Previos

- Android Studio Hedgehog o superior  
- JDK 11 o superior  
- Cuenta de Firebase (para configuración del proyecto)  
- Dispositivo Android con API 24+ (Android 7.0) o emulador  

---

## ⚙️ Instalación

1. Clonar repositorio  
2. Agregar `google-services.json` en la carpeta `app/`  
3. Compilar en Android Studio  
4. Configurar Firebase:
   - Crear proyecto en Firebase Console  
   - Habilitar **Authentication (Email/Password)**  
   - Habilitar **Cloud Firestore**  

---

## 📁 Estructura del Proyecto

app/
- ├── manifests/AndroidManifest.xml
- ├── kotlin/ mx.edu.utng.grgc.aplicacionotas/
- │   ├── data/
- │   │   ├── models/         # Category.kt, Note.kt, Priority.kt, User.kt
- │   │   └── repository/     # FirebaseRepository.kt
- │   ├── navigation/         # Navigation.kt
- │   ├── notifications/      # MyFirebaseMessagingService.kt, NotificationReceiver.kt
- │   └── ui/
- │       ├── components/     # NoteCard.kt, PriorityBadge.kt
- │       └── screens/        # MainScreen.kt, CreateNoteScreen.kt, etc.
- └── docs/screenshots/





---

## 🎯 Funcionalidades Principales

### 🔐 Autenticación
- Registro de nuevos usuarios con email y contraseña  
- Inicio de sesión para usuarios existentes  
- Gestión de sesión persistente  

### 📝 Gestión de Notas
- Crear notas con título, contenido, categoría y prioridad  
- Editar notas existentes  
- Eliminar notas  
- Visualización de todas las notas del usuario  

### 🏷️ Categorías
- Crear y gestionar categorías personalizadas  
- Filtrar notas por categoría  
- Asignar colores y emojis a las categorías  

### ⭐ Prioridades
- Sistema de prioridades (Alta, Media, Baja)  
- Indicadores visuales de prioridad  

---

## 📱 Capturas de Pantalla
*(Agrega aquí imágenes de la aplicación en funcionamiento)*

---

## 📦 Código Fuente

El proyecto incluye modelos, repositorios y servicios listos para integrarse con Firebase:

## ⚙️ Configuración de Gradle (build.gradle.kts)

Este archivo define la configuración principal de la aplicación Android, incluyendo **plugins**, **SDKs**, y **dependencias** como Firebase, Jetpack Compose y WorkManager.

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services) apply true
}

android {
    namespace = "mx.edu.utng.grgc.aplicacionotas"
    compileSdk = 36

    defaultConfig {
        applicationId = "mx.edu.utng.grgc.aplicacionotas"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // ============================================
    // CORE ANDROID
    // ============================================
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // ============================================
    // JETPACK COMPOSE
    // ============================================
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // ============================================
    // ⭐ FIREBASE
    // ============================================
    // BOM de Firebase (gestiona versiones automáticamente)
    implementation(platform(libs.firebase.bom))

    // Firebase Authentication
    implementation(libs.firebase.auth.ktx)

    // Firebase Firestore (Base de datos)
    implementation(libs.firebase.firestore.ktx)

    // ============================================
    // COROUTINES
    // ============================================
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    // ============================================
    // TESTING
    // ============================================
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // ============================================
    // DEBUG
    // ============================================
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Firebase Cloud Messaging
    implementation("com.google.firebase:firebase-messaging:25.0.1")

    // WorkManager para programar notificaciones
    implementation("androidx.work:work-runtime-ktx:2.11.0")
}
```

- `Category.kt` → Modelo de categorías
- ## 🏷️ Modelo de Categoría

Este modelo sirve para organizar las notas por **temas** (Trabajo, Personal, Estudio, etc.).  
El color se guarda como `String` (hexadecimal) porque Firebase no entiende directamente el tipo `Color` de Compose.

```kotlin
package mx.edu.utng.grgc.aplicacionotas.data.models

import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.DocumentId
import android.graphics.Color as AndroidColor

/**
 * 🏷️ Modelo de Categoría
 *
 * Para organizar las notas por temas (Trabajo, Personal, etc.)
 *
 * Guardamos el color como Long porque Firebase no entiende
 * el tipo Color de Compose directamente.
 */
data class Category(
    @DocumentId val id: String = "",
    val nombre: String = "",
    val colorHex: String = "0xFF6366F1",
    val emoji: String = "📝"
) {
    // Constructor vacío para Firebase
    constructor() : this("", "", "0xFF6366F1", "📝")

    /**
     * Convierte el Long a Color de Compose
     */
    fun toColor(): Color {
        // Intenta parsear la cadena usando la función de Android.
        return try {
            val androidColor = AndroidColor.parseColor(colorHex)
            // Convierte el valor int de Android a Color de Compose
            Color(androidColor)
        } catch (e: Exception) {
            // Si el colorHex está mal, usa el color Negro (#FF000000) por defecto para evitar el crash.
            Color(0xFF000000)
        }
    }
}

/**
 * 📦 Categorías Predefinidas
 *
 * Como los sabores de helado: ya vienen incluidos,
 * pero puedes agregar más después
 */
object DefaultCategories {
    val PERSONAL = mapOf(
        "nombre" to "Personal",
        "colorHex" to "0xFF6366F1",
        "emoji" to "👤"
    )

    val TRABAJO = mapOf(
        "nombre" to "Trabajo",
        "colorHex" to "0xFFEF4444",
        "emoji" to "💼"
    )

    val ESTUDIO = mapOf(
        "nombre" to "Estudio",
        "colorHex" to "0xFF10B981",
        "emoji" to "📚"
    )

    val IDEAS = mapOf(
        "nombre" to "Ideas",
        "colorHex" to "0xFFF59E0B",
        "emoji" to "💡"
    )

    val COMPRAS = mapOf(
        "nombre" to "Compras",
        "colorHex" to "0xFF8B5CF6",
        "emoji" to "🛒"
    )

    fun getAll() = listOf(PERSONAL, TRABAJO, ESTUDIO, IDEAS, COMPRAS)
}
```
---

### 📖 Explicación rápida
- **Category**: modelo que define una categoría con `nombre`, `colorHex` y `emoji`.  
- **toColor()**: convierte el `colorHex` en un objeto `Color` de Compose.  
- **DefaultCategories**: incluye categorías predefinidas como *Personal*, *Trabajo*, *Estudio*, *Ideas* y *Compras*.  

---

- `Note.kt` → Modelo de notas
  ## 📌 Modelo de Nota

Este es el modelo principal de una **Nota** en la aplicación:

```kotlin
package mx.edu.utng.grgc.aplicacionotas.data.models

import com.google.firebase.firestore.DocumentId

/**
 * 📝 Modelo de Nota - La Estrella del Show
 *
 * Contiene TODA la información de una nota:
 * - Título y contenido
 * - A qué categoría pertenece
 * - Qué tan importante es (prioridad)
 * - De quién es (userId)
 * - Cuándo se creó
 * - Recordatorios y archivos
 *
 * ✅ IMPORTANTE: La categoría va CON la nota, no con el usuario
 */
data class Note(
    @DocumentId val id: String = "",
    var title: String = "",
    var content: String = "",
    var categoryId: String = "",  // ID de la categoría
    var priority: String = "MEDIA", // "ALTA", "MEDIA", "BAJA"
    var userId: String = "",  // ID del dueño de la nota
    val createdAt: Long = System.currentTimeMillis(),
    var reminderDate: String? = null,
    var attachments: List<String> = emptyList()
) {
    // Constructor vacío para Firebase
    constructor() : this(
        "", "", "", "", "MEDIA", "",
        System.currentTimeMillis(), null, emptyList()
    )

    /**
     * Obtiene la prioridad como enum
     */
    fun getPriorityEnum(): Priority {
        return Priority.fromString(priority)
    }

    /**
     * Obtiene un timestamp legible
     */
    fun getFormattedDate(): String {
        val diff = System.currentTimeMillis() - createdAt
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            minutes < 1 -> "Ahora"
            minutes < 60 -> "Hace ${minutes}m"
            hours < 24 -> "Hace ${hours}h"
            days < 7 -> "Hace ${days}d"
            else -> "Hace ${days / 7} semanas"
        }
    }
}
```
- `Priority.kt` → Enum de prioridades
- ## ⭐ Enum de Prioridad

Este enum define los niveles de **urgencia** de una nota, similar a las etiquetas de un hospital:

- **ALTA** = Rojo (urgente 🚨)  
- **MEDIA** = Amarillo (importante ⚠️)  
- **BAJA** = Verde (cuando puedas ✅)  

```kotlin
package mx.edu.utng.grgc.aplicacionotas.data.models

import androidx.compose.ui.graphics.Color

/**
 * ⭐ Enum de Prioridad
 *
 * Como las etiquetas de urgencia en un hospital:
 * - ALTA = Rojo (urgente!)
 * - MEDIA = Amarillo (importante)
 * - BAJA = Verde (cuando puedas)
 */
enum class Priority(
    val displayName: String,
    val colorHex: Long
) {
    ALTA("Alta", 0xFFEF4444),
    MEDIA("Media", 0xFFF59E0B),
    BAJA("Baja", 0xFF10B981);

    /**
     * Convierte el color hexadecimal a Color de Compose
     */
    fun toColor(): Color {
        return Color(colorHex)
    }

    companion object {
        /**
         * Convierte un String a Priority
         * Útil cuando leemos de Firebase
         */
        fun fromString(value: String): Priority {
            return when (value.uppercase()) {
                "ALTA" -> ALTA
                "MEDIA" -> MEDIA
                "BAJA" -> BAJA
                else -> MEDIA // Por defecto
            }
        }
    }
}
```
---

### 📖 Explicación rápida
- **Priority**: enum con tres valores (`ALTA`, `MEDIA`, `BAJA`).  
- Cada prioridad tiene:
  - `displayName`: nombre legible.  
  - `colorHex`: color asociado en formato hexadecimal.  
- **toColor()**: convierte el `colorHex` en un objeto `Color` de Compose.  
- **fromString()**: transforma un `String` en el enum correspondiente (útil al leer datos de Firebase).  

---

- `User.kt` → Modelo de usuario
- ## 👤 Modelo de Usuario

Este modelo representa a cada persona que usa la aplicación.  
Firebase utiliza `@DocumentId` para asignar un identificador único a cada usuario.

- `id`: Identificación única (no cambia, como una huella digital).  
- `nombre`: Nombre del usuario (se puede cambiar, como un apodo).  
- `email`: Correo electrónico del usuario.  
- `createdAt`: Fecha de creación del registro en milisegundos.  

```kotlin
package mx.edu.utng.grgc.aplicacionotas.data.models

import com.google.firebase.firestore.DocumentId

/**
 * 👤 Modelo de Usuario
 *
 * Representa a cada persona que usa la app.
 * Firebase usa @DocumentId para identificar únicamente cada usuario.
 *
 * @DocumentId: Es como tu número de identificación único
 * val id: No se puede cambiar (como tu huella digital)
 * var nombre: Se puede cambiar (como tu apodo)
 */
data class User(
    @DocumentId val id: String = "",
    val nombre: String = "",
    val email: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    // Constructor vacío requerido por Firebase
    constructor() : this("", "", "", 0L)
}
````

---

### 📖 Explicación rápida
- **User** es el modelo que define a cada usuario dentro de la app.  
- Incluye:
  - `id`: generado automáticamente por Firebase.  
  - `nombre`: nombre visible del usuario.  
  - `email`: correo electrónico asociado.  
  - `createdAt`: fecha de creación del registro.  
- El **constructor vacío** es obligatorio para que Firebase pueda deserializar el objeto correctamente.  

---

- `FirebaseRepository.kt` → CRUD y autenticación con Firebase
- ## 🏪 Firebase Repository

Este repositorio es el **único punto de comunicación con Firebase**.  
Piensa en él como el **gerente de una tienda**: maneja autenticación, notas, categorías y sincronización en tiempo real.

### 📌 Responsabilidades
- Autenticación de usuarios (login, registro, logout).  
- CRUD de notas (crear, leer, actualizar, eliminar).  
- Gestión de categorías (crear, actualizar, eliminar, obtener).  
- Sincronización en tiempo real con `Flow`.  

```kotlin
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
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val usersCollection = firestore.collection("users")
    private val notesCollection = firestore.collection("notes")
    private val categoriesCollection = firestore.collection("categories")

    // ============================
    // AUTENTICACIÓN
    // ============================
    suspend fun registerUser(nombre: String, email: String, password: String): Result<User> { /* ... */ }
    suspend fun login(email: String, password: String): Result<User> { /* ... */ }
    fun logout() { auth.signOut() }
    fun getCurrentUserId(): String? = auth.currentUser?.uid
    suspend fun getCurrentUser(): User? { /* ... */ }

    // ============================
    // NOTAS
    // ============================
    suspend fun createNote(title: String, content: String, categoryId: String, priority: Priority, reminderDate: String?): Result<Note> { /* ... */ }
    fun getUserNotesFlow(): Flow<List<Note>> { /* ... */ }
    suspend fun updateNote(noteId: String, title: String, content: String, categoryId: String, priority: Priority, reminderDate: String?): Result<Unit> { /* ... */ }
    suspend fun deleteNote(noteId: String): Result<Unit> { /* ... */ }
    suspend fun searchNotes(query: String): Result<List<Note>> { /* ... */ }

    // ============================
    // CATEGORÍAS
    // ============================
    fun getCategoriesFlow(): Flow<List<Category>> { /* ... */ }
    suspend fun getCategories(): Result<List<Category>> { /* ... */ }
    suspend fun createCategory(nombre: String, colorHex: String, emoji: String): Result<Category> { /* ... */ }
    suspend fun getCategoryById(categoryId: String): Result<Category?> { /* ... */ }
    suspend fun getNotesByCategory(categoryId: String): Result<List<Note>> { /* ... */ }
    suspend fun getNotesByPriority(priority: Priority): Result<List<Note>> { /* ... */ }
    suspend fun updateCategory(categoryId: String, nombre: String, colorHex: String, emoji: String): Result<Unit> { /* ... */ }
    suspend fun deleteCategory(categoryId: String): Result<Unit> { /* ... */ }
}
```
- `Navigation.kt` → Sistema de navegación con Compose
- ## 🧭 Sistema de Navegación

Este archivo define todas las pantallas posibles de la aplicación y cómo se conectan entre sí.  
Piensa en él como el **mapa de un centro comercial** 🗺️: cada pantalla es una tienda, y el coordinador decide a dónde ir.

```kotlin
package mx.edu.utng.grgc.aplicacionotas.navigation

import androidx.compose.runtime.*
import mx.edu.utng.grgc.aplicacionotas.data.models.Note
import mx.edu.utng.grgc.aplicacionotas.data.repository.FirebaseRepository
import mx.edu.utng.grgc.aplicacionotas.ui.screens.*

/**
 * 🧭 Sistema de Navegación
 *
 * Define todas las pantallas posibles de la app.
 * Es como el mapa de un centro comercial 🗺️
 */
sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object Main : Screen()
    object CreateNote : Screen()
    object CategoriesManagement : Screen()
    data class EditNote(val note: Note) : Screen()
}

/**
 * 🎬 Coordinador de Navegación
 *
 * Maneja el flujo entre pantallas.
 * Es como el director de una película que decide qué escena sigue.
 */
@Composable
fun NotesAppNavigation(
    repository: FirebaseRepository,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    // Estado actual de la pantalla
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

    // Navegar según la pantalla actual
    when (val screen = currentScreen) {
        // ============================================
        // PANTALLA: LOGIN
        // ============================================
        is Screen.Login -> {
            LoginScreen(
                repository = repository,
                isDarkMode = isDarkMode,
                onThemeChange = onThemeChange,
                onLoginSuccess = {
                    currentScreen = Screen.Main
                },
                onNavigateToRegister = {
                    currentScreen = Screen.Register
                }
            )
        }

        // ============================================
        // PANTALLA: REGISTRO
        // ============================================
        is Screen.Register -> {
            RegisterScreen(
                repository = repository,
                onRegisterSuccess = {
                    // Después de registrarse, va al login
                    currentScreen = Screen.Login
                },
                onNavigateToLogin = {
                    currentScreen = Screen.Login
                }
            )
        }

        // ============================================
        // PANTALLA: PRINCIPAL
        // ============================================
        is Screen.Main -> {
            MainScreen(
                repository = repository,
                isDarkMode = isDarkMode,
                onThemeChange = onThemeChange,
                onCreateNote = {
                    currentScreen = Screen.CreateNote
                },
                onEditNote = { note ->
                    currentScreen = Screen.EditNote(note)
                },
                onNavigateToCategories = {
                    currentScreen = Screen.CategoriesManagement
                },
                onLogout = {
                    repository.logout()
                    currentScreen = Screen.Login
                }
            )
        }

        // ============================================
        // PANTALLA: CREAR NOTA
        // ============================================
        is Screen.CreateNote -> {
            CreateNoteScreen(
                repository = repository,
                onNoteCreated = {
                    currentScreen = Screen.Main
                },
                onBack = {
                    currentScreen = Screen.Main
                }
            )
        }

        // ============================================
        // PANTALLA: EDITAR NOTA
        // ============================================
        is Screen.EditNote -> {
            EditNoteScreen(
                note = screen.note,
                repository = repository,
                onNoteUpdated = {
                    currentScreen = Screen.Main
                },
                onBack = {
                    currentScreen = Screen.Main
                }
            )
        }

        // ============================================
        // PANTALLA: GESTIÓN DE CATEGORÍAS
        // ============================================
        is Screen.CategoriesManagement -> {
            CategoriesManagementScreen(
                repository = repository,
                onBack = {
                    currentScreen = Screen.Main
                }
            )
        }
    }
}
```  
- `MyFirebaseMessagingService.kt` → Manejo de notificaciones push
- ## 🔔 MyFirebaseMessagingService

Este servicio maneja las **notificaciones push** enviadas desde Firebase Cloud Messaging (FCM).  
Se encarga de dos cosas principales:

1. **onNewToken** → cuando se genera un nuevo token de dispositivo.  
   - Este token identifica el dispositivo en FCM.  
   - Debes enviarlo a tu servidor si manejas notificaciones desde tu backend.  

2. **onMessageReceived** → cuando llega un mensaje (data o notification).  
   - Maneja mensajes de datos (`remoteMessage.data`).  
   - Maneja mensajes de notificación (`remoteMessage.notification`).  
   - Construye y muestra la notificación manualmente si la app está en primer plano.  

```kotlin
package mx.edu.utng.grgc.aplicacionotas.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FCM_Service"

    /**
     * 1. Se llama cuando un nuevo token de registro de dispositivo se genera o actualiza.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Nuevo Token: $token")

        // Envía este token a tu servidor si manejas notificaciones desde tu backend.
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        // Implementar aquí la llamada a una API o base de datos para guardar el token.
    }

    /**
     * 2. Se llama cuando un mensaje (Data Message o Notification Message en primer plano) es recibido.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Mensaje de: ${remoteMessage.from}")

        // 1. Manejar Mensajes de Datos
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Payload de datos: " + remoteMessage.data)

            val customTitle = remoteMessage.data["title"]
            val customBody = remoteMessage.data["body"]

            customTitle?.let {
                showNotification(it, customBody ?: "Contenido de la nota actualizado")
            }
        }

        // 2. Manejar Mensajes de Notificación
        remoteMessage.notification?.let {
            Log.d(TAG, "Cuerpo de la notificación: ${it.body}")
            showNotification(it.title ?: "Nueva Nota", it.body ?: "Revisa la aplicación")
        }
    }

    // Función auxiliar para construir y mostrar la notificación
    private fun showNotification(title: String, body: String) {
        // Implementar con NotificationManager:
        // 1. Crear NotificationChannel (Android O+)
        // 2. Crear PendingIntent
        // 3. Usar NotificationCompat.Builder
        // 4. NotificationManager.notify()
    }
}
```

---

### 📖 Explicación rápida
- **NotificationReceiver**: escucha eventos programados y dispara notificaciones.  
- **Intent extras**: se usan para pasar datos como `NOTIFICATION_ID`, `TITLE`, `MESSAGE`.  
- **showScheduledNotification()**: aquí implementas la lógica real de la notificación (canal, builder, manager).  

---

- `NotificationReceiver.kt` → Notificaciones programadas
- ## ⏰ NotificationReceiver

Este `BroadcastReceiver` se encarga de **recibir eventos programados** (como alarmas o `PendingIntent`) y mostrar notificaciones en el momento indicado.  
Es útil para recordatorios de notas o tareas agendadas.

### 📌 Flujo de trabajo
1. **onReceive** → se ejecuta cuando el evento programado se dispara.  
   - Extrae datos del `Intent` (ID de notificación, título, mensaje).  
   - Llama a `showScheduledNotification()` para mostrar la notificación.  

2. **showScheduledNotification** → aquí implementas la lógica con `NotificationManager` y `NotificationCompat.Builder` para construir y mostrar la notificación en la bandeja del sistema.  

```kotlin
package mx.edu.utng.grgc.aplicacionotas.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationReceiver : BroadcastReceiver() {

    private val TAG = "NotificationReceiver"

    /**
     * Este método se llama cuando el evento programado (como una alarma o un PendingIntent) se dispara.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Notificación programada recibida.")

        // 1. Extraer datos del Intent
        val notificationId = intent?.getIntExtra("NOTIFICATION_ID", 0) ?: 0
        val title = intent?.getStringExtra("TITLE")
        val message = intent?.getStringExtra("MESSAGE")

        // 2. Mostrar la notificación si los datos son válidos
        if (context != null && title != null && message != null) {
            showScheduledNotification(context, notificationId, title, message)
        }
    }

    private fun showScheduledNotification(context: Context, id: Int, title: String, message: String) {
        // Implementar aquí la lógica para construir y mostrar la notificación
        // usando NotificationManager y NotificationCompat.Builder,
        // similar a como lo harías en MyFirebaseMessagingService.
        Log.d(TAG, "Mostrando notificación: $title - $message")
    }
}
 
```
---

### 📖 Explicación rápida
- **Plugins**: Android Application, Kotlin, Compose y Google Services.  
- **Android config**: namespace, SDKs, versiones, ProGuard y compatibilidad con Java 11.  
- **Dependencias principales**:
  - Jetpack Compose (UI moderna).  
  - Firebase (Auth, Firestore, Messaging).  
  - Coroutines (manejo asíncrono).  
  - WorkManager (notificaciones programadas).  
  - Testing (JUnit, Espresso, Compose Testing).  

---
