package iesvdc.segdodam.recyclerviewmotos.data.api

import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VideoGameApiService {
    // Primary endpoint (no trailing slash)
    @GET("videogame")
    suspend fun getVideoGamesResponse(): Response<List<VideoGameEntity>>

    // Fallback endpoint (some servers expect a trailing slash)
    @GET("videogame/")
    suspend fun getVideoGamesResponseWithSlash(): Response<List<VideoGameEntity>>

    @POST("videogame")
    suspend fun addVideoGame(@Body request: VideoGameCreateRequest): Response<Unit>

    @PUT("videogame/{id}")
    suspend fun updateVideoGame(
        @Path("id") id: Int,
        @Body request: VideoGameUpdateRequest
    ): Response<VideoGameEntity>

    @PATCH("videogame/{id}")
    suspend fun patchVideoGame(
        @Path("id") id: Int,
        @Body request: VideoGameUpdateRequest
    ): Response<VideoGameEntity>

    @DELETE("videogame/{id}")
    suspend fun deleteVideoGame(@Path("id") id: Int): Response<Unit>
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
    val nombre: String,
    val precio: Double,
    val plataforma: String,
    val caracteristicas: String,
    val puntuacion: Float,
    val visitas: Long
)
