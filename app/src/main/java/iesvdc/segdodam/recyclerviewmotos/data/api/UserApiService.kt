package iesvdc.segdodam.recyclerviewmotos.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {
    @POST("users")
    suspend fun createUser(@Body request: UserCreateRequest): Response<Unit>

    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") id: Int): Response<UserDto>

    @PATCH("api/users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body request: UpdateUserRequest
    ): Response<Unit>
}

data class UserCreateRequest(
    val username: String,
    val email: String,
    val password: String
)

data class UpdateUserRequest(
    val username: String?,
    val email: String?,
    val role: String? = null,
    val avatarUrl: String?,
    val isActive: Boolean? = null
)