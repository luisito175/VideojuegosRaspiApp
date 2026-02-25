package iesvdc.segdodam.recyclerviewmotos.data.auth

import iesvdc.segdodam.recyclerviewmotos.data.api.AuthApiService
import iesvdc.segdodam.recyclerviewmotos.data.api.RefreshTokenRequest
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AuthAuthenticator(
    private val sessionManager: SessionManager,
    private val authApi: AuthApiService
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        val refreshToken = sessionManager.getRefreshToken() ?: return null
        val refreshResponse = authApi.refreshCall(RefreshTokenRequest(refreshToken)).execute()
        if (!refreshResponse.isSuccessful) {
            sessionManager.clear()
            return null
        }

        val newToken = refreshResponse.body()?.accessToken ?: return null
        sessionManager.updateAccessToken(newToken)

        return response.request().newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var prior = response.priorResponse()
        while (prior != null) {
            result++
            prior = prior.priorResponse()
        }
        return result
    }
}