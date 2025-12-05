package mx.edu.utng.grgc.aplicacionotas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import mx.edu.utng.grgc.aplicacionotas.data.models.Category
import mx.edu.utng.grgc.aplicacionotas.data.models.Note
import mx.edu.utng.grgc.aplicacionotas.data.models.Priority
import mx.edu.utng.grgc.aplicacionotas.data.repository.FirebaseRepository

/**
 * ✏️ Pantalla de Editar Nota - COMPLETA
 *
 * Permite modificar TODOS los campos de una nota existente
 * Los cambios se guardan en Firebase automáticamente
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    note: Note,
    repository: FirebaseRepository,
    onNoteUpdated: () -> Unit,
    onBack: () -> Unit
) {
    // ============================================
    // ESTADOS (INICIALIZADOS CON DATOS DE LA NOTA)
    // ============================================
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }
    var selectedCategoryId by remember { mutableStateOf(note.categoryId) }
    var selectedPriority by remember { mutableStateOf(note.getPriorityEnum()) }
    var reminderDate by remember { mutableStateOf(note.reminderDate ?: "") }
    var showCategoryPicker by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Obtener categorías desde Firebase
    val categoriesFlow = repository.getCategoriesFlow()
    val categories by categoriesFlow.collectAsState(initial = emptyList())

    // Encontrar la categoría seleccionada
    val selectedCategory = categories.find { it.id == selectedCategoryId }

    val scope = rememberCoroutineScope()

    // ============================================
    // SCAFFOLD
    // ============================================
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Nota") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Botón Guardar
                    TextButton(
                        onClick = {
                            // Validación
                            when {
                                title.isBlank() -> {
                                    errorMessage = "El título es requerido"
                                }
                                content.isBlank() -> {
                                    errorMessage = "El contenido es requerido"
                                }
                                selectedCategoryId.isBlank() -> {
                                    errorMessage = "Selecciona una categoría"
                                }
                                else -> {
                                    // Actualizar en Firebase
                                    isLoading = true
                                    scope.launch {
                                        repository.updateNote(
                                            noteId = note.id,
                                            title = title,
                                            content = content,
                                            categoryId = selectedCategoryId,
                                            priority = selectedPriority,
                                            reminderDate = reminderDate.ifBlank { null }
                                        ).onSuccess {
                                            isLoading = false
                                            onNoteUpdated()
                                        }.onFailure { error ->
                                            isLoading = false
                                            errorMessage = error.message ?: "Error al actualizar"
                                        }
                                    }
                                }
                            }
                        },
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("GUARDAR", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ============================================
            // INFO: ESTÁS EDITANDO
            // ============================================
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Editando nota creada ${note.getFormattedDate()}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // ============================================
            // MENSAJE DE ERROR
            // ============================================
            if (errorMessage.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // ============================================
            // CAMPO: TÍTULO
            // ============================================
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    errorMessage = ""
                },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )

            // ============================================
            // CAMPO: CONTENIDO
            // ============================================
            OutlinedTextField(
                value = content,
                onValueChange = {
                    content = it
                    errorMessage = ""
                },
                label = { Text("Contenido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10,
                enabled = !isLoading
            )

            // ============================================
            // SECCIÓN: CATEGORÍA
            // ============================================
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Categoría",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { showCategoryPicker = true },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        selectedCategory?.let { category ->
                            Text(category.emoji, fontSize = 20.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(category.nombre)
                        } ?: Text("Seleccionar categoría")

                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            }

            // ============================================
            // SECCIÓN: PRIORIDAD
            // ============================================
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Prioridad",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Priority.values().forEach { priority ->
                            FilterChip(
                                selected = selectedPriority == priority,
                                onClick = { selectedPriority = priority },
                                label = { Text(priority.displayName) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = priority.toColor().copy(alpha = 0.2f)
                                ),
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading
                            )
                        }
                    }
                }
            }

            // ============================================
            // SECCIÓN: RECORDATORIO
            // ============================================
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Recordatorio",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = reminderDate,
                        onValueChange = { reminderDate = it },
                        label = { Text("Fecha y hora") },
                        placeholder = { Text("Opcional") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.CalendarToday, contentDescription = null)
                        },
                        enabled = !isLoading
                    )
                }
            }

            // ============================================
            // INFO: ARCHIVOS ADJUNTOS (SI HAY)
            // ============================================
            if (note.attachments.isNotEmpty()) {
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Archivos Adjuntos",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))

                        note.attachments.forEach { attachment ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = attachment,
                                    fontSize = 12.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    Icons.Default.AttachFile,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }

    // ============================================
    // DIALOG: SELECTOR DE CATEGORÍA
    // ============================================
    if (showCategoryPicker) {
        AlertDialog(
            onDismissRequest = { showCategoryPicker = false },
            title = { Text("Cambiar Categoría") },
            text = {
                Column {
                    categories.forEach { category ->
                        TextButton(
                            onClick = {
                                selectedCategoryId = category.id
                                showCategoryPicker = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(category.emoji, fontSize = 24.sp)
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = category.nombre,
                                modifier = Modifier.weight(1f)
                            )
                            if (selectedCategoryId == category.id) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCategoryPicker = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}