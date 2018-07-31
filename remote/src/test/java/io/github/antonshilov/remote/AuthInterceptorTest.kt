package io.github.antonshilov.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AuthInterceptorTest {

  private val mockWebServer = MockWebServer()
  private val client = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor())
    .build()

  @Before
  fun setup() {
    mockWebServer.start()
    mockWebServer.enqueue(MockResponse())
  }

  @After
  fun cleanup() {
    mockWebServer.shutdown()
  }

  @Test
  fun `should add token header`() {
    val request = Request.Builder().url(mockWebServer.url("/")).build()
    client.newCall(request).execute()

    val recordedRequest = mockWebServer.takeRequest()
    assertEquals(AuthInterceptor.HEADER_VALUE, recordedRequest.getHeader(AuthInterceptor.HEADER_NAME))
  }
}