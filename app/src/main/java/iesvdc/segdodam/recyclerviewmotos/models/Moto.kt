package iesvdc.segdodam.recyclerviewmotos.models

/**
 * `VideoGame.kt`
 * Modelo de datos que representa un videojuego.
 *
 * Usamos una `data class` porque nos proporciona automáticamente
 * métodos útiles como `equals()`, `hashCode()`, `toString()`, `copy()`, etc.
 *
 * @property id Identificador único del videojuego.
 * @property nombre Nombre del videojuego.
 * @property precio Precio del videojuego.
 * @property plataforma Plataforma del videojuego.
 * @property caracteristicas Descripción de las características principales.
 */
data class VideoGame(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val plataforma: String,
    val caracteristicas: String
)
