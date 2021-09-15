package ru.yoomoney.tech.moira.client.health

import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.yoomoney.tech.moira.client.AbstractMoiraTest
import ru.yoomoney.tech.moira.client.http.HttpException
import ru.yoomoney.tech.moira.client.stubResponse

class MoiraHealthTest : AbstractMoiraTest() {

    @Test
    fun `should return moira notifier state`() {
        server.stubResponse(body = jsonOk)

        val notifierState = moira.health.fetchCurrentState()

        assertEquals(NotifierState.OK, notifierState)
        verify(1, getRequestedFor(urlEqualTo("/api/health/notifier")))
    }

    @Test
    fun `should throw an exception when fetching notifier state and server respond with 500`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.health.fetchCurrentState() }
        verify(1, getRequestedFor(urlEqualTo("/api/health/notifier")))
    }

    @Test
    fun `should enable moira notifier`() {
        server.stubResponse()

        moira.health.enableNotifications()

        verify(1, putRequestedFor(urlEqualTo("/api/health/notifier"))
            .withRequestBody(EqualToJsonPattern(jsonOk, false, false)))
    }

    @Test
    fun `should throw an exception when enabling notifier and server respond with 500`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.health.enableNotifications() }
        verify(1, putRequestedFor(urlEqualTo("/api/health/notifier")))
    }

    @Test
    fun `should disable moira notifier`() {
        server.stubResponse()

        moira.health.disableNotifications()

        verify(1, putRequestedFor(urlEqualTo("/api/health/notifier"))
            .withRequestBody(EqualToJsonPattern(jsonError, false, false)))
    }

    @Test
    fun `should throw an exception when disabling notifier and server respond with 500`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.health.disableNotifications() }
        verify(1, putRequestedFor(urlEqualTo("/api/health/notifier")))
    }

    companion object {

        val jsonOk = """
            {
              "state": "OK"
            }
        """.trimIndent()

        val jsonError = """
            {
              "state": "ERROR"
            }
        """.trimIndent()
    }
}
