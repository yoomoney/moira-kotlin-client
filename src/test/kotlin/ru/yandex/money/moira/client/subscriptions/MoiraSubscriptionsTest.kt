package ru.yandex.money.moira.client.subscriptions

import com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.yandex.money.moira.client.AbstractMoiraTest
import ru.yandex.money.moira.client.fileContent
import ru.yandex.money.moira.client.http.HttpException
import ru.yandex.money.moira.client.schedule.Schedule
import ru.yandex.money.moira.client.stubResponse
import java.time.ZoneOffset

class MoiraSubscriptionsTest : AbstractMoiraTest() {

    @Test
    fun `should fetch all subscriptions`() {
        server.stubResponse(body = fileContent("subscriptions.json"))

        val actual = moira.subscriptions.fetchAll()

        assertEquals(subscriptions, actual)
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/subscription"))
        )
    }

    @Test
    fun `should throw an exception when fetching all subscriptions and server respond with 500`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.subscriptions.fetchAll() }
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/subscription"))
        )
    }

    @Test
    fun `should create new subscription `() {
        server.stubResponse(body = """{"id": "${subscriptions[0].id}"}""")

        val id = moira.subscriptions.create(subscription = subscriptions[0].copy(id = null))

        assertEquals(subscriptions[0].id, id)
        verify(
            1,
            putRequestedFor(urlEqualTo("/api/subscription"))
        )
    }

    @Test
    fun `should throw an exception when creating new subscription and server respond with 500`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.subscriptions.create(subscription = subscriptions[0].copy(id = null)) }
        verify(
            1,
            putRequestedFor(urlEqualTo("/api/subscription"))
        )
    }

    @Test
    fun `should update existing subscription when id given`() {
        server.stubResponse()

        val updated = moira.subscriptions.update(subscription = subscriptions[0])

        assertTrue(updated)
        verify(
            1,
            putRequestedFor(urlEqualTo("/api/subscription/${subscriptions[0].id}"))
        )
    }

    @Test
    fun `should update existing subscription when server respond with 500`() {
        server.stubResponse(statusCode = 500)

        val updated = moira.subscriptions.update(subscription = subscriptions[0])

        assertFalse(updated)
        verify(
            1,
            putRequestedFor(urlEqualTo("/api/subscription/${subscriptions[0].id}"))
        )
    }

    @Test
    fun `should delete existing subscription by id`() {
        server.stubResponse()

        val deleted = moira.subscriptions.delete(id = subscriptions[0].id!!)

        assertTrue(deleted)
        verify(
            1,
            deleteRequestedFor(urlEqualTo("/api/subscription/${subscriptions[0].id}"))
        )
    }

    @Test
    fun `should not delete existing subscription by id when server respond with 500`() {
        server.stubResponse(statusCode = 500)

        val deleted = moira.subscriptions.delete(id = subscriptions[0].id!!)

        assertFalse(deleted)
        verify(
            1,
            deleteRequestedFor(urlEqualTo("/api/subscription/${subscriptions[0].id}"))
        )
    }

    @Test
    fun `should send test notification by id`() {
        server.stubResponse()

        val sent = moira.subscriptions.sendTestNotification(id = subscriptions[0].id!!)

        assertTrue(sent)
        verify(
            1,
            putRequestedFor(urlEqualTo("/api/subscription/${subscriptions[0].id}/test"))
        )
    }

    @Test
    fun `should not send test notification by id when server respond with 500`() {
        server.stubResponse(statusCode = 500)

        val sent = moira.subscriptions.sendTestNotification(id = subscriptions[0].id!!)

        assertFalse(sent)
        verify(
            1,
            putRequestedFor(urlEqualTo("/api/subscription/${subscriptions[0].id}/test"))
        )
    }

    companion object {

        private val subscriptions = listOf(
            Subscription(
                id = "bbd80cfd-61cb-4b75-bf7c-16b195a47da7",
                contacts = listOf("dc228a8d-5005-4a21-bf87-2101816c851f"),
                tags = listOf("moiraclient"),
                schedule = Schedule(timeZoneOffset = ZoneOffset.ofHours(-3)),
                plotting = Plotting(enabled = false, theme = PlotTheme.LIGHT),
                enabled = true,
                throttlingEnabled = false,
                ignoreWarnings = false,
                ignoreRecoverings = false,
                user = ""
            ),
            Subscription(
                id = "71bd046e-0f4e-42fb-94c3-b4a929737dd8",
                contacts = listOf("539c9321-a151-4c1d-a2e9-7c4ab84c7fba"),
                tags = listOf("test"),
                schedule = Schedule(timeZoneOffset = ZoneOffset.ofHours(-5)),
                plotting = Plotting(enabled = false, theme = PlotTheme.DARK),
                enabled = true,
                throttlingEnabled = true,
                ignoreWarnings = false,
                ignoreRecoverings = false,
                user = ""
            )
        )
    }
}
