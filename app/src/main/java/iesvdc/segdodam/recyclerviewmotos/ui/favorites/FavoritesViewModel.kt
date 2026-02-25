package iesvdc.segdodam.recyclerviewmotos.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import iesvdc.segdodam.recyclerviewmotos.domain.usecases.GetFavoriteVideoGamesUseCase
import iesvdc.segdodam.recyclerviewmotos.domain.usecases.RemoveFavoriteUseCase
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteVideoGamesUseCase: GetFavoriteVideoGamesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    val favorites: LiveData<List<VideoGame>> = getFavoriteVideoGamesUseCase()
        .map { list ->
            list.map { entity ->
                VideoGame(
                    id = entity.id,
                    nombre = entity.nombre,
                    precio = entity.precio,
                    plataforma = entity.plataforma,
                    caracteristicas = entity.caracteristicas,
                    puntuacion = entity.puntuacion,
                    visitas = entity.visitas,
                    isFavorite = true
                )
            }
        }.asLiveData()

    fun removeFromFavorites(videoGame: VideoGame) {
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
            removeFavoriteUseCase(entity)
        }
    }
}