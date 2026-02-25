package iesvdc.segdodam.recyclerviewmotos.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import iesvdc.segdodam.recyclerviewmotos.data.local.AppDatabase
import iesvdc.segdodam.recyclerviewmotos.data.local.dao.FavoriteVideoGameDao
import iesvdc.segdodam.recyclerviewmotos.data.local.dao.UserProfileDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "videojuegos_db"
        )
        .fallbackToDestructiveMigration() // Facilitar actualizaciones de esquema en desarrollo
        .build()
    }

    @Provides
    fun provideFavoriteVideoGameDao(database: AppDatabase): FavoriteVideoGameDao {
        return database.favoriteVideoGameDao()
    }

    @Provides
    fun provideUserProfileDao(database: AppDatabase): UserProfileDao {
        return database.userProfileDao()
    }
}
