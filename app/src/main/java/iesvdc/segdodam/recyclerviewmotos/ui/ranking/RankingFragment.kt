package iesvdc.segdodam.recyclerviewmotos.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import iesvdc.segdodam.recyclerviewmotos.databinding.FragmentRankingBinding
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame
import iesvdc.segdodam.recyclerviewmotos.ui.videojuegos.VideoGamesViewModel

class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VideoGamesViewModel by activityViewModels()
    private lateinit var adapter: RankingAdapter

    private val rankingOptions = listOf(
        "Mejor valorado",
        "Más popular"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupRankingSelector()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = RankingAdapter(emptyList(), "Puntuación")
        binding.rvRanking.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRanking.adapter = adapter
    }

    private fun setupRankingSelector() {
        val rankingAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            rankingOptions
        )
        binding.actRanking.setAdapter(rankingAdapter)
        binding.actRanking.setText(rankingOptions.first(), false)
        binding.actRanking.setOnItemClickListener { _, _, position, _ ->
            val selected = rankingOptions.getOrNull(position) ?: rankingOptions.first()
            val current = viewModel.videoGames.value.orEmpty()
            applyRanking(current, selected)
        }
    }

    private fun observeViewModel() {
        viewModel.videoGames.observe(viewLifecycleOwner) { list ->
            val criterion = binding.actRanking.text?.toString() ?: rankingOptions.first()
            applyRanking(list, criterion)
        }
    }

    private fun applyRanking(list: List<VideoGame>, criterion: String) {
        val metricLabel = if (criterion == rankingOptions[0]) "Puntuación" else "Visitas"
        adapter.updateData(sortByCriterion(list, criterion), metricLabel)
    }

    private fun sortByCriterion(list: List<VideoGame>, criterion: String): List<VideoGame> {
        return when (criterion) {
            rankingOptions[0] -> list.sortedByDescending { it.puntuacion }
            rankingOptions[1] -> list.sortedByDescending { it.visitas }
            else -> list
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
