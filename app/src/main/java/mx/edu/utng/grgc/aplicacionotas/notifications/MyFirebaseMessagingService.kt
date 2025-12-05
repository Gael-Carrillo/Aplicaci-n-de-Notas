package mx.edu.utng.grgc.aplicacionotas.notifications
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FCM_Service"

    /**
     * 1. Se llama cuando un nuevo token de registro de dispositivo se genera o actualiza.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Nuevo Token: $token")

        // **LÓGICA REQUERIDA:**
        // 1. Envía este token a tu servidor si estás manejando notificaciones desde tu propio backend.
        //    Tu servidor necesita este token para saber dónde enviar las notificaciones.
        // 2. Si el usuario cerró sesión, podrías guardarlo localmente y enviarlo al servidor después de iniciar sesión.

        // Ejemplo simple (solo registro en consola):
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        // Implementar aquí la llamada a una API o base de datos para guardar el token.
    }

    /**
     * 2. Se llama cuando un mensaje (Data Message o Notification Message en primer plano) es recibido.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Mensaje de: ${remoteMessage.from}")

        // **LÓGICA REQUERIDA:**

        // 1. Manejar Mensajes de Datos
        // Los 'data' son pares clave-valor que enviaste desde Firebase.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Payload de datos: " + remoteMessage.data)

            // Ejemplo: obtener un título y cuerpo personalizado
            val customTitle = remoteMessage.data["title"]
            val customBody = remoteMessage.data["body"]

            // Llama a una función para construir y mostrar la notificación
            customTitle?.let {
                showNotification(it, customBody ?: "Contenido de la nota actualizado")
            }
        }

        // 2. Manejar Mensajes de Notificación
        // Esto solo ocurre si la app está en primer plano (abierta y en uso)
        remoteMessage.notification?.let {
            Log.d(TAG, "Cuerpo de la notificación: ${it.body}")

            // Ya que estás en primer plano, Android NO la mostrará automáticamente.
            // Debes construirla y mostrarla tú mismo.
            showNotification(it.title ?: "Nueva Nota", it.body ?: "Revisa la aplicación")
        }
    }

    // Función auxiliar para construir y mostrar la notificación
    private fun showNotification(title: String, body: String) {
        // Aquí debes implementar la lógica usando NotificationManager
        // para crear y mostrar una notificación en la bandeja del sistema.
        // Esto generalmente implica:
        // 1. Crear un NotificationChannel (solo para Android O/API 26+)
        // 2. Crear un PendingIntent para la acción al hacer clic
        // 3. Crear el objeto NotificationCompat.Builder
        // 4. Llamar a NotificationManager.notify()
    }
}