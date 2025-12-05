package mx.edu.utng.grgc.aplicacionotas.data.models

import com.google.firebase.firestore.DocumentId

/**
 * 👤 Modelo de Usuario
 *
 * Representa a cada persona que usa la app.
 * Firebase usa @DocumentId para identificar únicamente cada usuario.
 *
 * @DocumentId: Es como tu número de identificación único
 * val id: No se puede cambiar (como tu huella digital)
 * var nombre: Se puede cambiar (como tu apodo)
 */
data class User(
    @DocumentId val id: String = "",
    val nombre: String = "",
    val email: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    // Constructor vacío requerido por Firebase
    constructor() : this("", "", "", 0L)
}