package iesvdc.segdodam.recyclerviewmotos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import iesvdc.segdodam.recyclerviewmotos.data.datasources.VideoGameLocalDataSource
import iesvdc.segdodam.recyclerviewmotos.data.datasources.VideoGameLocalDataSourceImpl
import iesvdc.segdodam.recyclerviewmotos.data.repositories.VideoGameRepositoryImpl
import iesvdc.segdodam.recyclerviewmotos.domain.repositories.VideoGameRepository
import iesvdc.segdodam.recyclerviewmotos.domain.usecases.*
import javax.inject.Singleton

/**
 * Módulo Hilt que configura todas las dependencias de la aplicación.
 */
@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Singleton
    @Provides
    fun provideVideoGameLocalDataSource(): VideoGameLocalDataSource {
        return VideoGameLocalDataSourceImpl()
    }

    @Singleton
    @Provides
    fun provideVideoGameRepository(
        dataSource: VideoGameLocalDataSource
    ): VideoGameRepository {
        return VideoGameRepositoryImpl(dataSource)
    }

    @Singleton
    @Provides
    fun provideGetAllVideoGamesUseCase(
        repository: VideoGameRepository
    ): GetAllVideoGamesUseCase {
        return GetAllVideoGamesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideAddVideoGameUseCase(
        repository: VideoGameRepository
    ): AddVideoGameUseCase {
        return AddVideoGameUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUpdateVideoGameUseCase(
        repository: VideoGameRepository
    ): UpdateVideoGameUseCase {
        return UpdateVideoGameUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteVideoGameUseCase(
        repository: VideoGameRepository
    ): DeleteVideoGameUseCase {
        return DeleteVideoGameUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetVideoGameAtUseCase(
        repository: VideoGameRepository
    ): GetVideoGameAtUseCase {
        return GetVideoGameAtUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSetInitialVideoGamesUseCase(
        repository: VideoGameRepository
    ): SetInitialVideoGamesUseCase {
        return SetInitialVideoGamesUseCase(repository)
    }
}
