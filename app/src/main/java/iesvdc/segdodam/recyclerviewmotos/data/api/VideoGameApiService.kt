package iesvdc.segdodam.recyclerviewmotos.data.api

import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface VideoGameApiService {
    @GET("/api/videogame")
    suspend fun getVideoGamesResponse(): Response<List<VideoGameEntity>>

    @GET("/api/videogame/{id}")
    suspend fun getVideoGameById(@Path("id") id: Int): Response<VideoGameEntity>

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
