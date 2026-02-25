package iesvdc.segdodam.recyclerviewmotos.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>

    @POST("auth/refresh")
    fun refreshCall(@Body request: RefreshTokenRequest): retrofit2.Call<RefreshTokenResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<LogoutResponse>

    @GET("me")
    suspend fun getMe(): Response<UserDto>
}

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class RefreshTokenResponse(
    val accessToken: String
)

data class LogoutResponse(
    val message: String
)

data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val role: String,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("is_active")
    val isActive: Boolean
)