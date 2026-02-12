package iesvdc.segdodam.recyclerviewmotos.data.repositories

import iesvdc.segdodam.recyclerviewmotos.data.datasources.VideoGameLocalDataSource
import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import iesvdc.segdodam.recyclerviewmotos.domain.repositories.VideoGameRepository

/**
 * Implementación del repositorio en la capa de datos.
 * Implementa la interfaz del dominio y delega al data source.
 */
class VideoGameRepositoryImpl(
    private val localDataSource: VideoGameLocalDataSource
) : VideoGameRepository {

    override fun getAllVideoGames(): List<VideoGameEntity> = localDataSource.getAllVideoGames()

    override fun addVideoGame(videoGame: VideoGameEntity) = localDataSource.addVideoGame(videoGame)

    override fun updateVideoGame(pos: Int, videoGame: VideoGameEntity) =
        localDataSource.updateVideoGame(pos, videoGame)

    override fun deleteVideoGame(pos: Int) = localDataSource.deleteVideoGame(pos)

    override fun getVideoGameAt(pos: Int): VideoGameEntity? = localDataSource.getVideoGameAt(pos)

    override fun setInitialVideoGames(list: List<VideoGameEntity>) =
        localDataSource.setInitialVideoGames(list)
}
