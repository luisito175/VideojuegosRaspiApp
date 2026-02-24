package iesvdc.segdodam.recyclerviewmotos

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class que inicializa los datos de la app con Hilt.
 */
@HiltAndroidApp
class ConcesionarioApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
