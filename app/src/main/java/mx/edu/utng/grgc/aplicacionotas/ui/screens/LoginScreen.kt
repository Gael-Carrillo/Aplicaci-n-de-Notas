package mx.edu.utng.grgc.aplicacionotas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import mx.edu.utng.grgc.aplicacionotas.data.repository.FirebaseRepository

/**
 * 🔐 Pantalla de Login
 *
 * La puerta de entrada a la app.
 * Como la recepción de un hotel 🏨
 *
 * Funcionalidades:
 * - Login con email y contraseña
 * - Validación de campos
 * - Indicador de carga
 * - Navegación a registro
 * - Cambio de tema claro/oscuro
 */
@Composable
fun LoginScreen(
    repository: FirebaseRepository,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ============================================
        // BOTÓN DE MODO OSCURO (ARRIBA A LA DERECHA)
        // ============================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onThemeChange(!isDarkMode) }) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = if (isDarkMode) "Cambiar a modo claro" else "Cambiar a modo oscuro",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // ============================================
        // LOGO Y TÍTULO
        // ============================================
        Text(
            text = "📝",
            fontSize = 64.sp
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Mis Notas",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Organiza tu vida",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(48.dp))

        // ============================================
        // FORMULARIO
        // ============================================

        // Campo Email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = ""
            },
            label = { Text("Email") },
            placeholder = { Text("tu@email.com") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(Modifier.height(16.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = ""
            },
            label = { Text("Contraseña") },
            placeholder = { Text("••••••••") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )

        // Mensaje de Error
        if (errorMessage.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.height(24.dp))

        // ============================================
        // BOTÓN DE LOGIN
        // ============================================
        Button(
            onClick = {
                val finalEmail = email.trim()
                val finalPassword = password.trim()

                // Validación de campos
                when {
                    finalEmail.isBlank() -> {
                        errorMessage = "El email es requerido"
                    }
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(finalEmail).matches() -> {
                        errorMessage = "El formato del email no es válido"
                    }
                    finalPassword.isBlank() -> {
                        errorMessage = "La contraseña es requerida"
                    }
                    finalPassword.length < 6 -> {
                        errorMessage = "La contraseña debe tener al menos 6 caracteres"
                    }
                    else -> {
                        // Intentar login
                        isLoading = true
                        scope.launch {
                            repository.login(finalEmail, finalPassword)
                                .onSuccess {
                                    isLoading = false
                                    onLoginSuccess()
                                }
                                .onFailure { error ->
                                    isLoading = false
                                    errorMessage = when (error.message) {
                                        "There is no user record corresponding to this identifier. The user may have been deleted." -> "Usuario no encontrado."
                                        "Wrong password" -> "Contraseña incorrecta."
                                        else -> error.message ?: "Error al iniciar sesión"
                                    }
                                }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Iniciar Sesión", fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(16.dp))

        // ============================================
        // LINK A REGISTRO
        // ============================================
        TextButton(
            onClick = onNavigateToRegister,
            enabled = !isLoading
        ) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}
