package iesvdc.segdodam.recyclerviewmotos.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("users")
    suspend fun createUser(@Body request: UserCreateRequest): Response<Unit>
}

data class UserCreateRequest(
    val username: String,
    val email: String,
    val password: String
)