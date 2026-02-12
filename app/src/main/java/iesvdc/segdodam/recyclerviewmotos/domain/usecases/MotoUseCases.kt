package iesvdc.segdodam.recyclerviewmotos.domain.usecases

import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import iesvdc.segdodam.recyclerviewmotos.domain.repositories.VideoGameRepository

/**
 * Caso de uso para obtener todos los videojuegos.
 */
class GetAllVideoGamesUseCase(private val repository: VideoGameRepository) {
    operator fun invoke(): List<VideoGameEntity> = repository.getAllVideoGames()
}

/**
 * Caso de uso para añadir un videojuego.
 */
class AddVideoGameUseCase(private val repository: VideoGameRepository) {
    operator fun invoke(videoGame: VideoGameEntity) = repository.addVideoGame(videoGame)
}

/**
 * Caso de uso para actualizar un videojuego.
 */
class UpdateVideoGameUseCase(private val repository: VideoGameRepository) {
    operator fun invoke(pos: Int, videoGame: VideoGameEntity) =
        repository.updateVideoGame(pos, videoGame)
}

/**
 * Caso de uso para eliminar un videojuego.
 */
class DeleteVideoGameUseCase(private val repository: VideoGameRepository) {
    operator fun invoke(pos: Int) = repository.deleteVideoGame(pos)
}

/**
 * Caso de uso para obtener un videojuego en una posición.
 */
class GetVideoGameAtUseCase(private val repository: VideoGameRepository) {
    operator fun invoke(pos: Int): VideoGameEntity? = repository.getVideoGameAt(pos)
}

/**
 * Caso de uso para establecer los videojuegos iniciales.
 */
class SetInitialVideoGamesUseCase(private val repository: VideoGameRepository) {
    operator fun invoke(list: List<VideoGameEntity>) = repository.setInitialVideoGames(list)
}
