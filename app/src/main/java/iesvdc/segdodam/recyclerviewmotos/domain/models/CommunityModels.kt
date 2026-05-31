package iesvdc.segdodam.recyclerviewmotos.domain.models

enum class RecommendationCategory {
    TOP_RATED,
    MOST_POPULAR,
    PLATFORM,
    GENRE,
    BUDGET
}

data class ReviewEntity(
    val userId: Int,
    val username: String,
    val avatarUrl: String?,
    val rating: Int,
    val comentario: String,
    val updatedAt: String
)

data class RateResponseEntity(
    val newAverage: Float,
    val myRating: Int,
    val totalVotes: Int
)

data class UserSearchResultEntity(
    val userId: Int,
    val username: String,
    val avatarUrl: String?,
    val relation: String
)

data class FriendEntity(
    val friendshipId: Int,
    val userId: Int,
    val username: String,
    val email: String?,
    val avatarUrl: String?,
    val status: String,
    val direction: String,
    val unreadCount: Int,
    val createdAt: String
)

data class ChatMessageEntity(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val content: String,
    val isRead: Boolean,
    val createdAt: String
)


