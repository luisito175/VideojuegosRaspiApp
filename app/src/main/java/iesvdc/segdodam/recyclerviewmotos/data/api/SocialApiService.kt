package iesvdc.segdodam.recyclerviewmotos.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SocialApiService {
    @GET("/api/social/users/search")
    suspend fun searchUsers(@Query("q") query: String): Response<List<UserSearchResultDto>>

    @GET("/api/social/requests")
    suspend fun getIncomingRequests(): Response<List<FriendDto>>

    @GET("/api/social/friends")
    suspend fun getFriends(): Response<List<FriendDto>>

    @POST("/api/social/friends/request")
    suspend fun sendFriendRequest(@Body request: FriendRequestBody): Response<MessageResponse>

    @POST("/api/social/friends/{friendshipId}/accept")
    suspend fun acceptFriendRequest(@Path("friendshipId") friendshipId: Int): Response<MessageResponse>

    @DELETE("/api/social/friends/{friendshipId}")
    suspend fun removeFriendship(@Path("friendshipId") friendshipId: Int): Response<MessageResponse>

    @GET("/api/social/messages/{friendId}")
    suspend fun getMessages(@Path("friendId") friendId: Int): Response<List<ChatMessageDto>>

    @POST("/api/social/messages")
    suspend fun sendMessage(@Body request: SendMessageBody): Response<ChatMessageDto>
}

data class FriendRequestBody(
    val addresseeId: Int
)

data class MessageResponse(
    val message: String
)

data class UserSearchResultDto(
    val userId: Int,
    val username: String,
    val avatarUrl: String?,
    val relation: String
)

data class FriendDto(
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

data class SendMessageBody(
    val receiverId: Int,
    val content: String
)

data class ChatMessageDto(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val content: String,
    val isRead: Boolean,
    val createdAt: String
)



