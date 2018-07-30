package io.github.antonshilov.remote

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Authentication Interceptor
 *
 * Intercepts the [Request] and adds the auth headers to work with the [UnsplashApi]
 */
class AuthInterceptor : Interceptor {
  companion object {
    /**
     * [UnsplashApi] access key to work with it.
     */
    private const val ACCESS_KEY = "2be941011a3c64dd62e7ad3387f720f903c7c0df18aada354087e20fdc1a324f"
    internal const val HEADER_NAME = "Authorization"
    internal const val HEADER_VALUE = "Client-ID $ACCESS_KEY"
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
      .newBuilder()
      .addHeader(HEADER_NAME, HEADER_VALUE)
      .build()
    return chain.proceed(request)
  }
}