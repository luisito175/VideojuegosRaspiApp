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
    private val getVideoGameAtUseCase: GetVideoGameAtUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val getFavoriteVideoGamesUseCase: GetFavoriteVideoGamesUseCase
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
                .onSuccess { videoGamesFromUseCase ->
                    val videoGamesList = videoGamesFromUseCase.map { entity ->
                        val isFav = isFavoriteUseCase(entity.id)
                        VideoGame(
                            id = entity.id,
                            nombre = entity.nombre,
                            precio = entity.precio,
                            plataforma = entity.plataforma,
                            caracteristicas = entity.caracteristicas,
                            puntuacion = entity.puntuacion,
                            visitas = entity.visitas,
                            isFavorite = isFav
                        )
                    }.toMutableList()
                    allVideoGames.clear()
                    allVideoGames.addAll(videoGamesList)
                    applyFilter(currentQuery)
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "No se pudo cargar la lista."
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
            loadVideoGames() // Refresh to update UI
        }
    }

    fun addVideoGame(nuevoVideoGame: VideoGame) {
        viewModelScope.launch {
            val nextId = (_videoGames.value?.maxOfOrNull { it.id } ?: 0) + 1
            val entity = VideoGameEntity(
                id = nextId,
                nombre = nuevoVideoGame.nombre,
                precio = nuevoVideoGame.precio,
                plataforma = nuevoVideoGame.plataforma,
                caracteristicas = nuevoVideoGame.caracteristicas,
                puntuacion = nuevoVideoGame.puntuacion,
                visitas = nuevoVideoGame.visitas
            )
            runCatching { addVideoGameUseCase(entity) }
                .onSuccess { updatedList ->
                    _videoGames.value = updatedList.map { mapped ->
                        VideoGame(
                            id = mapped.id,
                            nombre = mapped.nombre,
                            precio = mapped.precio,
                            plataforma = mapped.plataforma,
                            caracteristicas = mapped.caracteristicas,
                            puntuacion = mapped.puntuacion,
                            visitas = mapped.visitas
                        )
                    }.toMutableList()
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "No se pudo añadir el videojuego."
                }
        }
    }

    fun deleteVideoGame(pos: Int) {
        val current = _videoGames.value?.getOrNull(pos) ?: return
        viewModelScope.launch {
            val entity = VideoGameEntity(
                id = current.id,
                nombre = current.nombre,
                precio = current.precio,
                plataforma = current.plataforma,
                caracteristicas = current.caracteristicas,
                puntuacion = current.puntuacion,
                visitas = current.visitas
            )
            runCatching { deleteVideoGameUseCase(pos, entity) }
                .onSuccess { updatedList ->
                    _videoGames.value = updatedList.map { mapped ->
                        VideoGame(
                            id = mapped.id,
                            nombre = mapped.nombre,
                            precio = mapped.precio,
                            plataforma = mapped.plataforma,
                            caracteristicas = mapped.caracteristicas,
                            puntuacion = mapped.puntuacion,
                            visitas = mapped.visitas
                        )
                    }.toMutableList()
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "No se pudo eliminar el videojuego."
                }
        }
    }

    fun updateVideoGame(pos: Int, videoGameActualizado: VideoGame) {
        viewModelScope.launch {
            val entity = VideoGameEntity(
                id = videoGameActualizado.id,
                nombre = videoGameActualizado.nombre,
                precio = videoGameActualizado.precio,
                plataforma = videoGameActualizado.plataforma,
                caracteristicas = videoGameActualizado.caracteristicas,
                puntuacion = videoGameActualizado.puntuacion,
                visitas = videoGameActualizado.visitas
            )
            runCatching { updateVideoGameUseCase(pos, entity) }
                .onSuccess { updatedList ->
                    _videoGames.value = updatedList.map { mapped ->
                        VideoGame(
                            id = mapped.id,
                            nombre = mapped.nombre,
                            precio = mapped.precio,
                            plataforma = mapped.plataforma,
                            caracteristicas = mapped.caracteristicas,
                            puntuacion = mapped.puntuacion,
                            visitas = mapped.visitas
                        )
                    }.toMutableList()
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "No se pudo actualizar el videojuego."
                }
        }
    }

    fun getVideoGameAt(pos: Int): VideoGame? {
        return _videoGames.value?.getOrNull(pos)
    }

    fun setSearchQuery(query: String) {
        currentQuery = query
        applyFilter(query)
    }

    private fun applyFilter(query: String) {
        if (query.isBlank()) {
            _videoGames.value = allVideoGames.toMutableList()
            return
        }
        val filtered = allVideoGames.filter { it.nombre.contains(query, ignoreCase = true) }
        _videoGames.value = filtered.toMutableList()
    }
}
