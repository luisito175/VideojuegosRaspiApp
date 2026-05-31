package iesvdc.segdodam.recyclerviewmotos.data.datasources

import iesvdc.segdodam.recyclerviewmotos.data.api.VideoGameApiService
import iesvdc.segdodam.recyclerviewmotos.data.api.VideoGameCreateRequest
import iesvdc.segdodam.recyclerviewmotos.data.api.RateGameRequest
import iesvdc.segdodam.recyclerviewmotos.data.api.VideoGameUpdateRequest
import iesvdc.segdodam.recyclerviewmotos.domain.models.RateResponseEntity
import iesvdc.segdodam.recyclerviewmotos.domain.models.RecommendationCategory
import iesvdc.segdodam.recyclerviewmotos.domain.models.ReviewEntity
import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import retrofit2.HttpException

/**
 * Data source remoto que consume la API con Retrofit.
 */
interface VideoGameRemoteDataSource {
    suspend fun fetchVideoGames(): List<VideoGameEntity>
    suspend fun fetchRecommendations(category: RecommendationCategory, value: String? = null): List<VideoGameEntity>
    suspend fun addVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun updateVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun deleteVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun getVideoGameAt(pos: Int): VideoGameEntity?
    suspend fun registerVisit(id: Int)
    suspend fun rateVideoGame(id: Int, rating: Int, comentario: String?): RateResponseEntity
    suspend fun getMyRating(id: Int): Int?
    suspend fun getReviews(id: Int): List<ReviewEntity>
}

class VideoGameRemoteDataSourceImpl(
    private val apiService: VideoGameApiService
) : VideoGameRemoteDataSource {

    private val cachedVideoGames = mutableListOf<VideoGameEntity>()

    override suspend fun fetchVideoGames(): List<VideoGameEntity> {
        val response = apiService.getVideoGamesResponse()
        if (response.isSuccessful) {
            val remote = response.body().orEmpty()
            cachedVideoGames.clear()
            cachedVideoGames.addAll(remote)
            return ArrayList(cachedVideoGames)
        }
        throw HttpException(response)
    }

    override suspend fun fetchRecommendations(
        category: RecommendationCategory,
        value: String?
    ): List<VideoGameEntity> {
        val remote = when (category) {
            RecommendationCategory.PLATFORM -> {
                val platform = value?.trim().orEmpty()
                if (platform.isBlank()) {
                    fetchVideoGames()
                } else {
                    val response = apiService.getVideoGamesByPlataforma(platform)
                    if (response.isSuccessful) {
                        response.body().orEmpty()
                    } else {
                        fetchVideoGames().filter {
                            it.plataforma.contains(platform, ignoreCase = true)
                        }
                    }
                }
            }
            else -> fetchVideoGames()
        }

        return when (category) {
            RecommendationCategory.TOP_RATED -> remote
                .sortedWith(compareByDescending<VideoGameEntity> { it.puntuacion }.thenByDescending { it.totalVotos })
            RecommendationCategory.MOST_POPULAR -> remote.sortedByDescending { it.visitas }
            RecommendationCategory.GENRE -> {
                val genre = value?.trim().orEmpty()
                remote.filter {
                    genre.isBlank() || (it.genero?.contains(genre, ignoreCase = true) == true)
                }.sortedWith(compareByDescending<VideoGameEntity> { it.puntuacion }.thenByDescending { it.visitas })
            }
            RecommendationCategory.BUDGET -> {
                val maxBudget = value?.toDoubleOrNull() ?: 30.0
                remote.filter { it.precio <= maxBudget }
                    .sortedWith(compareBy<VideoGameEntity> { it.precio }.thenByDescending { it.puntuacion })
            }
            RecommendationCategory.PLATFORM -> remote
                .sortedWith(compareByDescending<VideoGameEntity> { it.puntuacion }.thenByDescending { it.visitas })
        }
    }

    override suspend fun addVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity> {
        val request = VideoGameCreateRequest(
            id = videoGame.id,
            nombre = videoGame.nombre,
            precio = videoGame.precio,
            plataforma = videoGame.plataforma,
            caracteristicas = videoGame.caracteristicas,
            puntuacion = videoGame.puntuacion,
            visitas = videoGame.visitas
        )
        val response = apiService.addVideoGame(request)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return fetchVideoGames()
    }

    override suspend fun updateVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity> {
        val request = VideoGameUpdateRequest(
            id = videoGame.id,
            nombre = videoGame.nombre,
            precio = videoGame.precio,
            plataforma = videoGame.plataforma,
            caracteristicas = videoGame.caracteristicas,
            puntuacion = videoGame.puntuacion,
            visitas = videoGame.visitas
        )
        // Usamos PATCH como método principal de actualización
        val response = apiService.patchVideoGame(videoGame.id, request)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return fetchVideoGames()
    }

    override suspend fun deleteVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity> {
        val response = apiService.deleteVideoGame(videoGame.id)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return fetchVideoGames()
    }

    override suspend fun getVideoGameAt(pos: Int): VideoGameEntity? {
        if (cachedVideoGames.isEmpty()) {
            fetchVideoGames()
        }
        return cachedVideoGames.getOrNull(pos)
    }

    override suspend fun registerVisit(id: Int) {
        val response = apiService.incrementVisit(id)
        if (response.isSuccessful) return

        // Compatibilidad con despliegues que exponen esta ruta bajo /api.
        val fallback = apiService.incrementVisitApi(id)
        if (!fallback.isSuccessful) {
            throw HttpException(fallback)
        }
    }

    override suspend fun rateVideoGame(id: Int, rating: Int, comentario: String?): RateResponseEntity {
        val response = apiService.rateVideoGame(
            id = id,
            request = RateGameRequest(rating = rating, comentario = comentario)
        )
        if (!response.isSuccessful) throw HttpException(response)
        val body = response.body() ?: throw IllegalStateException("Respuesta de valoración vacía")
        return RateResponseEntity(
            newAverage = body.newAverage,
            myRating = body.myRating,
            totalVotes = body.totalVotes
        )
    }

    override suspend fun getMyRating(id: Int): Int? {
        val response = apiService.getMyRating(id)
        if (!response.isSuccessful) throw HttpException(response)
        return response.body()?.rating
    }

    override suspend fun getReviews(id: Int): List<ReviewEntity> {
        val response = apiService.getReviews(id)
        if (!response.isSuccessful) throw HttpException(response)
        return response.body().orEmpty().map {
            ReviewEntity(
                userId = it.userId,
                username = it.username,
                avatarUrl = it.avatarUrl,
                rating = it.rating,
                comentario = it.comentario,
                updatedAt = it.updatedAt
            )
        }
    }
}
