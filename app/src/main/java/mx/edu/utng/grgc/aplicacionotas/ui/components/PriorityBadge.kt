package mx.edu.utng.grgc.aplicacionotas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.grgc.aplicacionotas.data.models.Priority

/**
 * ⭐ Insignia de Prioridad
 *
 * Componente pequeño que muestra la prioridad de una nota
 * con su color correspondiente
 *
 * Es como una etiqueta de precio en una tienda 🏷️
 */
@Composable
fun PriorityBadge(priority: Priority) {
    Text(
        text = priority.displayName,
        fontSize = 11.sp,
        color = Color.White,
        modifier = Modifier
            .background(
                color = priority.toColor(),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}