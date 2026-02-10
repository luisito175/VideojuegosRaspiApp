package iesvdc.segdodam.recyclerviewmotos.ui.motos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import iesvdc.segdodam.recyclerviewmotos.domain.models.MotoEntity
import iesvdc.segdodam.recyclerviewmotos.domain.usecases.*
import iesvdc.segdodam.recyclerviewmotos.models.Moto
import javax.inject.Inject

@HiltViewModel
class MotosViewModel @Inject constructor(
    private val getAllMotosUseCase: GetAllMotosUseCase,
    private val addMotoUseCase: AddMotoUseCase,
    private val updateMotoUseCase: UpdateMotoUseCase,
    private val deleteMotoUseCase: DeleteMotoUseCase,
    private val getMotoAtUseCase: GetMotoAtUseCase
) : ViewModel() {

    private val _motos = MutableLiveData<MutableList<Moto>>()
    val motos: LiveData<MutableList<Moto>> = _motos

    init {
        loadMotos()
    }

    private fun loadMotos() {
        val motosFromUseCase = getAllMotosUseCase()
        val motosList = motosFromUseCase.map { entity ->
            Moto(
                marca = entity.marca,
                modelo = entity.modelo,
                caracteristicas = entity.caracteristicas,
                imagen = entity.imagen
            )
        }.toMutableList()
        _motos.value = motosList
    }

    fun addMoto(nuevaMoto: Moto) {
        val entity = MotoEntity(
            marca = nuevaMoto.marca,
            modelo = nuevaMoto.modelo,
            caracteristicas = nuevaMoto.caracteristicas,
            imagen = nuevaMoto.imagen
        )
        addMotoUseCase(entity)
        loadMotos()
    }

    fun deleteMoto(pos: Int) {
        deleteMotoUseCase(pos)
        loadMotos()
    }

    fun updateMoto(pos: Int, motoActualizada: Moto) {
        val entity = MotoEntity(
            marca = motoActualizada.marca,
            modelo = motoActualizada.modelo,
            caracteristicas = motoActualizada.caracteristicas,
            imagen = motoActualizada.imagen
        )
        updateMotoUseCase(pos, entity)
        loadMotos()
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
}
