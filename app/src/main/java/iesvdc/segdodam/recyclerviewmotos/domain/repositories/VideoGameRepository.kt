package iesvdc.segdodam.recyclerviewmotos.domain.repositories

import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity

/**
 * Interfaz del repositorio en el dominio.
 * Define los contratos que la capa de datos debe cumplir.
 */
interface VideoGameRepository {
    suspend fun getAllVideoGames(): List<VideoGameEntity>
    suspend fun addVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun updateVideoGame(pos: Int, videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun deleteVideoGame(pos: Int, videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun getVideoGameAt(pos: Int): VideoGameEntity?
    suspend fun setInitialVideoGames(list: List<VideoGameEntity>)

    // Favorites
    fun getFavoriteVideoGames(): kotlinx.coroutines.flow.Flow<List<VideoGameEntity>>
    suspend fun addFavorite(videoGame: VideoGameEntity)
    suspend fun removeFavorite(videoGame: VideoGameEntity)
    suspend fun isFavorite(id: Int): Boolean
}
