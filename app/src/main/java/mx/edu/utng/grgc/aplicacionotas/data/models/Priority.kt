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