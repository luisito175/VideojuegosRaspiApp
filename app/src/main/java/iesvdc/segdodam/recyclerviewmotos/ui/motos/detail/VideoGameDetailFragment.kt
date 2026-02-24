package iesvdc.segdodam.recyclerviewmotos.ui.motos.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.databinding.FragmentVideoGameDetailBinding
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame
import iesvdc.segdodam.recyclerviewmotos.ui.motos.VideoGamesViewModel

@AndroidEntryPoint
class VideoGameDetailFragment : Fragment() {

    private var _binding: FragmentVideoGameDetailBinding? = null
    private val binding get() = _binding!!
    private var currentPosition: Int = -1

    // Obtiene la MISMA instancia del ViewModel que el fragmento de la lista
    private val viewModel: VideoGamesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoGameDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recoge el argumento (la posición del videojuego)
        currentPosition = arguments?.getInt("video_game_position", -1) ?: -1

        if (currentPosition != -1) {
            // Pide el videojuego al ViewModel en lugar de tener su propia lista
            val videoGame = viewModel.getVideoGameAt(currentPosition)
            if (videoGame != null) {
                bind(videoGame)
            }
        }
    }

    private fun bind(videoGame: VideoGame) {
        binding.tvDetailNombre.text = videoGame.nombre
        binding.tvDetailPlataforma.text = videoGame.plataforma
        binding.tvDetailPrecio.text = String.format("%.2f €", videoGame.precio)
        binding.tvDetailCaracteristicas.text = videoGame.caracteristicas
        binding.ratingBar.rating = videoGame.puntuacion
        binding.tvDetailVisitas.text = "Visitas: ${videoGame.visitas}"

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            val updated = videoGame.copy(puntuacion = rating)
            if (currentPosition != -1) {
                viewModel.updateVideoGame(currentPosition, updated)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
