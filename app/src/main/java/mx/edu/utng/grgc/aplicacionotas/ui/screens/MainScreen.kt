package mx.edu.utng.grgc.aplicacionotas.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import mx.edu.utng.grgc.aplicacionotas.data.models.Category
import mx.edu.utng.grgc.aplicacionotas.data.models.Note
import mx.edu.utng.grgc.aplicacionotas.data.models.Priority
import mx.edu.utng.grgc.aplicacionotas.data.repository.FirebaseRepository
import mx.edu.utng.grgc.aplicacionotas.ui.components.NoteCard

/**
 * 🏠 Pantalla Principal - El Centro de Control
 *
 * Aquí pasa toda la acción:
 * - Lista de notas en tiempo real (Flow de Firebase)
 * - Búsqueda instantánea
 * - Filtros por categoría y prioridad
 * - Crear, editar, eliminar notas
 * - Cambio de tema claro/oscuro
 * - Gestión de categorías
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    repository: FirebaseRepository,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onCreateNote: () -> Unit,
    onEditNote: (Note) -> Unit,
    onNavigateToCategories: () -> Unit,
    onLogout: () -> Unit
) {
    // ============================================
    // ESTADOS
    // ============================================
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedPriority by remember { mutableStateOf<Priority?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    // Estados de Firebase (en tiempo real)
    val notesFlow = repository.getUserNotesFlow()
    val categoriesFlow = repository.getCategoriesFlow()

    val notes by notesFlow.collectAsState(initial = emptyList())
    val categories by categoriesFlow.collectAsState(initial = emptyList())

    val currentUser by produceState<mx.edu.utng.grgc.aplicacionotas.data.models.User?>(
        initialValue = null
    ) {
        value = repository.getCurrentUser()
    }

    val scope = rememberCoroutineScope()

    // ============================================
    // FILTRADO DE NOTAS
    // ============================================
    val filteredNotes = remember(searchQuery, selectedCategory, selectedPriority, notes, categories) {
        notes.filter { note ->
            val matchesSearch = searchQuery.isEmpty() ||
                    note.title.contains(searchQuery, ignoreCase = true) ||
                    note.content.contains(searchQuery, ignoreCase = true)

            val matchesCategory = selectedCategory == null ||
                    note.categoryId == selectedCategory!!.id

            val matchesPriority = selectedPriority == null ||
                    note.getPriorityEnum() == selectedPriority

            matchesSearch && matchesCategory && matchesPriority
        }
    }

    // Crear mapa de categorías para acceso rápido
    val categoryMap = remember(categories) {
        categories.associateBy { it.id }
    }

    // ============================================
    // SCAFFOLD (ESTRUCTURA PRINCIPAL)
    // ============================================
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Mis Notas")
                        currentUser?.let { user ->
                            Text(
                                text = "Hola, ${user.nombre}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                },
                actions = {
                    // Botón de gestión de categorías
                    IconButton(onClick = onNavigateToCategories) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Gestionar categorías"
                        )
                    }
                    // Botón de cambio de tema
                    IconButton(onClick = { onThemeChange(!isDarkMode) }) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkMode) "Cambiar a modo claro" else "Cambiar a modo oscuro"
                        )
                    }
                    // Botón de logout
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNote,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Crear nota",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ============================================
            // BARRA DE BÚSQUEDA
            // ============================================
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar notas...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar")
                        }
                    }
                },
                singleLine = true
            )

            // ============================================
            // FILTROS DE CATEGORÍA
            // ============================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Chip "Todas"
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { selectedCategory = null },
                    label = { Text("Todas") }
                )

                // Chips de categorías
                categories.forEach { category ->
                    FilterChip(
                        selected = selectedCategory?.id == category.id,
                        onClick = {
                            selectedCategory = if (selectedCategory?.id == category.id) {
                                null
                            } else {
                                category
                            }
                        },
                        label = {
                            Row {
                                Text(category.emoji)
                                Spacer(Modifier.width(4.dp))
                                Text(category.nombre)
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // ============================================
            // FILTROS DE PRIORIDAD
            // ============================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Prioridad:",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontSize = 14.sp
                )

                FilterChip(
                    selected = selectedPriority == null,
                    onClick = { selectedPriority = null },
                    label = { Text("Todas") }
                )

                Priority.values().forEach { priority ->
                    FilterChip(
                        selected = selectedPriority == priority,
                        onClick = {
                            selectedPriority = if (selectedPriority == priority) {
                                null
                            } else {
                                priority
                            }
                        },
                        label = { Text(priority.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = priority.toColor().copy(alpha = 0.2f)
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ============================================
            // LISTA DE NOTAS
            // ============================================
            if (filteredNotes.isEmpty()) {
                // Estado vacío
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("📝", fontSize = 64.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = if (searchQuery.isNotEmpty()) {
                            "No se encontraron notas"
                        } else {
                            "No hay notas aún"
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (searchQuery.isNotEmpty()) {
                            "Intenta con otra búsqueda"
                        } else {
                            "Presiona + para crear tu primera nota"
                        },
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Lista de notas
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    filteredNotes.forEach { note ->
                        NoteCard(
                            note = note,
                            category = categoryMap[note.categoryId],
                            onEdit = { onEditNote(note) },
                            onDelete = {
                                noteToDelete = note
                                showDeleteDialog = true
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                    Spacer(Modifier.height(80.dp)) // Espacio para el FAB
                }
            }
        }
    }

    // ============================================
    // DIALOG DE CONFIRMACIÓN DE ELIMINACIÓN
    // ============================================
    if (showDeleteDialog && noteToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("¿Eliminar nota?") },
            text = {
                Text("Esta acción no se puede deshacer. La nota se eliminará permanentemente de Firebase.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            repository.deleteNote(noteToDelete!!.id)
                                .onSuccess {
                                    showDeleteDialog = false
                                    noteToDelete = null
                                }
                                .onFailure {
                                    // Manejar error si es necesario
                                    showDeleteDialog = false
                                }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}