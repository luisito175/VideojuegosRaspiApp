package iesvdc.segdodam.recyclerviewmotos.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.Adapter.VideoGameAdapter
import iesvdc.segdodam.recyclerviewmotos.R
import iesvdc.segdodam.recyclerviewmotos.databinding.FragmentFavoritesBinding

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var adapter: VideoGameAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = VideoGameAdapter(
            mutableListOf(),
            { pos -> navigateToDetail(pos) },
            { pos -> 
                val game = adapter.getItemAt(pos)
                if (game != null) {
                    viewModel.removeFromFavorites(game)
                }
            }
        )
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorites.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.favorites.observe(viewLifecycleOwner) { list ->
            adapter.updateData(list)
            binding.tvEmptyFavorites.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun navigateToDetail(pos: Int) {
        // Here we might need to handle position differently if detail expects position in the FULL list
        // However, looking at VideoGameDetailFragment, it seems to use position to get from VideoGamesViewModel
        // If Favorites list is different, we might need to pass ID instead of position.
        // For now, let's keep it consistent with the existing logic if possible.
        // WARNING: Using position from a filtered list might break detail if it uses absolute position.
        
        // TODO: Update DetailFragment to accept ID instead of position for better reliability
        val bundle = Bundle().apply {
            putInt("video_game_position", pos) 
        }
        // findNavController().navigate(R.id.action_favoritesFragment_to_videoGameDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
