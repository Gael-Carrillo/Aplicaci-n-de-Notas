package mx.edu.utng.grgc.aplicacionotas.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.grgc.aplicacionotas.data.models.Category
import mx.edu.utng.grgc.aplicacionotas.data.models.Note

/**
 * 📋 Tarjeta de Nota - Versión Completa
 *
 * Muestra toda la información de una nota:
 * - Categoría con emoji y color
 * - Prioridad (badge)
 * - Título y contenido
 * - Timestamp
 * - Indicadores de recordatorio y adjuntos
 * - Botones de editar y eliminar
 *
 * Es como la tarjeta de presentación de una nota 🎴
 */
@Composable
fun NoteCard(
    note: Note,
    category: Category?,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // ============================================
            // HEADER: Categoría y Prioridad
            // ============================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Categoría
                if (category != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category.emoji,
                            fontSize = 20.sp
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = category.nombre,
                            fontSize = 12.sp,
                            color = category.toColor()
                        )
                    }
                }

                // Prioridad
                PriorityBadge(priority = note.getPriorityEnum())
            }

            Spacer(Modifier.height(12.dp))

            // ============================================
            // TÍTULO Y ACCIONES
            // ============================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = note.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // ============================================
            // CONTENIDO
            // ============================================
            Text(
                text = note.content,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3
            )

            Spacer(Modifier.height(12.dp))

            // ============================================
            // FOOTER: Timestamp e Indicadores
            // ============================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Timestamp
                Text(
                    text = note.getFormattedDate(),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Indicadores
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Recordatorio
                    if (note.reminderDate != null) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Recordatorio",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Adjuntos
                    if (note.attachments.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.AttachFile,
                                contentDescription = "Adjuntos",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${note.attachments.size}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}