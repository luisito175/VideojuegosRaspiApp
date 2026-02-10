package iesvdc.segdodam.recyclerviewmotos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import iesvdc.segdodam.recyclerviewmotos.data.datasources.MotoLocalDataSource
import iesvdc.segdodam.recyclerviewmotos.data.datasources.MotoLocalDataSourceImpl
import iesvdc.segdodam.recyclerviewmotos.data.repositories.MotoRepositoryImpl
import iesvdc.segdodam.recyclerviewmotos.domain.repositories.MotoRepository
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
    fun provideMotoLocalDataSource(): MotoLocalDataSource {
        return MotoLocalDataSourceImpl()
    }

    @Singleton
    @Provides
    fun provideMotoRepository(
        dataSource: MotoLocalDataSource
    ): MotoRepository {
        return MotoRepositoryImpl(dataSource)
    }

    @Singleton
    @Provides
    fun provideGetAllMotosUseCase(
        repository: MotoRepository
    ): GetAllMotosUseCase {
        return GetAllMotosUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideAddMotoUseCase(
        repository: MotoRepository
    ): AddMotoUseCase {
        return AddMotoUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUpdateMotoUseCase(
        repository: MotoRepository
    ): UpdateMotoUseCase {
        return UpdateMotoUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteMotoUseCase(
        repository: MotoRepository
    ): DeleteMotoUseCase {
        return DeleteMotoUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetMotoAtUseCase(
        repository: MotoRepository
    ): GetMotoAtUseCase {
        return GetMotoAtUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSetInitialMotosUseCase(
        repository: MotoRepository
    ): SetInitialMotosUseCase {
        return SetInitialMotosUseCase(repository)
    }
}
