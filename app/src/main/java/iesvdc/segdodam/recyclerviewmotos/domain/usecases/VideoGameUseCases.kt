package iesvdc.segdodam.recyclerviewmotos.domain.usecases

import iesvdc.segdodam.recyclerviewmotos.domain.models.RateResponseEntity
import iesvdc.segdodam.recyclerviewmotos.domain.models.RecommendationCategory
import iesvdc.segdodam.recyclerviewmotos.domain.models.ReviewEntity
import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import iesvdc.segdodam.recyclerviewmotos.domain.repositories.VideoGameRepository

/**
 * Caso de uso para obtener todos los videojuegos.
 */
class GetAllVideoGamesUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(): List<VideoGameEntity> = repository.getAllVideoGames()
}

class GetRecommendationsUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(
        category: RecommendationCategory,
        value: String? = null
    ): List<VideoGameEntity> = repository.getRecommendations(category, value)
}

/**
 * Caso de uso para añadir un videojuego.
 */
class AddVideoGameUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(videoGame: VideoGameEntity): List<VideoGameEntity> =
        repository.addVideoGame(videoGame)
}

/**
 * Caso de uso para actualizar un videojuego.
 */
class UpdateVideoGameUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(pos: Int, videoGame: VideoGameEntity): List<VideoGameEntity> =
        repository.updateVideoGame(pos, videoGame)
}

/**
 * Caso de uso para eliminar un videojuego.
 */
class DeleteVideoGameUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(pos: Int, videoGame: VideoGameEntity): List<VideoGameEntity> =
        repository.deleteVideoGame(pos, videoGame)
}

/**
 * Caso de uso para obtener un videojuego en una posición.
 */
class GetVideoGameAtUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(pos: Int): VideoGameEntity? = repository.getVideoGameAt(pos)
}

/**
 * Caso de uso para establecer los videojuegos iniciales.
 */
class SetInitialVideoGamesUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(list: List<VideoGameEntity>) = repository.setInitialVideoGames(list)
}

/**
 * Caso de uso para registrar una visita al abrir el detalle.
 */
class RegisterVisitUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(id: Int) = repository.registerVisit(id)
}

class RateVideoGameUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(id: Int, rating: Int, comentario: String?): RateResponseEntity =
        repository.rateVideoGame(id, rating, comentario)
}

class GetMyRatingUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(id: Int): Int? = repository.getMyRating(id)
}

class GetReviewsUseCase(private val repository: VideoGameRepository) {
    suspend operator fun invoke(id: Int): List<ReviewEntity> = repository.getReviews(id)
}

