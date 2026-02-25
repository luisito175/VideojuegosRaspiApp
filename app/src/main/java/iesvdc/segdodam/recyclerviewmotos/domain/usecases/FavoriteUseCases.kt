package iesvdc.segdodam.recyclerviewmotos.domain.usecases

import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import iesvdc.segdodam.recyclerviewmotos.domain.repositories.VideoGameRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obtener todos los videojuegos favoritos.
 */
class GetFavoriteVideoGamesUseCase(private val repository: VideoGameRepository) {
    operator fun invoke(): Flow<List<VideoGameEntity>> = repository.getFavoriteVideoGames()
}

/**
 * Caso de uso para alternar el estado de favorito de un videojuego.
 */
class ToggleFavoriteUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(videoGame: VideoGameEntity) {
        if (repository.isFavorite(videoGame.id)) {
            repository.removeFavorite(videoGame)
        } else {
            repository.addFavorite(videoGame)
        }
    }
}

/**
 * Caso de uso para comprobar si un videojuego es favorito.
 */
class IsFavoriteUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(id: Int): Boolean = repository.isFavorite(id)
}

/**
 * Caso de uso para eliminar un videojuego de favoritos.
 */
class RemoveFavoriteUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(videoGame: VideoGameEntity) = repository.removeFavorite(videoGame)
}
