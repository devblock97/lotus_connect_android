package devblock.tech.lotus_connect_android.core.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: () -> String?
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenProvider()

        val requestBuilder = originalRequest.newBuilder()

        val path = originalRequest.url.encodedPath
        val isPublicEndpoint = path.contains("/auth/login")
                || path.contains("/auth/register")

        if (!isPublicEndpoint && !token.isNullOrBlank()
            && originalRequest.header("Authorization") == null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}