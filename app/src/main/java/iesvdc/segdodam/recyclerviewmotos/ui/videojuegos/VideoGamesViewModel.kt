package iesvdc.segdodam.recyclerviewmotos.ui.videojuegos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iesvdc.segdodam.recyclerviewmotos.domain.models.RecommendationCategory
import iesvdc.segdodam.recyclerviewmotos.domain.models.ReviewEntity
import iesvdc.segdodam.recyclerviewmotos.domain.models.VideoGameEntity
import iesvdc.segdodam.recyclerviewmotos.domain.usecases.*
import iesvdc.segdodam.recyclerviewmotos.models.VideoGame
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class VideoGamesViewModel @Inject constructor(
    private val getAllVideoGamesUseCase: GetAllVideoGamesUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val addVideoGameUseCase: AddVideoGameUseCase,
    private val updateVideoGameUseCase: UpdateVideoGameUseCase,
    private val deleteVideoGameUseCase: DeleteVideoGameUseCase,
    private val registerVisitUseCase: RegisterVisitUseCase,
    private val rateVideoGameUseCase: RateVideoGameUseCase,
    private val getMyRatingUseCase: GetMyRatingUseCase,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _videoGames = MutableLiveData<MutableList<VideoGame>>()
    val videoGames: LiveData<MutableList<VideoGame>> = _videoGames

    private val allVideoGames = mutableListOf<VideoGame>()
    private var currentQuery: String = ""

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _recommendedVideoGames = MutableLiveData<List<VideoGame>>(emptyList())
    val recommendedVideoGames: LiveData<List<VideoGame>> = _recommendedVideoGames

    private val _selectedGameMyRating = MutableLiveData<Int?>()
    val selectedGameMyRating: LiveData<Int?> = _selectedGameMyRating

    private val _selectedGameReviews = MutableLiveData<List<ReviewEntity>>(emptyList())
    val selectedGameReviews: LiveData<List<ReviewEntity>> = _selectedGameReviews

    init {
        loadVideoGames()
    }

    fun refresh() {
        loadVideoGames()
    }

    private fun loadVideoGames() {
        viewModelScope.launch {
            runCatching { getAllVideoGamesUseCase() }
                .onSuccess { entities ->
                    updateListWithFavorites(entities)
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error al cargar datos"
                }
        }
    }

    private suspend fun updateListWithFavorites(entities: List<VideoGameEntity>) {
        val videoGamesList = entities.map { entity ->
            VideoGame(
                id = entity.id,
                nombre = entity.nombre,
                precio = entity.precio,
                plataforma = entity.plataforma,
                caracteristicas = entity.caracteristicas,
                puntuacion = entity.puntuacion,
                visitas = entity.visitas,
                genero = entity.genero,
                totalVotos = entity.totalVotos,
                isFavorite = isFavoriteUseCase(entity.id)
            )
        }.toMutableList()
        allVideoGames.clear()
        allVideoGames.addAll(videoGamesList)
        applyFilter(currentQuery)
    }

    fun updateVideoGame(pos: Int, videoGame: VideoGame) {
        viewModelScope.launch {
            val entity = VideoGameEntity(
                id = videoGame.id,
                nombre = videoGame.nombre,
                precio = videoGame.precio,
                plataforma = videoGame.plataforma,
                caracteristicas = videoGame.caracteristicas,
                puntuacion = videoGame.puntuacion,
                visitas = videoGame.visitas,
                genero = videoGame.genero,
                totalVotos = videoGame.totalVotos
            )
            runCatching { updateVideoGameUseCase(pos, entity) }
                .onSuccess { entities ->
                    updateListWithFavorites(entities)
                }
                .onFailure { error ->
                    _errorMessage.value = "Error al actualizar: ${error.message}"
                }
        }
    }

    fun toggleFavorite(pos: Int) {
        val game = _videoGames.value?.getOrNull(pos) ?: return
        viewModelScope.launch {
            val entity = VideoGameEntity(
                id = game.id,
                nombre = game.nombre,
                precio = game.precio,
                plataforma = game.plataforma,
                caracteristicas = game.caracteristicas,
                puntuacion = game.puntuacion,
                visitas = game.visitas,
                genero = game.genero,
                totalVotos = game.totalVotos
            )
            toggleFavoriteUseCase(entity)
            loadVideoGames()
        }
    }

    fun addVideoGame(videoGame: VideoGame) {
        viewModelScope.launch {
            val entity = VideoGameEntity(
                id = videoGame.id,
                nombre = videoGame.nombre,
                precio = videoGame.precio,
                plataforma = videoGame.plataforma,
                caracteristicas = videoGame.caracteristicas,
                puntuacion = videoGame.puntuacion,
                visitas = videoGame.visitas,
                genero = videoGame.genero,
                totalVotos = videoGame.totalVotos
            )
            runCatching { addVideoGameUseCase(entity) }
                .onSuccess { entities ->
                    updateListWithFavorites(entities)
                }
                .onFailure { error ->
                    _errorMessage.value = parseBackendError(error)
                }
        }
    }

    fun deleteVideoGame(pos: Int, videoGame: VideoGame) {
        viewModelScope.launch {
            val entity = VideoGameEntity(
                id = videoGame.id,
                nombre = videoGame.nombre,
                precio = videoGame.precio,
                plataforma = videoGame.plataforma,
                caracteristicas = videoGame.caracteristicas,
                puntuacion = videoGame.puntuacion,
                visitas = videoGame.visitas,
                genero = videoGame.genero,
                totalVotos = videoGame.totalVotos
            )
            runCatching { deleteVideoGameUseCase(pos, entity) }
                .onSuccess { entities ->
                    updateListWithFavorites(entities)
                }
                .onFailure { error ->
                    _errorMessage.value = parseBackendError(error)
                }
        }
    }

    fun incrementVisitOptimistic(id: Int) {
        // UI optimista: subimos contador local al instante.
        allVideoGames.replaceAll { game ->
            if (game.id == id) game.copy(visitas = game.visitas + 1) else game
        }
        applyFilter(currentQuery)

        viewModelScope.launch {
            runCatching { registerVisitUseCase(id) }
            // Error silencioso por UX: mantenemos la visita optimista en pantalla.
        }
    }

    fun loadRecommendations(category: RecommendationCategory, value: String? = null) {
        viewModelScope.launch {
            runCatching { getRecommendationsUseCase(category, value) }
                .onSuccess { entities ->
                    val mapped = entities.map {
                        VideoGame(
                            id = it.id,
                            nombre = it.nombre,
                            precio = it.precio,
                            plataforma = it.plataforma,
                            caracteristicas = it.caracteristicas,
                            puntuacion = it.puntuacion,
                            visitas = it.visitas,
                            genero = it.genero,
                            totalVotos = it.totalVotos,
                            isFavorite = isFavoriteUseCase(it.id)
                        )
                    }
                    _recommendedVideoGames.value = mapped
                }
                .onFailure { error ->
                    _recommendedVideoGames.value = buildLocalRecommendations(category, value)
                    _errorMessage.value = error.message ?: "Error al recomendar juegos"
                }
        }
    }

    fun loadSmartRecommendations(
        genre: String,
        platform: String,
        budgetText: String,
        mode: String,
        priority: String
    ) {
        val source = allVideoGames.toList()
        if (source.isEmpty()) {
            _recommendedVideoGames.value = emptyList()
            return
        }

        val budget = budgetText.replace(",", ".").toDoubleOrNull()
        val maxVisits = max(source.maxOfOrNull { it.visitas } ?: 1L, 1L).toDouble()

        val ranked = source.map { game ->
            var score = 0.0
            val searchable = "${game.nombre} ${game.genero.orEmpty()} ${game.caracteristicas}".lowercase()

            if (genre.isNotBlank() && (
                        game.genero?.contains(genre, ignoreCase = true) == true
                            || game.caracteristicas.contains(genre, ignoreCase = true)
                        )
            ) {
                score += 4.0
            }

            if (platform.isNotBlank() && game.plataforma.contains(platform, ignoreCase = true)) {
                score += 3.5
            }

            if (budget != null) {
                score += if (game.precio <= budget) 2.5 else -1.5
            }

            score += when {
                mode.contains("multi", ignoreCase = true) &&
                    containsKeywords(searchable, listOf("multi", "co-op", "coop", "online", "pvp", "team")) -> 2.0
                mode.contains("solo", ignoreCase = true) &&
                    containsKeywords(searchable, listOf("single", "historia", "narrativa", "campaign", "aventura")) -> 2.0
                mode.contains("indifer", ignoreCase = true) || mode.isBlank() -> 1.0
                else -> 0.0
            }

            score += when {
                priority.contains("historia", ignoreCase = true) -> if (
                    containsKeywords(searchable, listOf("historia", "narrativa", "rpg", "aventura", "lore"))
                ) 2.2 else 0.0
                priority.contains("desafio", ignoreCase = true) || priority.contains("desaf", ignoreCase = true) -> if (
                    containsKeywords(searchable, listOf("souls", "rogue", "estrategia", "competitivo", "dificil"))
                ) 2.2 else 0.0
                priority.contains("relax", ignoreCase = true) -> if (
                    containsKeywords(searchable, listOf("cozy", "casual", "simulador", "sandbox", "puzzle"))
                ) 2.2 else 0.0
                priority.contains("graf", ignoreCase = true) -> if (
                    containsKeywords(searchable, listOf("graficos", "realista", "open world", "ray", "visual"))
                ) 2.2 else 0.0
                else -> 0.0
            }

            score += (game.puntuacion.coerceIn(0f, 10f) / 10f) * 3.0
            score += (game.visitas / maxVisits) * 1.8
            score += (game.totalVotos.coerceAtLeast(0) / 100.0).coerceAtMost(1.0)

            game to score
        }

        _recommendedVideoGames.value = ranked
            .sortedWith(
                compareByDescending<Pair<VideoGame, Double>> { it.second }
                    .thenByDescending { it.first.puntuacion }
                    .thenByDescending { it.first.visitas }
            )
            .map { it.first }
            .take(20)
    }

    private fun containsKeywords(text: String, keywords: List<String>): Boolean {
        return keywords.any { keyword -> text.contains(keyword, ignoreCase = true) }
    }

    private fun buildLocalRecommendations(
        category: RecommendationCategory,
        value: String?
    ): List<VideoGame> {
        val source = allVideoGames.toList()
        if (source.isEmpty()) return emptyList()

        return when (category) {
            RecommendationCategory.TOP_RATED ->
                source.sortedWith(compareByDescending<VideoGame> { it.puntuacion }.thenByDescending { it.totalVotos })
            RecommendationCategory.MOST_POPULAR ->
                source.sortedByDescending { it.visitas }
            RecommendationCategory.PLATFORM -> {
                val platform = value?.trim().orEmpty()
                source.filter {
                    platform.isBlank() || it.plataforma.contains(platform, ignoreCase = true)
                }.sortedWith(compareByDescending<VideoGame> { it.puntuacion }.thenByDescending { it.visitas })
            }
            RecommendationCategory.GENRE -> {
                val genre = value?.trim().orEmpty()
                source.filter {
                    genre.isBlank() || (it.genero?.contains(genre, ignoreCase = true) == true)
                }.sortedWith(compareByDescending<VideoGame> { it.puntuacion }.thenByDescending { it.visitas })
            }
            RecommendationCategory.BUDGET -> {
                val maxBudget = value?.toDoubleOrNull() ?: 30.0
                source.filter { it.precio <= maxBudget }
                    .sortedWith(compareBy<VideoGame> { it.precio }.thenByDescending { it.puntuacion })
            }
        }
    }

    fun loadGameFeedback(gameId: Int) {
        viewModelScope.launch {
            runCatching { getMyRatingUseCase(gameId) }
                .onSuccess { _selectedGameMyRating.value = it }
                .onFailure { _selectedGameMyRating.value = null }

            runCatching { getReviewsUseCase(gameId) }
                .onSuccess { _selectedGameReviews.value = it }
                .onFailure {
                    _selectedGameReviews.value = emptyList()
                    _errorMessage.value = it.message ?: "Error al cargar reseñas"
                }
        }
    }

    fun submitRating(gameId: Int, rating: Int, comentario: String?) {
        viewModelScope.launch {
            runCatching { rateVideoGameUseCase(gameId, rating, comentario?.takeIf { it.isNotBlank() }) }
                .onSuccess {
                    loadGameFeedback(gameId)
                    loadVideoGames()
                }
                .onFailure {
                    _errorMessage.value = it.message ?: "Error al enviar valoración"
                }
        }
    }

    private fun parseBackendError(error: Throwable): String {
        val msg = error.message ?: "Error desconocido"
        return when {
            msg.contains("403") && msg.contains("crear videojuegos") -> "No tienes permisos para crear videojuegos"
            msg.contains("403") && msg.contains("editar videojuegos") -> "No tienes permisos para editar videojuegos"
            msg.contains("403") && msg.contains("eliminar videojuegos") -> "No tienes permisos para eliminar videojuegos"
            else -> msg
        }
    }

    fun getVideoGameAt(pos: Int): VideoGame? = _videoGames.value?.getOrNull(pos)

    fun setSearchQuery(query: String) {
        currentQuery = query
        applyFilter(query)
    }

    private fun applyFilter(query: String) {
        val filtered = if (query.isBlank()) {
            allVideoGames
        } else {
            allVideoGames.filter { it.nombre.contains(query, ignoreCase = true) }
        }
        _videoGames.value = filtered.toMutableList()
    }
}
