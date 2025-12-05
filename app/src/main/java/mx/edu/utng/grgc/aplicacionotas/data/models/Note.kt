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