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
import mx.edu.utng.grgc.aplicacionotas.data.models.Priority
import mx.edu.utng.grgc.aplicacionotas.data.repository.FirebaseRepository

/**
 * ✏️ Pantalla de Crear Nota - COMPLETA CON FIREBASE
 *
 * Permite crear notas con:
 * - Título y contenido
 * - Selección de categoría
 * - Nivel de prioridad
 * - Recordatorio opcional
 *
 * Todo se guarda automáticamente en Firebase
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScreen(
    repository: FirebaseRepository,
    onNoteCreated: () -> Unit,
    onBack: () -> Unit
) {
    // ============================================
    // ESTADOS
    // ============================================
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIA) }
    var reminderDate by remember { mutableStateOf("") }
    var showCategoryPicker by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Obtener categorías desde Firebase
    val categoriesFlow = repository.getCategoriesFlow()
    val categories by categoriesFlow.collectAsState(initial = emptyList())

    // Seleccionar primera categoría por defecto
    LaunchedEffect(categories) {
        if (selectedCategory == null && categories.isNotEmpty()) {
            selectedCategory = categories.first()
        }
    }

    val scope = rememberCoroutineScope()

    // ============================================
    // SCAFFOLD
    // ============================================
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Nota") },
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
                                selectedCategory == null -> {
                                    errorMessage = "Selecciona una categoría"
                                }
                                else -> {
                                    // Guardar en Firebase
                                    isLoading = true
                                    scope.launch {
                                        repository.createNote(
                                            title = title,
                                            content = content,
                                            categoryId = selectedCategory!!.id,
                                            priority = selectedPriority,
                                            reminderDate = reminderDate.ifBlank { null }
                                        ).onSuccess {
                                            isLoading = false
                                            onNoteCreated()
                                        }.onFailure { error ->
                                            isLoading = false
                                            errorMessage = error.message ?: "Error al guardar"
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
                placeholder = { Text("Escribe el título de tu nota...") },
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
                placeholder = { Text("¿Qué quieres recordar?") },
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
            // SECCIÓN: RECORDATORIO (OPCIONAL)
            // ============================================
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Recordatorio (Opcional)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = reminderDate,
                        onValueChange = { reminderDate = it },
                        label = { Text("Fecha y hora") },
                        placeholder = { Text("Ej: Mañana 10:00 AM") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.CalendarToday, contentDescription = null)
                        },
                        enabled = !isLoading
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "💡 Tip: Escribe la fecha de forma natural",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
            title = { Text("Selecciona una Categoría") },
            text = {
                Column {
                    categories.forEach { category ->
                        TextButton(
                            onClick = {
                                selectedCategory = category
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
                            if (selectedCategory?.id == category.id) {
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