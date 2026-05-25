package iesvdc.segdodam.recyclerviewmotos.ui.videojuegos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import iesvdc.segdodam.recyclerviewmotos.domain.usecases.*
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoGamesViewModel @Inject constructor(
    private val getAllVideoGamesUseCase: GetAllVideoGamesUseCase,
    private val addVideoGameUseCase: AddVideoGameUseCase,
    private val updateVideoGameUseCase: UpdateVideoGameUseCase,
    private val deleteVideoGameUseCase: DeleteVideoGameUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _videoGames = MutableLiveData<MutableList<VideoGame>>()
    val videoGames: LiveData<MutableList<VideoGame>> = _videoGames

    private val allVideoGames = mutableListOf<VideoGame>()
    private var currentQuery: String = ""

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadVideoGames()
    }

    fun refresh() {
        loadVideoGames()
    }

    private fun loadVideoGames() {
        viewModelScope.launch {
            runCatching { getAllVideoGamesUseCase() }
                .onSuccess { entities ->
                    updateListWithFavorites(entities)
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error al cargar datos"
                }
        }
    }

    private suspend fun updateListWithFavorites(entities: List<VideoGameEntity>) {
        val videoGamesList = entities.map { entity ->
            VideoGame(
                id = entity.id,
                nombre = entity.nombre,
                precio = entity.precio,
                plataforma = entity.plataforma,
                caracteristicas = entity.caracteristicas,
                puntuacion = entity.puntuacion,
                visitas = entity.visitas,
                isFavorite = isFavoriteUseCase(entity.id)
            )
        }.toMutableList()
        allVideoGames.clear()
        allVideoGames.addAll(videoGamesList)
        applyFilter(currentQuery)
    }

    fun updateVideoGame(pos: Int, videoGame: VideoGame) {
        viewModelScope.launch {
            val entity = VideoGameEntity(
                id = videoGame.id,
                nombre = videoGame.nombre,
                precio = videoGame.precio,
                plataforma = videoGame.plataforma,
                caracteristicas = videoGame.caracteristicas,
                puntuacion = videoGame.puntuacion,
                visitas = videoGame.visitas
            )
            runCatching { updateVideoGameUseCase(pos, entity) }
                .onSuccess { entities ->
                    updateListWithFavorites(entities)
                }
                .onFailure { error ->
                    _errorMessage.value = "Error al actualizar: ${error.message}"
                }
        }
    }

    fun toggleFavorite(pos: Int) {
        val game = _videoGames.value?.getOrNull(pos) ?: return
        viewModelScope.launch {
            val entity = VideoGameEntity(
                id = game.id,
                nombre = game.nombre,
                precio = game.precio,
                plataforma = game.plataforma,
                caracteristicas = game.caracteristicas,
                puntuacion = game.puntuacion,
                visitas = game.visitas
            )
            toggleFavoriteUseCase(entity)
            loadVideoGames()
        }
    }

    fun addVideoGame(videoGame: VideoGame) {
        viewModelScope.launch {
            val entity = VideoGameEntity(
                id = videoGame.id,
                nombre = videoGame.nombre,
                precio = videoGame.precio,
                plataforma = videoGame.plataforma,
                caracteristicas = videoGame.caracteristicas,
                puntuacion = videoGame.puntuacion,
                visitas = videoGame.visitas
            )
            runCatching { addVideoGameUseCase(entity) }
                .onSuccess { entities ->
                    updateListWithFavorites(entities)
                }
                .onFailure { error ->
                    _errorMessage.value = parseBackendError(error)
                }
        }
    }

    fun deleteVideoGame(pos: Int, videoGame: VideoGame) {
        viewModelScope.launch {
            val entity = VideoGameEntity(
                id = videoGame.id,
                nombre = videoGame.nombre,
                precio = videoGame.precio,
                plataforma = videoGame.plataforma,
                caracteristicas = videoGame.caracteristicas,
                puntuacion = videoGame.puntuacion,
                visitas = videoGame.visitas
            )
            runCatching { deleteVideoGameUseCase(pos, entity) }
                .onSuccess { entities ->
                    updateListWithFavorites(entities)
                }
                .onFailure { error ->
                    _errorMessage.value = parseBackendError(error)
                }
        }
    }

    private fun parseBackendError(error: Throwable): String {
        val msg = error.message ?: "Error desconocido"
        return when {
            msg.contains("403") && msg.contains("crear videojuegos") -> "No tienes permisos para crear videojuegos"
            msg.contains("403") && msg.contains("editar videojuegos") -> "No tienes permisos para editar videojuegos"
            msg.contains("403") && msg.contains("eliminar videojuegos") -> "No tienes permisos para eliminar videojuegos"
            else -> msg
        }
    }

    fun getVideoGameAt(pos: Int): VideoGame? = _videoGames.value?.getOrNull(pos)

    fun setSearchQuery(query: String) {
        currentQuery = query
        applyFilter(query)
    }

    private fun applyFilter(query: String) {
        val filtered = if (query.isBlank()) {
            allVideoGames
        } else {
            allVideoGames.filter { it.nombre.contains(query, ignoreCase = true) }
        }
        _videoGames.value = filtered.toMutableList()
    }
}
