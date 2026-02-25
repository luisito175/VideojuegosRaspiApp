package iesvdc.segdodam.recyclerviewmotos.Adapter

import androidx.recyclerview.widget.RecyclerView
import iesvdc.segdodam.recyclerviewmotos.databinding.ItemVideoGameBinding
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame

class VideoGameViewHolder(
    private val binding: ItemVideoGameBinding,
    private val detailOnClick: (Int) -> Unit,
    private val favoriteOnClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun renderize(videoGame: VideoGame, position: Int) {
        binding.txtNombre.text = videoGame.nombre
        binding.txtPlataforma.text = videoGame.plataforma
        binding.txtPrecio.text = String.format("%.2f €", videoGame.precio)
        binding.txtCaracteristicas.text = videoGame.caracteristicas
        binding.txtPuntuacion.text = "Puntuación: ${videoGame.puntuacion.toInt()}/5"
        binding.txtVisitas.text = "Visitas: ${videoGame.visitas}"

        val icon = if (videoGame.isFavorite) {
            android.R.drawable.btn_star_big_on
        } else {
            android.R.drawable.btn_star_big_off
        }
        binding.btnFavorite.setImageResource(icon)

        // Listener para el clic en toda la tarjeta
        binding.root.setOnClickListener {
            detailOnClick(position)
        }

        binding.btnFavorite.setOnClickListener {
            favoriteOnClick(position)
        }
    }
}
