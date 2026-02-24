package iesvdc.segdodam.recyclerviewmotos.Controller

import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import iesvdc.segdodam.recyclerviewmotos.domain.usecases.*
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame

/**
 * Controller clásico que usa los casos de uso de Clean Architecture.
 * NOTA: Este Controller es OPCIONAL con Hilt, ya que puedes inyectar los UseCases
 * directamente en ViewModels y Fragments. Se mantiene como alternativa.
 */
class Controller(
    private val getAllVideoGamesUseCase: GetAllVideoGamesUseCase,
    private val addVideoGameUseCase: AddVideoGameUseCase,
    private val updateVideoGameUseCase: UpdateVideoGameUseCase,
    private val deleteVideoGameUseCase: DeleteVideoGameUseCase,
    private val getVideoGameAtUseCase: GetVideoGameAtUseCase,
    private val setInitialVideoGamesUseCase: SetInitialVideoGamesUseCase
) {

    suspend fun getAllVideoGames(): List<VideoGame> {
        return getAllVideoGamesUseCase().map { entity ->
            VideoGame(
                id = entity.id,
                nombre = entity.nombre,
                precio = entity.precio,
                plataforma = entity.plataforma,
                caracteristicas = entity.caracteristicas,
                puntuacion = entity.puntuacion,
                visitas = entity.visitas
            )
        }
    }

    suspend fun addVideoGame(videoGame: VideoGame) {
        val entity = VideoGameEntity(
            id = videoGame.id,
            nombre = videoGame.nombre,
            precio = videoGame.precio,
            plataforma = videoGame.plataforma,
            caracteristicas = videoGame.caracteristicas,
            puntuacion = videoGame.puntuacion,
            visitas = videoGame.visitas
        )
        addVideoGameUseCase(entity)
    }

    suspend fun updateVideoGame(pos: Int, videoGame: VideoGame) {
        val entity = VideoGameEntity(
            id = videoGame.id,
            nombre = videoGame.nombre,
            precio = videoGame.precio,
            plataforma = videoGame.plataforma,
            caracteristicas = videoGame.caracteristicas,
            puntuacion = videoGame.puntuacion,
            visitas = videoGame.visitas
        )
        updateVideoGameUseCase(pos, entity)
    }

    suspend fun deleteVideoGame(pos: Int, videoGame: VideoGame) {
        val entity = VideoGameEntity(
            id = videoGame.id,
            nombre = videoGame.nombre,
            precio = videoGame.precio,
            plataforma = videoGame.plataforma,
            caracteristicas = videoGame.caracteristicas,
            puntuacion = videoGame.puntuacion,
            visitas = videoGame.visitas
        )
        deleteVideoGameUseCase(pos, entity)
    }

    suspend fun getVideoGameAt(pos: Int): VideoGame? {
        val entity = getVideoGameAtUseCase(pos) ?: return null
        return VideoGame(
            id = entity.id,
            nombre = entity.nombre,
            precio = entity.precio,
            plataforma = entity.plataforma,
            caracteristicas = entity.caracteristicas,
            puntuacion = entity.puntuacion,
            visitas = entity.visitas
        )
    }

    suspend fun setInitialVideoGames(list: List<VideoGame>) {
        val entities = list.map { videoGame ->
            VideoGameEntity(
                id = videoGame.id,
                nombre = videoGame.nombre,
                precio = videoGame.precio,
                plataforma = videoGame.plataforma,
                caracteristicas = videoGame.caracteristicas,
                puntuacion = videoGame.puntuacion,
                visitas = videoGame.visitas
            )
        }
        setInitialVideoGamesUseCase(entities)
    }
}