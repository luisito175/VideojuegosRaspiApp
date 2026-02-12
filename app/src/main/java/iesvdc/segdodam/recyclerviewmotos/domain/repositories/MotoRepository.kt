package iesvdc.segdodam.recyclerviewmotos.domain.repositories

import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity

/**
 * Interfaz del repositorio en el dominio.
 * Define los contratos que la capa de datos debe cumplir.
 */
interface VideoGameRepository {
    fun getAllVideoGames(): List<VideoGameEntity>
    fun addVideoGame(videoGame: VideoGameEntity)
    fun updateVideoGame(pos: Int, videoGame: VideoGameEntity)
    fun deleteVideoGame(pos: Int)
    fun getVideoGameAt(pos: Int): VideoGameEntity?
    fun setInitialVideoGames(list: List<VideoGameEntity>)
}
