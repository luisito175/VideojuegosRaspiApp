package iesvdc.segdodam.recyclerviewmotos.data.repositories

import iesvdc.segdodam.recyclerviewmotos.data.datasources.VideoGameRemoteDataSource
import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import iesvdc.segdodam.recyclerviewmotos.domain.repositories.VideoGameRepository

/**
 * Implementación del repositorio en la capa de datos.
 * Implementa la interfaz del dominio y delega al data source.
 */
class VideoGameRepositoryImpl(
    private val remoteDataSource: VideoGameRemoteDataSource
) : VideoGameRepository {

    override suspend fun getAllVideoGames(): List<VideoGameEntity> =
        remoteDataSource.fetchVideoGames()

    override suspend fun addVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity> =
        remoteDataSource.addVideoGame(videoGame)

    override suspend fun updateVideoGame(pos: Int, videoGame: VideoGameEntity): List<VideoGameEntity> =
        remoteDataSource.updateVideoGame(videoGame)

    override suspend fun deleteVideoGame(pos: Int, videoGame: VideoGameEntity): List<VideoGameEntity> =
        remoteDataSource.deleteVideoGame(videoGame)

    override suspend fun getVideoGameAt(pos: Int): VideoGameEntity? =
        remoteDataSource.getVideoGameAt(pos)

    override suspend fun setInitialVideoGames(list: List<VideoGameEntity>) {
        // No-op: los datos vienen de la API.
    }
}
