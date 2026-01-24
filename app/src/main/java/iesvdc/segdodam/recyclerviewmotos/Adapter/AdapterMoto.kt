package iesvdc.segdodam.recyclerviewmotos.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iesvdc.segdodam.recyclerviewmotos.databinding.ObjetoMotoBinding
import iesvdc.segdodam.recyclerviewmotos.models.Moto

class AdapterMoto(
    private var listMoto: MutableList<Moto>,
    private val deleteOnClick: (Int) -> Unit,
    private val editOnClick: (Int) -> Unit,
    private val detailOnClick: (Int) -> Unit // Nuevo listener para navegar a detalles
) : RecyclerView.Adapter<ViewHMoto>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHMoto {
        val binding = ObjetoMotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // Pasamos el nuevo listener al ViewHolder
        return ViewHMoto(binding, deleteOnClick, editOnClick, detailOnClick)
    }

    override fun onBindViewHolder(holder: ViewHMoto, position: Int) {
        holder.renderize(listMoto[position], position)
    }

    override fun getItemCount(): Int = listMoto.size

    /**
     * Actualiza la lista de datos del adaptador y notifica al RecyclerView.
     */
    fun updateData(newMotos: List<Moto>) {
        listMoto.clear()
        listMoto.addAll(newMotos)
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }
}
