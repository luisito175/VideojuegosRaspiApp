package iesvdc.segdodam.recyclerviewmotos.Dao

import iesvdc.segdodam.recyclerviewmotos.Interfaces.InterfaceDao
import iesvdc.segdodam.recyclerviewmotos.models.Moto
import iesvdc.segdodam.recyclerviewmotos.object_models.Repository

class DaoMoto private constructor(): InterfaceDao {
    companion object {
        val myDao: DaoMoto by lazy {
            DaoMoto()
        }
    }
    //Método que accede a la BBDD y devuelve todos los datos
    override
    fun getAllMotos(): List<Moto> = Repository.motos
}