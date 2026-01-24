package iesvdc.segdodam.recyclerviewmotos.ui.motos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import iesvdc.segdodam.recyclerviewmotos.Dao.DaoMoto
import iesvdc.segdodam.recyclerviewmotos.models.Moto

class MotosViewModel : ViewModel() {

    private val _motos = MutableLiveData<MutableList<Moto>>()
    val motos: LiveData<MutableList<Moto>> = _motos

    init {
        _motos.value = DaoMoto.myDao.getAllMotos().toMutableList()
    }

    fun addMoto(nuevaMoto: Moto) {
        val currentList = _motos.value ?: mutableListOf()
        currentList.add(nuevaMoto)
        _motos.value = currentList
    }

    fun deleteMoto(pos: Int) {
        val currentList = _motos.value ?: return
        if (pos >= 0 && pos < currentList.size) {
            currentList.removeAt(pos)
            _motos.value = currentList
        }
    }

    fun updateMoto(pos: Int, motoActualizada: Moto) {
        val currentList = _motos.value ?: return
        if (pos >= 0 && pos < currentList.size) {
            currentList[pos] = motoActualizada
            _motos.value = currentList
        }
    }

    fun getMotoAt(pos: Int): Moto? {
        return _motos.value?.getOrNull(pos)
    }
}
