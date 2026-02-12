package iesvdc.segdodam.recyclerviewmotos.data.datasources

import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity

/**
 * Data source local que gestiona los videojuegos en memoria.
 */
interface VideoGameLocalDataSource {
    fun getAllVideoGames(): List<VideoGameEntity>
    fun addVideoGame(videoGame: VideoGameEntity)
    fun updateVideoGame(pos: Int, videoGame: VideoGameEntity)
    fun deleteVideoGame(pos: Int)
    fun getVideoGameAt(pos: Int): VideoGameEntity?
    fun setInitialVideoGames(list: List<VideoGameEntity>)
}

/**
 * Implementación del data source local.
 */
class VideoGameLocalDataSourceImpl : VideoGameLocalDataSource {
    private val videoGames = mutableListOf<VideoGameEntity>()

    override fun getAllVideoGames(): List<VideoGameEntity> = videoGames

    override fun addVideoGame(videoGame: VideoGameEntity) {
        videoGames.add(videoGame)
    }

    override fun updateVideoGame(pos: Int, videoGame: VideoGameEntity) {
        if (pos in videoGames.indices) {
            videoGames[pos] = videoGame
        }
    }

    override fun deleteVideoGame(pos: Int) {
        if (pos in videoGames.indices) {
            videoGames.removeAt(pos)
        }
    }

    override fun getVideoGameAt(pos: Int): VideoGameEntity? = videoGames.getOrNull(pos)

    override fun setInitialVideoGames(list: List<VideoGameEntity>) {
        videoGames.clear()
        videoGames.addAll(list)
    }
}
