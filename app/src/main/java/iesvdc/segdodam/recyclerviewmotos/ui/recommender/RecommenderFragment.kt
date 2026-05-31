package iesvdc.segdodam.recyclerviewmotos.ui.recommender

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
import iesvdc.segdodam.recyclerviewmotos.ui.ranking.RankingAdapter
import iesvdc.segdodam.recyclerviewmotos.ui.videojuegos.VideoGamesViewModel

class RecommenderFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VideoGamesViewModel by activityViewModels()
    private lateinit var adapter: RankingAdapter

    private var stepIndex = 0

    private var selectedGenre = ""
    private var selectedPlatform = ""
    private var selectedMode = ""
    private var selectedPriority = ""
    private var budget = ""

    private val questions by lazy {
        listOf(
            getString(R.string.wizard_question_genre),
            getString(R.string.wizard_question_platform),
            getString(R.string.wizard_question_budget),
            getString(R.string.wizard_question_mode),
            getString(R.string.wizard_question_priority)
        )
    }

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
        setupWizard()
        observeRecommendations()
        updateQuestionAndControls()
    }

    private fun setupRecyclerView() {
        adapter = RankingAdapter(emptyList(), getString(R.string.puntuacion))
        binding.rvRanking.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRanking.adapter = adapter
    }

    private fun setupWizard() {
        binding.tvRankingTitle.text = getString(R.string.recommendations_title)

        // Usamos un solo boton para avanzar por pasos.
        binding.btnGenerateRecommendations.text = getString(R.string.wizard_cta_next)
        binding.tilRecPlatform.visibility = View.GONE
        binding.tilRecBudget.visibility = View.GONE
        binding.tilRecMode.visibility = View.GONE
        binding.tilRecPriority.visibility = View.GONE

        setupChoiceDropdown(binding.actRecGenre, listOf("RPG", "Accion", "Aventura", "Shooter", "Estrategia", "Deportes", "Indie"))
        updateQuestionAndControls()

        binding.btnGenerateRecommendations.setOnClickListener {
            if (!validateCurrentStep()) return@setOnClickListener
            saveCurrentAnswer()

            if (stepIndex == questions.lastIndex) {
                generateRecommendations()
                binding.tvRankingSubtitle.text = getString(R.string.wizard_results_ready)
                binding.btnGenerateRecommendations.text = getString(R.string.wizard_recalculate)
            } else {
                stepIndex++
                updateQuestionAndControls()
            }
        }
    }

    private fun setupChoiceDropdown(
        target: com.google.android.material.textfield.MaterialAutoCompleteTextView,
        options: List<String>
    ) {
        val dropdownAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, options)
        target.setAdapter(dropdownAdapter)
    }

    private fun updateQuestionAndControls() {
        binding.tvRankingSubtitle.text = getString(
            R.string.wizard_step_question,
            stepIndex + 1,
            questions.size,
            questions[stepIndex]
        )

        binding.tilRecGenre.visibility = View.GONE
        binding.tilRecPlatform.visibility = View.GONE
        binding.tilRecBudget.visibility = View.GONE
        binding.tilRecMode.visibility = View.GONE
        binding.tilRecPriority.visibility = View.GONE

        when (stepIndex) {
            0 -> {
                binding.tilRecGenre.visibility = View.VISIBLE
                binding.actRecGenre.setText(selectedGenre, false)
                binding.btnGenerateRecommendations.text = getString(R.string.wizard_cta_next)
            }

            1 -> {
                setupChoiceDropdown(binding.actRecPlatform, listOf("PC", "PS5", "Xbox", "Switch", "Steam Deck"))
                binding.tilRecPlatform.visibility = View.VISIBLE
                binding.actRecPlatform.setText(selectedPlatform, false)
            }

            2 -> {
                binding.tilRecBudget.visibility = View.VISIBLE
                binding.etRecBudget.setText(budget)
            }

            3 -> {
                setupChoiceDropdown(
                    binding.actRecMode,
                    listOf(
                        getString(R.string.rec_mode_solo),
                        getString(R.string.rec_mode_multiplayer),
                        getString(R.string.rec_mode_indifferent)
                    )
                )
                binding.tilRecMode.visibility = View.VISIBLE
                binding.actRecMode.setText(selectedMode, false)
            }

            4 -> {
                setupChoiceDropdown(
                    binding.actRecPriority,
                    listOf(
                        getString(R.string.rec_priority_story),
                        getString(R.string.rec_priority_challenge),
                        getString(R.string.rec_priority_relax),
                        getString(R.string.rec_priority_graphics)
                    )
                )
                binding.tilRecPriority.visibility = View.VISIBLE
                binding.actRecPriority.setText(selectedPriority, false)
                binding.btnGenerateRecommendations.text = getString(R.string.wizard_cta_finish)
            }
        }
    }

    private fun validateCurrentStep(): Boolean {
        if (stepIndex == 2) {
            val currentBudget = binding.etRecBudget.text?.toString()?.trim().orEmpty()
            if (currentBudget.isBlank()) return true

            val normalized = currentBudget.replace(",", ".")
            if (normalized.toDoubleOrNull() == null) {
                Toast.makeText(requireContext(), getString(R.string.rec_budget_invalid), Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun saveCurrentAnswer() {
        when (stepIndex) {
            0 -> selectedGenre = binding.actRecGenre.text?.toString()?.trim().orEmpty()
            1 -> selectedPlatform = binding.actRecPlatform.text?.toString()?.trim().orEmpty()
            2 -> budget = binding.etRecBudget.text?.toString()?.trim().orEmpty()
            3 -> selectedMode = binding.actRecMode.text?.toString()?.trim().orEmpty()
            4 -> selectedPriority = binding.actRecPriority.text?.toString()?.trim().orEmpty()
        }
    }

    private fun generateRecommendations() {
        viewModel.loadSmartRecommendations(
            genre = selectedGenre,
            platform = selectedPlatform,
            budgetText = budget,
            mode = selectedMode,
            priority = selectedPriority
        )
    }

    private fun observeRecommendations() {
        viewModel.recommendedVideoGames.observe(viewLifecycleOwner) { list ->
            adapter.updateData(list, getString(R.string.puntuacion))
            binding.rvRanking.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
            binding.tvRecommendationsEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




