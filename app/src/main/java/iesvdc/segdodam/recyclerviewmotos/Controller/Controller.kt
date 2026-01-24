package iesvdc.segdodam.recyclerviewmotos.Controller

import iesvdc.segdodam.recyclerviewmotos.models.Moto

object Controller {
	private val motos = mutableListOf<Moto>()

	fun getAllMotos(): List<Moto> = motos

	fun addMoto(moto: Moto) {
		motos.add(moto)
	}

	fun updateMoto(pos: Int, moto: Moto) {
		if (pos in motos.indices) {
			motos[pos] = moto
		}
	}

	fun deleteMoto(pos: Int) {
		if (pos in motos.indices) {
			motos.removeAt(pos)
		}
	}

	fun getMotoAt(pos: Int): Moto? = motos.getOrNull(pos)

	fun setInitialMotos(list: List<Moto>) {
		motos.clear()
		motos.addAll(list)
	}
}