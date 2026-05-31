package iesvdc.segdodam.recyclerviewmotos.domain.repositories

import iesvdc.segdodam.recyclerviewmotos.domain.models.RateResponseEntity
import iesvdc.segdodam.recyclerviewmotos.domain.models.RecommendationCategory
import iesvdc.segdodam.recyclerviewmotos.domain.models.ReviewEntity
import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity

/**
 * Interfaz del repositorio en el dominio.
 * Define los contratos que la capa de datos debe cumplir.
 */
interface VideoGameRepository {
    suspend fun getAllVideoGames(): List<VideoGameEntity>
    suspend fun getRecommendations(category: RecommendationCategory, value: String? = null): List<VideoGameEntity>
    suspend fun addVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun updateVideoGame(pos: Int, videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun deleteVideoGame(pos: Int, videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun getVideoGameAt(pos: Int): VideoGameEntity?
    suspend fun setInitialVideoGames(list: List<VideoGameEntity>)
    suspend fun registerVisit(id: Int)
    suspend fun rateVideoGame(id: Int, rating: Int, comentario: String?): RateResponseEntity
    suspend fun getMyRating(id: Int): Int?
    suspend fun getReviews(id: Int): List<ReviewEntity>

    // Favorites
    fun getFavoriteVideoGames(): kotlinx.coroutines.flow.Flow<List<VideoGameEntity>>
    suspend fun addFavorite(videoGame: VideoGameEntity)
    suspend fun removeFavorite(videoGame: VideoGameEntity)
    suspend fun isFavorite(id: Int): Boolean
}
