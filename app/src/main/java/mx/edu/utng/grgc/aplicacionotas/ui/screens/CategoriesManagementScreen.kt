package mx.edu.utng.grgc.aplicacionotas.ui.screens

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import mx.edu.utng.grgc.aplicacionotas.data.models.Category
import mx.edu.utng.grgc.aplicacionotas.data.repository.FirebaseRepository

/**
 * ⚙️ Pantalla de Gestión de Categorías
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesManagementScreen(
    repository: FirebaseRepository,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val categoriesFlow = repository.getCategoriesFlow()
    val categories by categoriesFlow.collectAsState(initial = emptyList())

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Categorías") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            if (categories.size < 8) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar categoría")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Mensajes
            if (errorMessage.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(errorMessage)
                    }
                }
            }

            if (successMessage.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF10B981).copy(alpha = 0.2f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(successMessage)
                    }
                }
            }

            // Información
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Categorías: ${categories.size}/8",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Toca una categoría para editarla",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Lista de categorías
            categories.forEach { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            selectedCategory = category
                            showEditDialog = true
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(category.toColor(), CircleShape)
                            ) {
                                Text(
                                    text = category.emoji,
                                    modifier = Modifier.align(Alignment.Center),
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(Modifier.width(16.dp))

                            Text(
                                text = category.nombre,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        IconButton(
                            onClick = {
                                scope.launch {
                                    repository.deleteCategory(category.id)
                                        .onSuccess {
                                            successMessage = "Categoría eliminada"
                                            errorMessage = ""
                                        }
                                        .onFailure { error ->
                                            errorMessage = error.message ?: "Error al eliminar"
                                            successMessage = ""
                                        }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }

    // Diálogo agregar
    if (showAddDialog) {
        CategoryDialog(
            title = "Nueva Categoría",
            initialNombre = "",
            initialEmoji = "📌",
            initialColor = "0xFF6366F1",
            onDismiss = { showAddDialog = false },
            onConfirm = { nombre, emoji, colorHex ->
                scope.launch {
                    val newCategory = Category(
                        nombre = nombre,
                        emoji = emoji,
                        colorHex = colorHex
                    )
                    repository.createCategory(newCategory)
                        .onSuccess {
                            showAddDialog = false
                            successMessage = "Categoría creada"
                            errorMessage = ""
                        }
                        .onFailure { error ->
                            errorMessage = error.message ?: "Error al crear"
                            successMessage = ""
                        }
                }
            }
        )
    }

    // Diálogo editar
    if (showEditDialog && selectedCategory != null) {
        CategoryDialog(
            title = "Editar Categoría",
            initialNombre = selectedCategory!!.nombre,
            initialEmoji = selectedCategory!!.emoji,
            initialColor = selectedCategory!!.colorHex,
            onDismiss = {
                showEditDialog = false
                selectedCategory = null
            },
            onConfirm = { nombre, emoji, colorHex ->
                scope.launch {
                    repository.updateCategory(
                        categoryId = selectedCategory!!.id,
                        nombre = nombre,
                        emoji = emoji,
                        colorHex = colorHex
                    )
                        .onSuccess {
                            showEditDialog = false
                            selectedCategory = null
                            successMessage = "Categoría actualizada"
                            errorMessage = ""
                        }
                        .onFailure { error ->
                            errorMessage = error.message ?: "Error al actualizar"
                            successMessage = ""
                        }
                }
            }
        )
    }
}

@Composable
fun CategoryDialog(
    title: String,
    initialNombre: String,
    initialEmoji: String,
    initialColor: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(initialNombre) }
    var emoji by remember { mutableStateOf(initialEmoji) }
    var selectedColor by remember { mutableStateOf(initialColor) }

    val emojis = listOf("📌", "🎯", "⭐", "💼", "🏠", "🎨", "🎵", "📚", "🛒", "✈️", "🍔", "💡")
    val colors = listOf(
        "0xFF6366F1" to "Azul",
        "0xFFEF4444" to "Rojo",
        "0xFF10B981" to "Verde",
        "0xFFF59E0B" to "Naranja",
        "0xFF8B5CF6" to "Morado",
        "0xFFEC4899" to "Rosa",
        "0xFF06B6D4" to "Cian",
        "0xFF84CC16" to "Lima"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                Text("Emoji:", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    emojis.take(6).forEach { emojiOption ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (emoji == emojiOption) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    else Color.Transparent,
                                    CircleShape
                                )
                                .clickable { emoji = emojiOption },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(emojiOption, fontSize = 24.sp)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text("Color:", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    colors.forEach { (colorHex, colorName) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedColor = colorHex }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedColor == colorHex,
                                onClick = { selectedColor = colorHex }
                            )
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        Color(AndroidColor.parseColor("#${colorHex.substring(2)}")),
                                        CircleShape
                                    )
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(colorName)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nombre, emoji, selectedColor) },
                enabled = nombre.isNotBlank()
            ) {
                Text(if (initialNombre.isEmpty()) "Crear" else "Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}