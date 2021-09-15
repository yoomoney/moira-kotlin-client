package ru.yoomoney.tech.moira.client.http

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.only
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MoiraHttpClientTest {

    @Test
    fun `should send GET request with authentication`() {
        val httpClientMock = mock<HttpClient> {
            on { get(any()) } doReturn HttpResponse(status = 200, body = "")
        }
        val moiraHttpClient = createHttpClient(httpClientMock)

        val request = HttpRequest(path = "/test", headers = mapOf("X-Custom-Header" to "true"))
        val response = moiraHttpClient.get(request)

        assertEquals(200, response.status)
        verify(httpClientMock, only()).get(argThat { checkRequest(request) })
    }

    @Test
    fun `should send GET request without authentication`() {
        val httpClientMock = mock<HttpClient> {
            on { get(any()) } doReturn HttpResponse(status = 200, body = "")
        }
        val moiraHttpClient = createHttpClient(httpClientMock, false)

        val request = HttpRequest(path = "/test", headers = mapOf("X-Custom-Header" to "true"))
        val response = moiraHttpClient.get(request)

        assertEquals(200, response.status)
        verify(httpClientMock, only()).get(argThat { checkRequest(request, false) })
    }

    @Test
    fun `should send POST request with authentication`() {
        val httpClientMock = mock<HttpClient> {
            on { post(any()) } doReturn HttpResponse(status = 200, body = "")
        }
        val moiraHttpClient = createHttpClient(httpClientMock)

        val request = HttpRequest(path = "/test", headers = mapOf("X-Custom-Header" to "true"))
        val response = moiraHttpClient.post(request)

        assertEquals(200, response.status)
        verify(httpClientMock, only()).post(argThat { checkRequest(request) })
    }

    @Test
    fun `should send POST request without authentication`() {
        val httpClientMock = mock<HttpClient> {
            on { post(any()) } doReturn HttpResponse(status = 200, body = "")
        }
        val moiraHttpClient = createHttpClient(httpClientMock, false)

        val request = HttpRequest(path = "/test", headers = mapOf("X-Custom-Header" to "true"))
        val response = moiraHttpClient.post(request)

        assertEquals(200, response.status)
        verify(httpClientMock, only()).post(argThat { checkRequest(request, false) })
    }

    @Test
    fun `should send PUT request with authentication`() {
        val httpClientMock = mock<HttpClient> {
            on { put(any()) } doReturn HttpResponse(status = 200, body = "")
        }
        val moiraHttpClient = createHttpClient(httpClientMock)

        val request = HttpRequest(path = "/test", headers = mapOf("X-Custom-Header" to "true"))
        val response = moiraHttpClient.put(request)

        assertEquals(200, response.status)
        verify(httpClientMock, only()).put(argThat { checkRequest(request) })
    }

    @Test
    fun `should send PUT request without authentication`() {
        val httpClientMock = mock<HttpClient> {
            on { put(any()) } doReturn HttpResponse(status = 200, body = "")
        }
        val moiraHttpClient = createHttpClient(httpClientMock, false)

        val request = HttpRequest(path = "/test", headers = mapOf("X-Custom-Header" to "true"))
        val response = moiraHttpClient.put(request)

        assertEquals(200, response.status)
        verify(httpClientMock, only()).put(argThat { checkRequest(request, false) })
    }

    @Test
    fun `should send DELETE request with authentication`() {
        val httpClientMock = mock<HttpClient> {
            on { delete(any()) } doReturn HttpResponse(status = 200, body = "")
        }
        val moiraHttpClient = createHttpClient(httpClientMock)

        val request = HttpRequest(path = "/test", headers = mapOf("X-Custom-Header" to "true"))
        val response = moiraHttpClient.delete(request)

        assertEquals(200, response.status)
        verify(httpClientMock, only()).delete(argThat { checkRequest(request) })
    }

    @Test
    fun `should send DELETE request without authentication`() {
        val httpClientMock = mock<HttpClient> {
            on { delete(any()) } doReturn HttpResponse(status = 200, body = "")
        }
        val moiraHttpClient = createHttpClient(httpClientMock, false)

        val request = HttpRequest(path = "/test", headers = mapOf("X-Custom-Header" to "true"))
        val response = moiraHttpClient.delete(request)

        assertEquals(200, response.status)
        verify(httpClientMock, only()).delete(argThat { checkRequest(request, false) })
    }

    private fun createHttpClient(httpClient: HttpClient, useAuthentication: Boolean = true): HttpClient {
        return MoiraHttpClient(
            delegate = httpClient,
            login = if (useAuthentication) "komarovd95" else null,
            credentials = if (useAuthentication) UserCredentials(username = "admin", password = "admin") else null,
            customHeaders = mapOf("X-Test" to "true")
        )
    }

    private fun HttpRequest.checkRequest(initialRequest: HttpRequest, useAuthentication: Boolean = true): Boolean {
        val resultHeaders = mutableMapOf<String, String>()
        resultHeaders["X-Test"] = "true"
        if (useAuthentication) {
            resultHeaders["X-Webauth-User"] = "komarovd95"
            resultHeaders["Authorization"] = "Basic YWRtaW46YWRtaW4="
        }
        resultHeaders["User-Agent"] = "Moira Kotlin Client"
        resultHeaders += initialRequest.headers

        return initialRequest.path == path && headers == resultHeaders
    }
}
