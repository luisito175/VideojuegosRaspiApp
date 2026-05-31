package iesvdc.segdodam.recyclerviewmotos.data.api

import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoGameApiService {
    @GET("/api/videogame")
    suspend fun getVideoGamesResponse(): Response<List<VideoGameEntity>>

    @GET("/api/videogame/{id}")
    suspend fun getVideoGameById(@Path("id") id: Int): Response<VideoGameEntity>

    @GET("/api/videogame")
    suspend fun getVideoGamesByPlataforma(
        @Query("plataforma") plataforma: String
    ): Response<List<VideoGameEntity>>

    @POST("/api/videogame")
    suspend fun addVideoGame(@Body request: VideoGameCreateRequest): Response<Unit>

    @PATCH("/api/videogame/{id}")
    suspend fun patchVideoGame(
        @Path("id") id: Int,
        @Body request: VideoGameUpdateRequest
    ): Response<VideoGameEntity>

    @DELETE("/api/videogame/{id}")
    suspend fun deleteVideoGame(@Path("id") id: Int): Response<Unit>

    @POST("/videogame/{id}/visit")
    suspend fun incrementVisit(@Path("id") id: Int): Response<Unit>

    @POST("/api/videogame/{id}/visit")
    suspend fun incrementVisitApi(@Path("id") id: Int): Response<Unit>

    @POST("/api/videogame/{id}/rate")
    suspend fun rateVideoGame(
        @Path("id") id: Int,
        @Body request: RateGameRequest
    ): Response<RateResponseDto>

    @GET("/api/videogame/{id}/my-rating")
    suspend fun getMyRating(@Path("id") id: Int): Response<MyRatingResponse>

    @GET("/api/videogame/{id}/reviews")
    suspend fun getReviews(@Path("id") id: Int): Response<List<ReviewDto>>
}

data class VideoGameCreateRequest(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val plataforma: String,
    val caracteristicas: String,
    val puntuacion: Float,
    val visitas: Long
)

data class VideoGameUpdateRequest(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val plataforma: String,
    val caracteristicas: String,
    val puntuacion: Float,
    val visitas: Long
)

data class RateGameRequest(
    val rating: Int,
    val comentario: String?
)

data class RateResponseDto(
    val newAverage: Float,
    val myRating: Int,
    val totalVotes: Int
)

data class MyRatingResponse(
    val rating: Int?
)

data class ReviewDto(
    val userId: Int,
    val username: String,
    val avatarUrl: String?,
    val rating: Int,
    val comentario: String,
    val updatedAt: String
)

