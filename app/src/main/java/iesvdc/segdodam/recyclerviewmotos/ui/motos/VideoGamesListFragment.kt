package iesvdc.segdodam.recyclerviewmotos.ui.motos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.Adapter.VideoGameAdapter
import iesvdc.segdodam.recyclerviewmotos.Dialogs.VideoGameDialogFragment
import iesvdc.segdodam.recyclerviewmotos.R
import iesvdc.segdodam.recyclerviewmotos.databinding.FragmentVideoGamesListBinding

@AndroidEntryPoint
class VideoGamesListFragment : Fragment() {

    private var _binding: FragmentVideoGamesListBinding? = null
    private val binding get() = _binding!!

    // Obtiene una instancia compartida del ViewModel.
    private val viewModel: VideoGamesViewModel by activityViewModels()
    private lateinit var adapter: VideoGameAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoGamesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFab()
        setupRefreshButton()
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        // Refresca la lista cada vez que el fragmento entra en pantalla.
        viewModel.refresh()
    }

    private fun setupRecyclerView() {
        // El adaptador ahora recibe una función para el clic en el item
        adapter = VideoGameAdapter(
            mutableListOf(),
            ::deleteVideoGame,
            ::showEditVideoGameDialog,
            ::navigateToDetail
        )
        binding.myRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.myRecyclerView.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAddVideoGame.setOnClickListener {
            showAddVideoGameDialog()
        }
    }

    private fun setupRefreshButton() {
        binding.btnRefresh.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun observeViewModel() {
        // Observa el LiveData del ViewModel. Cada vez que la lista de videojuegos cambie,
        // este bloque se ejecutará y actualizará la interfaz.
        viewModel.videoGames.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddVideoGameDialog() {
        val dialog = VideoGameDialogFragment(null) { nuevoVideoGame ->
            viewModel.addVideoGame(nuevoVideoGame)
            Toast.makeText(context, "Videojuego añadido correctamente", Toast.LENGTH_SHORT).show()
        }
        dialog.show(parentFragmentManager, "ADD_VIDEO_GAME")
    }

    private fun showEditVideoGameDialog(pos: Int) {
        val videoGame = viewModel.getVideoGameAt(pos)
        if (videoGame != null) {
            val dialog = VideoGameDialogFragment(videoGame) { videoGameActualizado ->
                viewModel.updateVideoGame(pos, videoGameActualizado)
                Toast.makeText(context, "Videojuego actualizado", Toast.LENGTH_SHORT).show()
            }
            dialog.show(parentFragmentManager, "EDIT_VIDEO_GAME")
        }
    }

    private fun deleteVideoGame(pos: Int) {
        val videoGame = viewModel.getVideoGameAt(pos)
        if (videoGame != null) {
            viewModel.deleteVideoGame(pos)
            Toast.makeText(context, "Videojuego '${videoGame.nombre}' eliminado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToDetail(pos: Int) {
        // Crea un Bundle para pasar la posición del videojuego
        val bundle = Bundle().apply {
            putInt("video_game_position", pos)
        }
        // Navega al fragmento de detalles usando la acción definida en nav_graph.xml
        findNavController().navigate(R.id.action_videoGamesListFragment_to_videoGameDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
