package iesvdc.segdodam.recyclerviewmotos.data.repositories

import iesvdc.segdodam.recyclerviewmotos.data.api.FriendRequestBody
import iesvdc.segdodam.recyclerviewmotos.data.api.SendMessageBody
import iesvdc.segdodam.recyclerviewmotos.data.api.SocialApiService
import iesvdc.segdodam.recyclerviewmotos.domain.models.ChatMessageEntity
import iesvdc.segdodam.recyclerviewmotos.domain.models.FriendEntity
import iesvdc.segdodam.recyclerviewmotos.domain.models.UserSearchResultEntity
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialRepository @Inject constructor(
    private val api: SocialApiService
) {
    suspend fun getMessages(friendId: Int): List<ChatMessageEntity> {
        val response = api.getMessages(friendId)
        if (!response.isSuccessful) throw HttpException(response)
        return response.body().orEmpty().map {
            ChatMessageEntity(
                id = it.id,
                senderId = it.senderId,
                receiverId = it.receiverId,
                content = it.content,
                isRead = it.isRead,
                createdAt = it.createdAt
            )
        }
    }

    suspend fun sendMessage(receiverId: Int, content: String): ChatMessageEntity {
        val response = api.sendMessage(SendMessageBody(receiverId = receiverId, content = content))
        if (!response.isSuccessful) throw HttpException(response)
        val body = response.body() ?: throw IllegalStateException("Respuesta de mensaje vacía")
        return ChatMessageEntity(
            id = body.id,
            senderId = body.senderId,
            receiverId = body.receiverId,
            content = body.content,
            isRead = body.isRead,
            createdAt = body.createdAt
        )
    }

    suspend fun getAcceptedFriends(): List<FriendEntity> {
        val response = api.getFriends()
        if (!response.isSuccessful) throw HttpException(response)
        return response.body().orEmpty()
            .filter { it.status == "accepted" }
            .map {
                FriendEntity(
                    friendshipId = it.friendshipId,
                    userId = it.userId,
                    username = it.username,
                    email = it.email,
                    avatarUrl = it.avatarUrl,
                    status = it.status,
                    direction = it.direction,
                    unreadCount = it.unreadCount,
                    createdAt = it.createdAt
                )
            }
    }

    suspend fun searchUsers(query: String): List<UserSearchResultEntity> {
        val response = api.searchUsers(query)
        if (!response.isSuccessful) throw HttpException(response)
        return response.body().orEmpty().map {
            UserSearchResultEntity(
                userId = it.userId,
                username = it.username,
                avatarUrl = it.avatarUrl,
                relation = it.relation
            )
        }
    }

    suspend fun getIncomingRequests(): List<FriendEntity> {
        val response = api.getIncomingRequests()
        if (!response.isSuccessful) throw HttpException(response)
        return response.body().orEmpty().map {
            FriendEntity(
                friendshipId = it.friendshipId,
                userId = it.userId,
                username = it.username,
                email = it.email,
                avatarUrl = it.avatarUrl,
                status = it.status,
                direction = it.direction,
                unreadCount = it.unreadCount,
                createdAt = it.createdAt
            )
        }
    }

    suspend fun sendFriendRequest(addresseeId: Int): String {
        val response = api.sendFriendRequest(FriendRequestBody(addresseeId))
        if (!response.isSuccessful) throw HttpException(response)
        return response.body()?.message ?: "Solicitud enviada"
    }

    suspend fun acceptFriendRequest(friendshipId: Int): String {
        val response = api.acceptFriendRequest(friendshipId)
        if (!response.isSuccessful) throw HttpException(response)
        return response.body()?.message ?: "Solicitud aceptada"
    }

    suspend fun removeFriendship(friendshipId: Int): String {
        val response = api.removeFriendship(friendshipId)
        if (!response.isSuccessful) throw HttpException(response)
        return response.body()?.message ?: "Relación eliminada"
    }
}



