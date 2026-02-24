package iesvdc.segdodam.recyclerviewmotos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import iesvdc.segdodam.recyclerviewmotos.data.api.VideoGameApiService
import iesvdc.segdodam.recyclerviewmotos.data.datasources.VideoGameRemoteDataSource
import iesvdc.segdodam.recyclerviewmotos.data.datasources.VideoGameRemoteDataSourceImpl
import iesvdc.segdodam.recyclerviewmotos.data.repositories.VideoGameRepositoryImpl
import iesvdc.segdodam.recyclerviewmotos.domain.repositories.VideoGameRepository
import iesvdc.segdodam.recyclerviewmotos.domain.usecases.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Módulo Hilt que configura todas las dependencias de la aplicación.
 */
@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            // Use the ngrok HTTPS root provided by the user.
            // Keep the trailing slash so endpoint paths are appended correctly.
            .baseUrl("https://untrigonometric-postmaximal-candice.ngrok-free.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideVideoGameApiService(retrofit: Retrofit): VideoGameApiService {
        return retrofit.create(VideoGameApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideVideoGameRemoteDataSource(
        apiService: VideoGameApiService
    ): VideoGameRemoteDataSource {
        return VideoGameRemoteDataSourceImpl(apiService)
    }

    @Singleton
    @Provides
    fun provideVideoGameRepository(
        dataSource: VideoGameRemoteDataSource
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
