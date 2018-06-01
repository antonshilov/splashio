package io.github.antonshilov.splashio.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
  companion object {
    const val ACCESS_KEY = "2be941011a3c64dd62e7ad3387f720f903c7c0df18aada354087e20fdc1a324f"
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
        .newBuilder()
        .addHeader("Authorization", "Client-ID $ACCESS_KEY")
        .build()
    return chain.proceed(request)
  }
}