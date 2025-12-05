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