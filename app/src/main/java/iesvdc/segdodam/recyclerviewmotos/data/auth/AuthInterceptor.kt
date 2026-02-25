package iesvdc.segdodam.recyclerviewmotos.data.auth

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
    val path = original.url().encodedPath()
        val skipAuth = path.startsWith("/auth/login") || path.startsWith("/auth/refresh")

        val token = sessionManager.getAccessToken()
        val request = if (!skipAuth && !token.isNullOrBlank()) {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            original
        }

        return chain.proceed(request)
    }
}