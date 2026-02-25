package iesvdc.segdodam.recyclerviewmotos.data.auth

import iesvdc.segdodam.recyclerviewmotos.data.api.AuthApiService
import iesvdc.segdodam.recyclerviewmotos.data.api.LoginRequest
import iesvdc.segdodam.recyclerviewmotos.data.api.RegisterRequest
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepository @Inject constructor(
    private val authApi: AuthApiService,
    private val sessionManager: SessionManager
) {
    suspend fun register(username: String, email: String, password: String) {
        val response = authApi.register(RegisterRequest(username, email, password))
        if (response.isSuccessful) {
            response.body()?.let {
                sessionManager.saveTokens(it.accessToken, it.refreshToken)
                sessionManager.saveUserId(it.user.id)
            }
            return
        }

        // Algunos backends devuelven error pero crean el usuario.
        // Intentamos login con las mismas credenciales antes de fallar.
        val loginResponse = authApi.login(LoginRequest(email, password))
        if (loginResponse.isSuccessful) {
            val body = loginResponse.body()
            if (body != null) {
                sessionManager.saveTokens(body.accessToken, body.refreshToken)
                sessionManager.saveUserId(body.user.id)
            }
            return
        }

        throw HttpException(response)
    }
}