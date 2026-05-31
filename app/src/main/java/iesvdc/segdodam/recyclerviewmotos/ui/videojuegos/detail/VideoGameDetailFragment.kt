package iesvdc.segdodam.recyclerviewmotos.ui.videojuegos.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.databinding.FragmentVideoGameDetailBinding
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame
import iesvdc.segdodam.recyclerviewmotos.ui.videojuegos.VideoGamesViewModel

@AndroidEntryPoint
class VideoGameDetailFragment : Fragment() {

    private var _binding: FragmentVideoGameDetailBinding? = null
    private val binding get() = _binding!!
    private var currentPosition: Int = -1
    private var currentGameIdArg: Int = -1
    private var currentGameId: Int = -1
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
        currentGameIdArg = arguments?.getInt("video_game_id", -1) ?: -1

        // Observamos cambios en la lista para actualizar la UI en tiempo real
        viewModel.videoGames.observe(viewLifecycleOwner) { games ->
            val selected = when {
                currentGameIdArg != -1 -> games.firstOrNull { it.id == currentGameIdArg }
                currentPosition != -1 -> games.getOrNull(currentPosition)
                else -> null
            }

            selected?.let { game ->
                bind(game)

                if (currentGameId != game.id) {
                    currentGameId = game.id
                    viewModel.loadGameFeedback(game.id)
                }

                // Solo incrementamos visitas la primera vez que entramos y tenemos el dato
                if (!visitsIncremented) {
                    visitsIncremented = true
                    viewModel.incrementVisitOptimistic(game.id)
                }
            }
        }

        viewModel.selectedGameMyRating.observe(viewLifecycleOwner) { myRating ->
            binding.ratingBar.setOnRatingBarChangeListener(null)
            binding.ratingBar.rating = (myRating ?: 0).toFloat()
            setupRatingListener()
        }

        viewModel.selectedGameReviews.observe(viewLifecycleOwner) { reviews ->
            if (reviews.isEmpty()) {
                binding.tvReviewsContent.text = "Todavía no hay reseñas para este juego."
            } else {
                binding.tvReviewsContent.text = reviews.joinToString("\n\n") { review ->
                    "${review.username} (${review.rating}/5): ${review.comentario}"
                }
            }
        }

        binding.btnSendReview.setOnClickListener {
            if (currentGameId == -1) return@setOnClickListener
            val rating = binding.ratingBar.rating.toInt()
            if (rating < 1) {
                Toast.makeText(requireContext(), "Selecciona una valoración de 1 a 5", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val comment = binding.etReviewComment.text?.toString().orEmpty()
            viewModel.submitRating(currentGameId, rating, comment)
            binding.etReviewComment.text?.clear()
            Toast.makeText(requireContext(), "Valoración enviada", Toast.LENGTH_SHORT).show()
        }

        setupRatingListener()
    }

    private fun bind(videoGame: VideoGame) {
        binding.tvDetailNombre.text = videoGame.nombre
        binding.tvDetailPlataforma.text = videoGame.plataforma
        binding.tvDetailPrecio.text = String.format("%.2f €", videoGame.precio)
        binding.tvDetailCaracteristicas.text = videoGame.caracteristicas
        binding.tvDetailPuntuacion.text = String.format("Media: %.1f/5 (%d votos)", videoGame.puntuacion, videoGame.totalVotos)
        binding.tvDetailVisitas.text = "Visitas: ${videoGame.visitas}"
    }

    private fun setupRatingListener() {
        binding.ratingBar.setOnRatingBarChangeListener { _, _, fromUser ->
            if (fromUser) {
                binding.btnSendReview.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
