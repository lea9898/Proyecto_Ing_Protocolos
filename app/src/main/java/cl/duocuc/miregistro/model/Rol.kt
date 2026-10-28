package cl.duocuc.miregistro.model

// =====================================================================
//  NUEVO (Rol) — Rol.kt
//  ---------------------------------------------------------------------
//  Representa una fila de la tabla "roles".
//  Un rol tiene MUCHOS usuarios; cada usuario tiene UN rol (relación 1 a N).
// =====================================================================

data class Rol(
    val id: Long,
    val nombre: String
) {
    companion object {
        // NUEVO (Rol): id del rol que se asigna al registrarse desde el Formulario.
        // Coincide con el dato precargado en onCreate (1 = Técnico de terreno).
        const val ID_TECNICO_TERRENO = 1L
    }
}
