package iesvdc.segdodam.recyclerviewmotos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un videojuego favorito en la base de datos local.
 */
@Entity(tableName = "favorite_video_games")
data class FavoriteVideoGame(
    @PrimaryKey val id: Int,
    val nombre: String,
    val precio: Double,
    val plataforma: String,
    val caracteristicas: String,
    val puntuacion: Float,
    val visitas: Long
)
