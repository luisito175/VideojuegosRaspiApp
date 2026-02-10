package iesvdc.segdodam.recyclerviewmotos.data.repositories

import iesvdc.segdodam.recyclerviewmotos.data.datasources.MotoLocalDataSource
import iesvdc.segdodam.recyclerviewmotos.domain.models.MotoEntity
import iesvdc.segdodam.recyclerviewmotos.domain.repositories.MotoRepository

/**
 * Implementación del repositorio en la capa de datos.
 * Implementa la interfaz del dominio y delega al data source.
 */
class MotoRepositoryImpl(
    private val localDataSource: MotoLocalDataSource
) : MotoRepository {

    override fun getAllMotos(): List<MotoEntity> = localDataSource.getAllMotos()

    override fun addMoto(moto: MotoEntity) = localDataSource.addMoto(moto)

    override fun updateMoto(pos: Int, moto: MotoEntity) = localDataSource.updateMoto(pos, moto)

    override fun deleteMoto(pos: Int) = localDataSource.deleteMoto(pos)

    override fun getMotoAt(pos: Int): MotoEntity? = localDataSource.getMotoAt(pos)

    override fun setInitialMotos(list: List<MotoEntity>) = localDataSource.setInitialMotos(list)
}
