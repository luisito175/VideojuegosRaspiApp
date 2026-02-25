package iesvdc.segdodam.recyclerviewmotos.ui.videojuegos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.Adapter.VideoGameAdapter
import iesvdc.segdodam.recyclerviewmotos.Dialogs.VideoGameDialogFragment
import iesvdc.segdodam.recyclerviewmotos.R
import iesvdc.segdodam.recyclerviewmotos.databinding.FragmentVideoGamesListBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

@AndroidEntryPoint
class VideoGamesListFragment : Fragment() {

    private var _binding: FragmentVideoGamesListBinding? = null
    private val binding get() = _binding!!

    // Obtiene una instancia compartida del ViewModel.
    private val viewModel: VideoGamesViewModel by activityViewModels()
    private lateinit var adapter: VideoGameAdapter
    private var refreshJob: Job? = null

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
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        // Refresca la lista cada vez que el fragmento entra en pantalla.
        viewModel.refresh()
        startAutoRefresh()
    }

    override fun onStop() {
        super.onStop()
        stopAutoRefresh()
    }

    private fun setupRecyclerView() {
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

    private fun observeViewModel() {
        // Observa el LiveData del ViewModel. Cada vez que la lista de videojuegos cambie,
        // este bloque se ejecutará y actualizará la interfaz.
        viewModel.videoGames.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }
    }

    private fun startAutoRefresh() {
        if (refreshJob?.isActive == true) return
        refreshJob = viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                viewModel.refresh()
                delay(5000)
            }
        }
    }

    private fun stopAutoRefresh() {
        refreshJob?.cancel()
        refreshJob = null
    }

    private fun showAddVideoGameDialog() {
        val dialog = VideoGameDialogFragment(null) { nuevoVideoGame ->
            viewModel.addVideoGame(nuevoVideoGame)
            viewModel.refresh()
        }
        dialog.show(parentFragmentManager, "ADD_VIDEO_GAME")
    }

    private fun showEditVideoGameDialog(pos: Int) {
        val videoGame = viewModel.getVideoGameAt(pos)
        if (videoGame != null) {
            val dialog = VideoGameDialogFragment(videoGame) { videoGameActualizado ->
                viewModel.updateVideoGame(pos, videoGameActualizado)
                viewModel.refresh()
            }
            dialog.show(parentFragmentManager, "EDIT_VIDEO_GAME")
        }
    }

    private fun deleteVideoGame(pos: Int) {
        val videoGame = viewModel.getVideoGameAt(pos)
        if (videoGame != null) {
            viewModel.deleteVideoGame(pos)
            viewModel.refresh()
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
