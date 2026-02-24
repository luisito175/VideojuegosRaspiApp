package iesvdc.segdodam.recyclerviewmotos.Adapter

import androidx.recyclerview.widget.RecyclerView
import iesvdc.segdodam.recyclerviewmotos.databinding.ItemVideoGameBinding
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame

class VideoGameViewHolder(
    private val binding: ItemVideoGameBinding,
    private val deleteOnClick: (Int) -> Unit,
    private val editOnClick: (Int) -> Unit,
    private val detailOnClick: (Int) -> Unit // Se añade el listener para el detalle
) : RecyclerView.ViewHolder(binding.root) {

    fun renderize(videoGame: VideoGame, position: Int) {
        binding.txtNombre.text = videoGame.nombre
        binding.txtPlataforma.text = videoGame.plataforma
        binding.txtPrecio.text = String.format("%.2f €", videoGame.precio)
        binding.txtCaracteristicas.text = videoGame.caracteristicas
        binding.txtPuntuacion.text = "Puntuación: ${videoGame.puntuacion.toInt()}/5"
        binding.txtVisitas.text = "Visitas: ${videoGame.visitas}"

        // Listeners para los botones
        binding.btnEdit.setOnClickListener { editOnClick(position) }
        binding.btnDelete.setOnClickListener { deleteOnClick(position) }

        // Listener para el clic en toda la tarjeta
        binding.root.setOnClickListener {
            detailOnClick(position)
        }
    }
}
