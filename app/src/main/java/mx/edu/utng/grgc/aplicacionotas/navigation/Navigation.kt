package mx.edu.utng.grgc.aplicacionotas.navigation

import androidx.compose.runtime.*
import mx.edu.utng.grgc.aplicacionotas.data.models.Note
import mx.edu.utng.grgc.aplicacionotas.data.repository.FirebaseRepository
import mx.edu.utng.grgc.aplicacionotas.ui.screens.*

/**
 * 🧭 Sistema de Navegación
 *
 * Define todas las pantallas posibles de la app.
 * Es como el mapa de un centro comercial 🗺️
 */
sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object Main : Screen()
    object CreateNote : Screen()
    object CategoriesManagement : Screen()
    data class EditNote(val note: Note) : Screen()
}

/**
 * 🎬 Coordinador de Navegación
 *
 * Maneja el flujo entre pantallas.
 * Es como el director de una película que decide qué escena sigue.
 */
@Composable
fun NotesAppNavigation(
    repository: FirebaseRepository,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    // Estado actual de la pantalla
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

    // Navegar según la pantalla actual
    when (val screen = currentScreen) {
        // ============================================
        // PANTALLA: LOGIN
        // ============================================
        is Screen.Login -> {
            LoginScreen(
                repository = repository,
                isDarkMode = isDarkMode,
                onThemeChange = onThemeChange,
                onLoginSuccess = {
                    currentScreen = Screen.Main
                },
                onNavigateToRegister = {
                    currentScreen = Screen.Register
                }
            )
        }

        // ============================================
        // PANTALLA: REGISTRO
        // ============================================
        is Screen.Register -> {
            RegisterScreen(
                repository = repository,
                onRegisterSuccess = {
                    // Después de registrarse, va al login
                    currentScreen = Screen.Login
                },
                onNavigateToLogin = {
                    currentScreen = Screen.Login
                }
            )
        }

        // ============================================
        // PANTALLA: PRINCIPAL
        // ============================================
        is Screen.Main -> {
            MainScreen(
                repository = repository,
                isDarkMode = isDarkMode,
                onThemeChange = onThemeChange,
                onCreateNote = {
                    currentScreen = Screen.CreateNote
                },
                onEditNote = { note ->
                    currentScreen = Screen.EditNote(note)
                },
                onNavigateToCategories = {
                    currentScreen = Screen.CategoriesManagement
                },
                onLogout = {
                    repository.logout()
                    currentScreen = Screen.Login
                }
            )
        }

        // ============================================
        // PANTALLA: CREAR NOTA
        // ============================================
        is Screen.CreateNote -> {
            CreateNoteScreen(
                repository = repository,
                onNoteCreated = {
                    currentScreen = Screen.Main
                },
                onBack = {
                    currentScreen = Screen.Main
                }
            )
        }

        // ============================================
        // PANTALLA: EDITAR NOTA
        // ============================================
        is Screen.EditNote -> {
            EditNoteScreen(
                note = screen.note,
                repository = repository,
                onNoteUpdated = {
                    currentScreen = Screen.Main
                },
                onBack = {
                    currentScreen = Screen.Main
                }
            )
        }

        // ============================================
        // PANTALLA: GESTIÓN DE CATEGORÍAS
        // ============================================
        is Screen.CategoriesManagement -> {
            CategoriesManagementScreen(
                repository = repository,
                onBack = {
                    currentScreen = Screen.Main
                }
            )
        }
    }
}