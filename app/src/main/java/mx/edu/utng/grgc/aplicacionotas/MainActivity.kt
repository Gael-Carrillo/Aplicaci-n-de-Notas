package mx.edu.utng.grgc.aplicacionotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import mx.edu.utng.grgc.aplicacionotas.data.repository.FirebaseRepository
import mx.edu.utng.grgc.aplicacionotas.navigation.NotesAppNavigation
import mx.edu.utng.grgc.aplicacionotas.ui.theme.NotesAppTheme

/**
 * 🎬 MainActivity - El Corazón de la Aplicación
 *
 * Esta es la ÚNICA Activity de toda la app.
 * Todo lo demás son Composables (pantallas) que se muestran aquí.
 *
 * Es como el escenario de un teatro donde se presentan diferentes obras 🎭
 */
class MainActivity : ComponentActivity() {

    // Instancia única del repositorio
    private val repository = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Estado del modo oscuro (por defecto en false = modo claro)
            var isDarkMode by remember { mutableStateOf(false) }

            NotesAppTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Sistema de navegación que maneja todas las pantallas
                    NotesAppNavigation(
                        repository = repository,
                        isDarkMode = isDarkMode,
                        onThemeChange = { isDarkMode = it }
                    )
                }
            }
        }
    }
}