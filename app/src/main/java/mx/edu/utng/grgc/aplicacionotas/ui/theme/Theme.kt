package mx.edu.utng.grgc.aplicacionotas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * 🎨 Tema de la Aplicación
 *
 * Define los colores y estilos visuales de toda la app.
 * Es como el libro de estilo de una marca 🎨
 */

// ============================================
// PALETA DE COLORES
// ============================================
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6366F1),        // Azul/Morado principal
    onPrimary = Color.White,             // Texto sobre el color primario
    primaryContainer = Color(0xFFE0E7FF), // Fondo claro del color primario
    onPrimaryContainer = Color(0xFF1E1B4B), // Texto sobre primaryContainer

    secondary = Color(0xFF10B981),       // Verde
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1FAE5),
    onSecondaryContainer = Color(0xFF064E3B),

    tertiary = Color(0xFFF59E0B),        // Amarillo/Naranja
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = Color(0xFF78350F),

    error = Color(0xFFEF4444),           // Rojo para errores
    onError = Color.White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF7F1D1D),

    background = Color(0xFFF5F5F5),      // Fondo general
    onBackground = Color(0xFF1F2937),    // Texto sobre fondo

    surface = Color.White,               // Tarjetas y superficies
    onSurface = Color(0xFF1F2937),       // Texto sobre superficies
    onSurfaceVariant = Color(0xFF6B7280) // Texto secundario
)

/**
 * 🌙 Tema Oscuro
 */
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF818CF8),
    onPrimary = Color(0xFF1E1B4B),
    primaryContainer = Color(0xFF312E81),
    onPrimaryContainer = Color(0xFFE0E7FF),

    secondary = Color(0xFF34D399),
    onSecondary = Color(0xFF064E3B),
    secondaryContainer = Color(0xFF065F46),
    onSecondaryContainer = Color(0xFFD1FAE5),

    tertiary = Color(0xFFFBBF24),
    onTertiary = Color(0xFF78350F),
    tertiaryContainer = Color(0xFF92400E),
    onTertiaryContainer = Color(0xFFFEF3C7),

    error = Color(0xFFF87171),
    onError = Color(0xFF7F1D1D),
    errorContainer = Color(0xFF991B1B),
    onErrorContainer = Color(0xFFFEE2E2),

    background = Color(0xFF111827),
    onBackground = Color(0xFFF3F4F6),
    surface = Color(0xFF1F2937),
    onSurface = Color(0xFFF3F4F6),
    onSurfaceVariant = Color(0xFF9CA3AF)
)

/**
 * 🎨 Composable del Tema
 *
 * Envuelve toda la app con los colores y estilos definidos
 */
@Composable
fun NotesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Ahora sí responde al parámetro
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(), // Tipografía por defecto
        content = content
    )
}