package iesvdc.segdodam.recyclerviewmotos.domain.usecases

import iesvdc.segdodam.recyclerviewmotos.domain.models.MotoEntity
import iesvdc.segdodam.recyclerviewmotos.domain.repositories.MotoRepository

/**
 * Caso de uso para obtener todas las motos.
 */
class GetAllMotosUseCase(private val repository: MotoRepository) {
    operator fun invoke(): List<MotoEntity> = repository.getAllMotos()
}

/**
 * Caso de uso para añadir una moto.
 */
class AddMotoUseCase(private val repository: MotoRepository) {
    operator fun invoke(moto: MotoEntity) = repository.addMoto(moto)
}

/**
 * Caso de uso para actualizar una moto.
 */
class UpdateMotoUseCase(private val repository: MotoRepository) {
    operator fun invoke(pos: Int, moto: MotoEntity) = repository.updateMoto(pos, moto)
}

/**
 * Caso de uso para eliminar una moto.
 */
class DeleteMotoUseCase(private val repository: MotoRepository) {
    operator fun invoke(pos: Int) = repository.deleteMoto(pos)
}

/**
 * Caso de uso para obtener una moto en una posición.
 */
class GetMotoAtUseCase(private val repository: MotoRepository) {
    operator fun invoke(pos: Int): MotoEntity? = repository.getMotoAt(pos)
}

/**
 * Caso de uso para establecer las motos iniciales.
 */
class SetInitialMotosUseCase(private val repository: MotoRepository) {
    operator fun invoke(list: List<MotoEntity>) = repository.setInitialMotos(list)
}
