package iesvdc.segdodam.recyclerviewmotos.ui.motos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import iesvdc.segdodam.recyclerviewmotos.domain.usecases.*
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame
import javax.inject.Inject

@HiltViewModel
class MotosViewModel @Inject constructor(
    private val getAllVideoGamesUseCase: GetAllVideoGamesUseCase,
    private val addVideoGameUseCase: AddVideoGameUseCase,
    private val updateVideoGameUseCase: UpdateVideoGameUseCase,
    private val deleteVideoGameUseCase: DeleteVideoGameUseCase,
    private val getVideoGameAtUseCase: GetVideoGameAtUseCase
) : ViewModel() {

    private val _motos = MutableLiveData<MutableList<VideoGame>>()
    val motos: LiveData<MutableList<VideoGame>> = _motos

    init {
        loadMotos()
    }

    private fun loadMotos() {
        val videoGamesFromUseCase = getAllVideoGamesUseCase()
        val videoGamesList = videoGamesFromUseCase.map { entity ->
            VideoGame(
                id = entity.id,
                nombre = entity.nombre,
                precio = entity.precio,
                plataforma = entity.plataforma,
                caracteristicas = entity.caracteristicas
            )
        }.toMutableList()
        _motos.value = videoGamesList
    }

    fun addMoto(nuevoVideoGame: VideoGame) {
        val nextId = (_motos.value?.maxOfOrNull { it.id } ?: 0) + 1
        val entity = VideoGameEntity(
            id = nextId,
            nombre = nuevoVideoGame.nombre,
            precio = nuevoVideoGame.precio,
            plataforma = nuevoVideoGame.plataforma,
            caracteristicas = nuevoVideoGame.caracteristicas
        )
        addVideoGameUseCase(entity)
        loadMotos()
    }

    fun deleteMoto(pos: Int) {
        deleteVideoGameUseCase(pos)
        loadMotos()
    }

    fun updateMoto(pos: Int, videoGameActualizado: VideoGame) {
        val entity = VideoGameEntity(
            id = videoGameActualizado.id,
            nombre = videoGameActualizado.nombre,
            precio = videoGameActualizado.precio,
            plataforma = videoGameActualizado.plataforma,
            caracteristicas = videoGameActualizado.caracteristicas
        )
        updateVideoGameUseCase(pos, entity)
        loadMotos()
    }

    fun getMotoAt(pos: Int): VideoGame? {
        val entity = getVideoGameAtUseCase(pos) ?: return null
        return VideoGame(
            id = entity.id,
            nombre = entity.nombre,
            precio = entity.precio,
            plataforma = entity.plataforma,
            caracteristicas = entity.caracteristicas
        )
    }
}
