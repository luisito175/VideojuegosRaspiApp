package iesvdc.segdodam.recyclerviewmotos.data.datasources

import iesvdc.segdodam.recyclerviewmotos.data.api.VideoGameApiService
import iesvdc.segdodam.recyclerviewmotos.data.api.VideoGameCreateRequest
import iesvdc.segdodam.recyclerviewmotos.data.api.VideoGameUpdateRequest
import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import retrofit2.HttpException

/**
 * Data source remoto que consume la API con Retrofit (JSON Server).
 */
interface VideoGameRemoteDataSource {
    suspend fun fetchVideoGames(): List<VideoGameEntity>
    suspend fun addVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun updateVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun deleteVideoGame(videoGame: VideoGameEntity): List<VideoGameEntity>
    suspend fun getVideoGameAt(pos: Int): VideoGameEntity?
}

class VideoGameRemoteDataSourceImpl(
    private val apiService: VideoGameApiService
) : VideoGameRemoteDataSource {

    private val cachedVideoGames = mutableListOf<VideoGameEntity>()

    override suspend fun fetchVideoGames(): List<VideoGameEntity> {
        val remote = fetchVideoGamesFromApi()
        cachedVideoGames.clear()
        cachedVideoGames.addAll(remote)
        return ArrayList(cachedVideoGames)
    }

    private suspend fun fetchVideoGamesFromApi(): List<VideoGameEntity> {
        val primary = apiService.getVideoGamesResponse()
        if (primary.isSuccessful) {
            return primary.body().orEmpty()
        }

        // If the server expects a trailing slash, retry once with it
        if (primary.code() == 404) {
            val fallback = apiService.getVideoGamesResponseWithSlash()
            if (fallback.isSuccessful) {
                return fallback.body().orEmpty()
            }
            throw HttpException(fallback)
        }

        throw HttpException(primary)
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
            nombre = videoGame.nombre,
            precio = videoGame.precio,
            plataforma = videoGame.plataforma,
            caracteristicas = videoGame.caracteristicas,
            puntuacion = videoGame.puntuacion,
            visitas = videoGame.visitas
        )
        val response = apiService.updateVideoGame(videoGame.id, request)
        if (!response.isSuccessful) {
            if (response.code() == 404 || response.code() == 405) {
                val patchResponse = apiService.patchVideoGame(videoGame.id, request)
                if (!patchResponse.isSuccessful) {
                    throw HttpException(patchResponse)
                }
            } else {
                throw HttpException(response)
            }
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
}
