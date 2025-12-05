package mx.edu.utng.grgc.aplicacionotas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
 * 📝 Pantalla de Registro
 *
 * Donde los nuevos usuarios crean su cuenta
 * Como llenar un formulario de inscripción 📋
 *
 * Validaciones:
 * - Nombre no vacío
 * - Email válido
 * - Contraseña mínimo 6 caracteres
 * - Contraseñas coinciden
 */
@Composable
fun RegisterScreen(
    repository: FirebaseRepository,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ============================================
        // TÍTULO
        // ============================================
        Text(
            text = "📝 Crear Cuenta",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Únete y comienza a organizar tus notas",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(32.dp))

        // ============================================
        // FORMULARIO
        // ============================================

        // Campo Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorMessage = ""
            },
            label = { Text("Nombre completo") },
            placeholder = { Text("Juan Pérez") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(Modifier.height(16.dp))

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
            placeholder = { Text("Mínimo 6 caracteres") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(Modifier.height(16.dp))

        // Campo Confirmar Contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                errorMessage = ""
            },
            label = { Text("Confirmar contraseña") },
            placeholder = { Text("Repite tu contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )

        // Mensaje de Error
        if (errorMessage.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // ============================================
        // BOTÓN DE REGISTRO
        // ============================================
        Button(
            onClick = {
                // Validación completa
                when {
                    nombre.isBlank() -> {
                        errorMessage = "El nombre es requerido"
                    }
                    nombre.length < 3 -> {
                        errorMessage = "El nombre debe tener al menos 3 caracteres"
                    }
                    email.isBlank() -> {
                        errorMessage = "El email es requerido"
                    }
                    !email.contains("@") || !email.contains(".") -> {
                        errorMessage = "Email inválido"
                    }
                    password.isBlank() -> {
                        errorMessage = "La contraseña es requerida"
                    }
                    password.length < 6 -> {
                        errorMessage = "La contraseña debe tener al menos 6 caracteres"
                    }
                    confirmPassword.isBlank() -> {
                        errorMessage = "Confirma tu contraseña"
                    }
                    password != confirmPassword -> {
                        errorMessage = "Las contraseñas no coinciden"
                    }
                    else -> {
                        // Intentar registro
                        isLoading = true
                        scope.launch {
                            repository.registerUser(nombre, email, password)
                                .onSuccess {
                                    isLoading = false
                                    onRegisterSuccess()
                                }
                                .onFailure { error ->
                                    isLoading = false
                                    errorMessage = error.message ?: "Error al registrar"
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
                Text("Registrarse", fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(16.dp))

        // ============================================
        // LINK A LOGIN
        // ============================================
        TextButton(
            onClick = onNavigateToLogin,
            enabled = !isLoading
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}