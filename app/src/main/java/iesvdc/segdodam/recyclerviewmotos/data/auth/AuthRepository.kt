package iesvdc.segdodam.recyclerviewmotos.data.auth

import iesvdc.segdodam.recyclerviewmotos.data.api.AuthApiService
import iesvdc.segdodam.recyclerviewmotos.data.api.LoginRequest
import iesvdc.segdodam.recyclerviewmotos.data.api.RefreshTokenRequest
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApiService,
    private val sessionManager: SessionManager
) {
    suspend fun login(email: String, password: String) {
        val response = api.login(LoginRequest(email, password))
        if (!response.isSuccessful) throw HttpException(response)
        val body = response.body() ?: throw IllegalStateException("Respuesta vacía")
        sessionManager.saveTokens(body.accessToken, body.refreshToken)
    }

    suspend fun refreshAccessToken(): Boolean {
        val refreshToken = sessionManager.getRefreshToken() ?: return false
        val response = api.refresh(RefreshTokenRequest(refreshToken))
        if (!response.isSuccessful) return false
        val body = response.body() ?: return false
        sessionManager.updateAccessToken(body.accessToken)
        return true
    }

    suspend fun logout() {
        api.logout()
        sessionManager.clear()
    }
}