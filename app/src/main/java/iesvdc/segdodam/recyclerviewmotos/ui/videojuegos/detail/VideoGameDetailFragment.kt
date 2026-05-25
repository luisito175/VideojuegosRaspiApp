package iesvdc.segdodam.recyclerviewmotos.ui.videojuegos.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.databinding.FragmentVideoGameDetailBinding
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame
import iesvdc.segdodam.recyclerviewmotos.ui.videojuegos.VideoGamesViewModel

@AndroidEntryPoint
class VideoGameDetailFragment : Fragment() {

    private var _binding: FragmentVideoGameDetailBinding? = null
    private val binding get() = _binding!!
    private var currentPosition: Int = -1
    private var visitsIncremented = false

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

        currentPosition = arguments?.getInt("video_game_position", -1) ?: -1

        if (currentPosition != -1) {
            // Observamos cambios en la lista para actualizar la UI en tiempo real
            viewModel.videoGames.observe(viewLifecycleOwner) { games ->
                games.getOrNull(currentPosition)?.let { game ->
                    bind(game)
                    
                    // Solo incrementamos visitas la primera vez que entramos y tenemos el dato
                    if (!visitsIncremented) {
                        visitsIncremented = true
                        viewModel.incrementVisitOptimistic(game.id)
                    }
                }
            }
        }
    }

    private fun bind(videoGame: VideoGame) {
        binding.tvDetailNombre.text = videoGame.nombre
        binding.tvDetailPlataforma.text = videoGame.plataforma
        binding.tvDetailPrecio.text = String.format("%.2f €", videoGame.precio)
        binding.tvDetailCaracteristicas.text = videoGame.caracteristicas
        binding.tvDetailVisitas.text = "Visitas: ${videoGame.visitas}"
        
        // Evitamos bucles infinitos al setear el rating
        binding.ratingBar.setOnRatingBarChangeListener(null)
        binding.ratingBar.rating = videoGame.puntuacion
        
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                val updated = videoGame.copy(puntuacion = rating)
                viewModel.updateVideoGame(currentPosition, updated)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
