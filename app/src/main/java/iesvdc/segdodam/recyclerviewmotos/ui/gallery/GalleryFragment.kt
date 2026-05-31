package iesvdc.segdodam.recyclerviewmotos.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import iesvdc.segdodam.recyclerviewmotos.R
import iesvdc.segdodam.recyclerviewmotos.databinding.FragmentGalleryBinding
import iesvdc.segdodam.recyclerviewmotos.ui.videojuegos.VideoGamesViewModel
import iesvdc.segdodam.recyclerviewmotos.ui.ranking.RankingAdapter

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VideoGamesViewModel by activityViewModels()
    private lateinit var adapter: RankingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupRecommenderForm()
        observeViewModel()
        applyRecommendation()
    }

    private fun setupRecyclerView() {
        adapter = RankingAdapter(emptyList(), getString(R.string.puntuacion))
        binding.rvRanking.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRanking.adapter = adapter
    }

    private fun setupRecommenderForm() {
        setupDropdown(
            binding.actRecGenre,
            listOf("RPG", "Accion", "Aventura", "Shooter", "Estrategia", "Deportes", "Indie")
        )
        setupDropdown(binding.actRecPlatform, listOf("PC", "PS5", "Xbox", "Switch", "Steam Deck"))
        setupDropdown(
            binding.actRecMode,
            listOf(
                getString(R.string.rec_mode_solo),
                getString(R.string.rec_mode_multiplayer),
                getString(R.string.rec_mode_indifferent)
            )
        )
        setupDropdown(
            binding.actRecPriority,
            listOf(
                getString(R.string.rec_priority_story),
                getString(R.string.rec_priority_challenge),
                getString(R.string.rec_priority_relax),
                getString(R.string.rec_priority_graphics)
            )
        )

        binding.actRecGenre.setText("RPG", false)
        binding.actRecPlatform.setText("PC", false)
        binding.etRecBudget.setText("40")
        binding.actRecMode.setText(getString(R.string.rec_mode_indifferent), false)
        binding.actRecPriority.setText(getString(R.string.rec_priority_story), false)

        binding.btnGenerateRecommendations.setOnClickListener { applyRecommendation() }
    }

    private fun observeViewModel() {
        viewModel.recommendedVideoGames.observe(viewLifecycleOwner) { list ->
            binding.tvRecommendationsEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            adapter.updateData(list, getString(R.string.puntuacion))
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDropdown(view: com.google.android.material.textfield.MaterialAutoCompleteTextView, options: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, options)
        view.setAdapter(adapter)
    }

    private fun applyRecommendation() {
        val budgetText = binding.etRecBudget.text?.toString()?.trim().orEmpty()
        if (budgetText.isNotBlank() && budgetText.replace(",", ".").toDoubleOrNull() == null) {
            Toast.makeText(requireContext(), getString(R.string.rec_budget_invalid), Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.loadSmartRecommendations(
            genre = binding.actRecGenre.text?.toString().orEmpty(),
            platform = binding.actRecPlatform.text?.toString().orEmpty(),
            budgetText = budgetText,
            mode = binding.actRecMode.text?.toString().orEmpty(),
            priority = binding.actRecPriority.text?.toString().orEmpty()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
