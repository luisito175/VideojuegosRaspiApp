package iesvdc.segdodam.recyclerviewmotos.data.datasources

import iesvdc.segdodam.recyclerviewmotos.domain.models.MotoEntity

/**
 * Data source local que gestiona las motos en memoria.
 */
interface MotoLocalDataSource {
    fun getAllMotos(): List<MotoEntity>
    fun addMoto(moto: MotoEntity)
    fun updateMoto(pos: Int, moto: MotoEntity)
    fun deleteMoto(pos: Int)
    fun getMotoAt(pos: Int): MotoEntity?
    fun setInitialMotos(list: List<MotoEntity>)
}

/**
 * Implementación del data source local.
 */
class MotoLocalDataSourceImpl : MotoLocalDataSource {
    private val motos = mutableListOf<MotoEntity>()

    override fun getAllMotos(): List<MotoEntity> = motos

    override fun addMoto(moto: MotoEntity) {
        motos.add(moto)
    }

    override fun updateMoto(pos: Int, moto: MotoEntity) {
        if (pos in motos.indices) {
            motos[pos] = moto
        }
    }

    override fun deleteMoto(pos: Int) {
        if (pos in motos.indices) {
            motos.removeAt(pos)
        }
    }

    override fun getMotoAt(pos: Int): MotoEntity? = motos.getOrNull(pos)

    override fun setInitialMotos(list: List<MotoEntity>) {
        motos.clear()
        motos.addAll(list)
    }
}
