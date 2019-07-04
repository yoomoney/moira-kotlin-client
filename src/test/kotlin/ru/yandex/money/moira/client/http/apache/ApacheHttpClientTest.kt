package ru.yandex.money.moira.client.http.apache

import com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern
import com.github.tomakehurst.wiremock.matching.EqualToPattern
import org.apache.http.impl.client.HttpClients
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.yandex.money.moira.client.AbstractMoiraTest
import ru.yandex.money.moira.client.http.HttpException
import ru.yandex.money.moira.client.http.HttpRequest
import ru.yandex.money.moira.client.stubResponse

class ApacheHttpClientTest : AbstractMoiraTest() {

    @Test
    fun `should send GET request`() {
        val apacheHttpClient = ApacheHttpClient(HttpClients.createDefault(), "http://localhost:${server.port()}/")
        server.stubResponse()

        val response = apacheHttpClient.get(createRequest())

        assertEquals(200, response.status)
        verify(1, getRequestedFor(urlEqualTo("/test?param1=value&%D0%BF%D0%B0%D1%802=%D0%B7%D0%BD%D0%B0%D1%87"))
            .withHeader("X-Custom-Header", EqualToPattern("true")))
    }

    @Test
    fun `should throw http exception when sending GET request with non-empty body`() {
        val apacheHttpClient = ApacheHttpClient(HttpClients.createDefault(), "http://localhost:${server.port()}/")

        assertThrows<HttpException> { apacheHttpClient.get(createRequest(body = "{}")) }
    }

    @Test
    fun `should send POST request`() {
        val apacheHttpClient = ApacheHttpClient(HttpClients.createDefault(), "http://localhost:${server.port()}/")
        server.stubResponse()

        val response = apacheHttpClient.post(createRequest(body = "{}"))

        assertEquals(200, response.status)
        verify(1, postRequestedFor(urlEqualTo("/test?param1=value&%D0%BF%D0%B0%D1%802=%D0%B7%D0%BD%D0%B0%D1%87"))
            .withRequestBody(EqualToJsonPattern("{}", false, false))
            .withHeader("X-Custom-Header", EqualToPattern("true"))
            .withHeader("Content-Type", EqualToPattern("application/json; charset=UTF-8")))
    }

    @Test
    fun `should send POST request without body`() {
        val apacheHttpClient = ApacheHttpClient(HttpClients.createDefault(), "http://localhost:${server.port()}/")
        server.stubResponse()

        val response = apacheHttpClient.post(createRequest())

        assertEquals(200, response.status)
        verify(1, postRequestedFor(urlEqualTo("/test?param1=value&%D0%BF%D0%B0%D1%802=%D0%B7%D0%BD%D0%B0%D1%87"))
            .withHeader("X-Custom-Header", EqualToPattern("true")))
    }

    @Test
    fun `should send PUT request`() {
        val apacheHttpClient = ApacheHttpClient(HttpClients.createDefault(), "http://localhost:${server.port()}/")
        server.stubResponse()

        val response = apacheHttpClient.put(createRequest(body = "{}"))

        assertEquals(200, response.status)
        verify(1, putRequestedFor(urlEqualTo("/test?param1=value&%D0%BF%D0%B0%D1%802=%D0%B7%D0%BD%D0%B0%D1%87"))
            .withRequestBody(EqualToJsonPattern("{}", false, false))
            .withHeader("X-Custom-Header", EqualToPattern("true"))
            .withHeader("Content-Type", EqualToPattern("application/json; charset=UTF-8")))
    }

    @Test
    fun `should send PUT request without body`() {
        val apacheHttpClient = ApacheHttpClient(HttpClients.createDefault(), "http://localhost:${server.port()}/")
        server.stubResponse()

        val response = apacheHttpClient.put(createRequest())

        assertEquals(200, response.status)
        verify(1, putRequestedFor(urlEqualTo("/test?param1=value&%D0%BF%D0%B0%D1%802=%D0%B7%D0%BD%D0%B0%D1%87"))
            .withHeader("X-Custom-Header", EqualToPattern("true")))
    }

    @Test
    fun `should send DELETE request`() {
        val apacheHttpClient = ApacheHttpClient(HttpClients.createDefault(), "http://localhost:${server.port()}/")
        server.stubResponse()

        val response = apacheHttpClient.delete(createRequest())

        assertEquals(200, response.status)
        verify(1, deleteRequestedFor(urlEqualTo("/test?param1=value&%D0%BF%D0%B0%D1%802=%D0%B7%D0%BD%D0%B0%D1%87"))
            .withHeader("X-Custom-Header", EqualToPattern("true")))
    }

    @Test
    fun `should throw http exception when sending DELETE request with non-empty body`() {
        val apacheHttpClient = ApacheHttpClient(HttpClients.createDefault(), "http://localhost:${server.port()}/")

        assertThrows<HttpException> { apacheHttpClient.delete(createRequest(body = "{}")) }
    }

    private fun createRequest(body: String? = null): HttpRequest = HttpRequest(
        path = "/test",
        headers = mapOf("X-Custom-Header" to "true"),
        queryParameters = linkedMapOf("param1" to "value", "пар2" to "знач"), // use LinkedMap to preserve order of parameters, use non latin symbols to check encoding
        body = body
    )
}
