package iesvdc.segdodam.recyclerviewmotos.data.repositories

import iesvdc.segdodam.recyclerviewmotos.data.datasources.VideoGameRemoteDataSource
import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import iesvdc.segdodam.recyclerviewmotos.domain.repositories.VideoGameRepository

import iesvdc.segdodam.recyclerviewmotos.data.local.dao.FavoriteVideoGameDao
import iesvdc.segdodam.recyclerviewmotos.data.local.entities.FavoriteVideoGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación del repositorio en la capa de datos.
 * Implementa la interfaz del dominio y delega al data source.
 */
class VideoGameRepositoryImpl(
    private val remoteDataSource: VideoGameRemoteDataSource,
    private val favoriteDao: FavoriteVideoGameDao
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

    override fun getFavoriteVideoGames(): Flow<List<VideoGameEntity>> {
        return favoriteDao.getAllFavorites().map { favorites ->
            favorites.map { fav ->
                VideoGameEntity(
                    id = fav.id,
                    nombre = fav.nombre,
                    precio = fav.precio,
                    plataforma = fav.plataforma,
                    caracteristicas = fav.caracteristicas,
                    puntuacion = fav.puntuacion,
                    visitas = fav.visitas
                )
            }
        }
    }

    override suspend fun addFavorite(videoGame: VideoGameEntity) {
        favoriteDao.insertFavorite(
            FavoriteVideoGame(
                id = videoGame.id,
                nombre = videoGame.nombre,
                precio = videoGame.precio,
                plataforma = videoGame.plataforma,
                caracteristicas = videoGame.caracteristicas,
                puntuacion = videoGame.puntuacion,
                visitas = videoGame.visitas
            )
        )
    }

    override suspend fun removeFavorite(videoGame: VideoGameEntity) {
        favoriteDao.deleteFavorite(
            FavoriteVideoGame(
                id = videoGame.id,
                nombre = videoGame.nombre,
                precio = videoGame.precio,
                plataforma = videoGame.plataforma,
                caracteristicas = videoGame.caracteristicas,
                puntuacion = videoGame.puntuacion,
                visitas = videoGame.visitas
            )
        )
    }

    override suspend fun isFavorite(id: Int): Boolean {
        return favoriteDao.isFavorite(id)
    }
}
