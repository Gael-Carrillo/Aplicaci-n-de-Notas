package mx.edu.utng.grgc.aplicacionotas.notifications
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationReceiver : BroadcastReceiver() {

    private val TAG = "NotificationReceiver"

    /**
     * Este método se llama cuando el evento programado (como una alarma o un PendingIntent) se dispara.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Notificación programada recibida.")

        // **LÓGICA REQUERIDA:**
        // 1. Extraer datos del Intent (si enviaste título, cuerpo, ID de nota, etc.)
        val notificationId = intent?.getIntExtra("NOTIFICATION_ID", 0) ?: 0
        val title = intent?.getStringExtra("TITLE")
        val message = intent?.getStringExtra("MESSAGE")

        // 2. Aquí debes llamar a una función para construir y mostrar la notificación
        if (context != null && title != null && message != null) {
            showScheduledNotification(context, notificationId, title, message)
        }
    }

    private fun showScheduledNotification(context: Context, id: Int, title: String, message: String) {
        // Implementar aquí la lógica para construir y mostrar la notificación
        // usando el NotificationManager y NotificationCompat.Builder,
        // similar a como lo harías en MyFirebaseMessagingService.
        Log.d(TAG, "Mostrando notificación: $title - $message")
    }
}