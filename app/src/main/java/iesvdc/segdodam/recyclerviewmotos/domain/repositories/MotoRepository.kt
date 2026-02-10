package iesvdc.segdodam.recyclerviewmotos.domain.repositories

import iesvdc.segdodam.recyclerviewmotos.domain.models.MotoEntity

/**
 * Interfaz del repositorio en el dominio.
 * Define los contratos que la capa de datos debe cumplir.
 */
interface MotoRepository {
    fun getAllMotos(): List<MotoEntity>
    fun addMoto(moto: MotoEntity)
    fun updateMoto(pos: Int, moto: MotoEntity)
    fun deleteMoto(pos: Int)
    fun getMotoAt(pos: Int): MotoEntity?
    fun setInitialMotos(list: List<MotoEntity>)
}
