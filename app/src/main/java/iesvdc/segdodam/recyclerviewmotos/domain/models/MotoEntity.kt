package iesvdc.segdodam.recyclerviewmotos.domain.models

/**
 * Entity del dominio que representa un videojuego.
 * No tiene dependencias externas.
 */
data class VideoGameEntity(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val plataforma: String,
    val caracteristicas: String
)
