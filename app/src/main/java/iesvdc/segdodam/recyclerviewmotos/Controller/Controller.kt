package iesvdc.segdodam.recyclerviewmotos.Controller

import iesvdc.segdodam.recyclerviewmotos.domain.models.MotoEntity
import iesvdc.segdodam.recyclerviewmotos.domain.usecases.*
import iesvdc.segdodam.recyclerviewmotos.models.Moto

/**
 * Controller clásico que usa los casos de uso de Clean Architecture.
 * NOTA: Este Controller es OPCIONAL con Hilt, ya que puedes inyectar los UseCases
 * directamente en ViewModels y Fragments. Se mantiene como alternativa.
 */
class Controller(
    private val getAllMotosUseCase: GetAllMotosUseCase,
    private val addMotoUseCase: AddMotoUseCase,
    private val updateMotoUseCase: UpdateMotoUseCase,
    private val deleteMotoUseCase: DeleteMotoUseCase,
    private val getMotoAtUseCase: GetMotoAtUseCase,
    private val setInitialMotosUseCase: SetInitialMotosUseCase
) {

    fun getAllMotos(): List<Moto> {
        return getAllMotosUseCase().map { entity ->
            Moto(
                marca = entity.marca,
                modelo = entity.modelo,
                caracteristicas = entity.caracteristicas,
                imagen = entity.imagen
            )
        }
    }

    fun addMoto(moto: Moto) {
        val entity = MotoEntity(
            marca = moto.marca,
            modelo = moto.modelo,
            caracteristicas = moto.caracteristicas,
            imagen = moto.imagen
        )
        addMotoUseCase(entity)
    }

    fun updateMoto(pos: Int, moto: Moto) {
        val entity = MotoEntity(
            marca = moto.marca,
            modelo = moto.modelo,
            caracteristicas = moto.caracteristicas,
            imagen = moto.imagen
        )
        updateMotoUseCase(pos, entity)
    }

    fun deleteMoto(pos: Int) {
        deleteMotoUseCase(pos)
    }

    fun getMotoAt(pos: Int): Moto? {
        val entity = getMotoAtUseCase(pos) ?: return null
        return Moto(
            marca = entity.marca,
            modelo = entity.modelo,
            caracteristicas = entity.caracteristicas,
            imagen = entity.imagen
        )
    }

    fun setInitialMotos(list: List<Moto>) {
        val entities = list.map { moto ->
            MotoEntity(
                marca = moto.marca,
                modelo = moto.modelo,
                caracteristicas = moto.caracteristicas,
                imagen = moto.imagen
            )
        }
        setInitialMotosUseCase(entities)
    }
}