package iesvdc.segdodam.recyclerviewmotos.Adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import iesvdc.segdodam.recyclerviewmotos.databinding.ObjetoMotoBinding
import iesvdc.segdodam.recyclerviewmotos.models.Moto

class ViewHMoto(
    private val binding: ObjetoMotoBinding,
    private val deleteOnClick: (Int) -> Unit,
    private val editOnClick: (Int) -> Unit,
    private val detailOnClick: (Int) -> Unit // Se añade el listener para el detalle
) : RecyclerView.ViewHolder(binding.root) {

    fun renderize(moto: Moto, position: Int) {
        binding.txtMarca.text = moto.marca
        binding.txtModelo.text = moto.modelo
        binding.txtCaracteristicas.text = moto.caracteristicas

        Glide.with(binding.root.context)
            .load(moto.imagen)
            .into(binding.imgFotoMoto)

        // Listeners para los botones
        binding.btnEdit.setOnClickListener { editOnClick(position) }
        binding.btnDelete.setOnClickListener { deleteOnClick(position) }

        // Listener para el clic en toda la tarjeta
        binding.root.setOnClickListener {
            detailOnClick(position)
        }
    }
}
