package iesvdc.segdodam.recyclerviewmotos

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.HiltAndroidApp
import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import iesvdc.segdodam.recyclerviewmotos.domain.usecases.SetInitialVideoGamesUseCase
import javax.inject.Inject

/**
 * Application class que inicializa los datos de la app con Hilt.
 */
@HiltAndroidApp
class ConcesionarioApp : Application() {

    @Inject
    lateinit var setInitialVideoGamesUseCase: SetInitialVideoGamesUseCase

    override fun onCreate() {
        super.onCreate()
        initializeVideoGames()
    }

    private fun initializeVideoGames() {
        val videoGames = runCatching {
            val json = assets.open("videojuegos.json").bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<VideoGameEntity>>() {}.type
            Gson().fromJson<List<VideoGameEntity>>(json, listType).orEmpty()
        }.getOrDefault(emptyList())

        setInitialVideoGamesUseCase(videoGames)
    }
}
