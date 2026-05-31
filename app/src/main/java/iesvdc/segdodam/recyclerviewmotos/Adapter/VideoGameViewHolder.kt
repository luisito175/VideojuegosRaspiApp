package iesvdc.segdodam.recyclerviewmotos.Adapter

import androidx.recyclerview.widget.RecyclerView
import iesvdc.segdodam.recyclerviewmotos.R
import iesvdc.segdodam.recyclerviewmotos.databinding.ItemVideoGameBinding
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame
import java.text.NumberFormat
import java.util.Locale

class VideoGameViewHolder(
    private val binding: ItemVideoGameBinding,
    private val detailOnClick: (Int) -> Unit,
    private val favoriteOnClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun renderize(videoGame: VideoGame, position: Int) {
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }

        binding.txtNombre.text = videoGame.nombre
        binding.txtPlataforma.text = videoGame.plataforma
        binding.txtPrecio.text = binding.root.context.getString(
            R.string.price_value,
            numberFormat.format(videoGame.precio)
        )
        binding.txtCaracteristicas.text = videoGame.caracteristicas
        binding.txtPuntuacion.text = binding.root.context.getString(
            R.string.rating_with_votes,
            videoGame.puntuacion,
            videoGame.totalVotos
        )
        binding.txtVisitas.text = binding.root.context.getString(R.string.visits_value, videoGame.visitas)

        val icon = if (videoGame.isFavorite) {
            R.drawable.ic_favorite_filled
        } else {
            R.drawable.ic_favorite_outline
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
