package iesvdc.segdodam.recyclerviewmotos.data.datasources

import iesvdc.segdodam.recyclerviewmotos.data.api.VideoGameApiService
import iesvdc.segdodam.recyclerviewmotos.data.api.VideoGameCreateRequest
import iesvdc.segdodam.recyclerviewmotos.data.api.VideoGameUpdateRequest
import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import retrofit2.HttpException

/**
 * Data source remoto que consume la API con Retrofit.
 */
interface VideoGameRemoteDataSource {
    suspend fun fetchVideoGames(): List<VideoGameEntity>
    suspend fun addVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun updateVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun deleteVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun getVideoGameAt(pos: Int): VideoGameEntity?
    suspend fun registerVisit(id: Int)
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
}
