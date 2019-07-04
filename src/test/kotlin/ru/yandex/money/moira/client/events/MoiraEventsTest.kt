package ru.yandex.money.moira.client.events

import com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.yandex.money.moira.client.AbstractMoiraTest
import ru.yandex.money.moira.client.fileContent
import ru.yandex.money.moira.client.http.HttpException
import ru.yandex.money.moira.client.pagination.Page
import ru.yandex.money.moira.client.stubResponse
import ru.yandex.money.moira.client.triggers.TriggerState
import java.time.Instant

class MoiraEventsTest : AbstractMoiraTest() {

    @Test
    fun `should fetch all events successfully`() {
        server.stubResponse(body = fileContent("events.json"))

        val receivedEvents = moira.events.fetchByTriggerId(triggerId = events[0].triggerId)

        assertEquals(Page(0, 1000), receivedEvents.page)
        assertEquals(3, receivedEvents.total)
        assertEquals(events, receivedEvents.items)
        verify(1, getRequestedFor(urlEqualTo("/api/event/${events[0].triggerId}?p=0&size=100")))
    }

    @Test
    fun `should fetch all events successfully when request page explicitly`() {
        val json = JSONObject(fileContent("events.json"))
        json.put("size", 100)
        json.put("page", 1)
        server.stubResponse(body = json.toString())

        val receivedEvents = moira.events.fetchByTriggerId(triggerId = events[0].triggerId, page = Page(1, 100))

        assertEquals(Page(1, 100), receivedEvents.page)
        assertEquals(3, receivedEvents.total)
        assertEquals(events, receivedEvents.items)
        verify(1, getRequestedFor(urlEqualTo("/api/event/${events[0].triggerId}?p=1&size=100")))
    }

    @Test
    fun `should throw http exception when server respond with 500`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.events.fetchByTriggerId(triggerId = events[0].triggerId) }
        verify(1, getRequestedFor(urlEqualTo("/api/event/${events[0].triggerId}?p=0&size=100")))
    }

    @Test
    fun `should return true when all events deleted`() {
        server.stubResponse()

        val deleted = moira.events.deleteAll()

        assertTrue(deleted)
        verify(1, deleteRequestedFor(urlEqualTo("/api/event/all")))
    }

    @Test
    fun `should return false when server respond with 500 while deleting all events`() {
        server.stubResponse(statusCode = 500)

        val deleted = moira.events.deleteAll()

        assertFalse(deleted)
        verify(1, deleteRequestedFor(urlEqualTo("/api/event/all")))
    }

    companion object {

        private val events = listOf(
            Event(
                triggerId = "9e159f99-029f-4faf-8d88-154cf459443c",
                metric = "test1",
                timestamp = Instant.ofEpochSecond(1544800207L),
                state = TriggerState.OK,
                oldState = TriggerState.ERROR,
                message = "",
                triggerEvent = true,
                contactId = null,
                subscriptionId = null,
                value = null
            ),
            Event(
                triggerId = "9e159f99-029f-4faf-8d88-154cf459443c",
                metric = "test2",
                timestamp = Instant.ofEpochSecond(1544800186L),
                state = TriggerState.OK,
                oldState = TriggerState.ERROR,
                value = 26.0,
                message = null,
                triggerEvent = false,
                contactId = null,
                subscriptionId = null
            ),
            Event(
                triggerId = "9e159f99-029f-4faf-8d88-154cf459443c",
                metric = "test3",
                timestamp = Instant.ofEpochSecond(1544796007L),
                state = TriggerState.ERROR,
                oldState = TriggerState.NO_DATA,
                value = null,
                message = null,
                triggerEvent = false,
                contactId = null,
                subscriptionId = null
            )
        )
    }
}
