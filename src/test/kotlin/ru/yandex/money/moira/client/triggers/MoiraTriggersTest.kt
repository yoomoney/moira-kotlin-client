package ru.yandex.money.moira.client.triggers

import com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.yandex.money.moira.client.AbstractMoiraTest
import ru.yandex.money.moira.client.fileContent
import ru.yandex.money.moira.client.http.HttpException
import ru.yandex.money.moira.client.schedule.Schedule
import ru.yandex.money.moira.client.stubResponse
import ru.yandex.money.moira.client.triggers.expression.AdvancedExpression
import ru.yandex.money.moira.client.triggers.expression.SimpleExpression
import ru.yandex.money.moira.client.triggers.ttl.TtlCondition
import java.time.Duration
import java.time.LocalTime
import java.time.ZoneOffset

class MoiraTriggersTest : AbstractMoiraTest() {

    @Test
    fun `should fetch all triggers`() {
        server.stubResponse(body = fileContent("triggers.json"))

        val actual = moira.triggers.fetchAll()

        assertEquals(triggers, actual)
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/trigger"))
        )
    }

    @Test
    fun `should throw an exception when server respond with non 200 status code while fetching all triggers`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.triggers.fetchAll() }
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/trigger"))
        )
    }

    @Test
    fun `should fetch trigger by id`() {
        server.stubResponse(body = JSONObject(fileContent("triggers.json")).getJSONArray("list").getJSONObject(0).toString())

        val trigger = moira.triggers.fetchById(id = triggers[0].id!!)

        assertEquals(triggers[0], trigger)
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/trigger/${triggers[0].id}"))
        )
    }

    @Test
    fun `should return null when server respond with 404 status code while fetching trigger by id`() {
        server.stubResponse(statusCode = 404)

        val trigger = moira.triggers.fetchById(id = triggers[0].id!!)

        assertNull(trigger)
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/trigger/${triggers[0].id}"))
        )
    }

    @Test
    fun `should throw an exception when server respond with non 200 status code while fetching trigger by id`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.triggers.fetchById(id = triggers[0].id!!) }
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/trigger/${triggers[0].id}"))
        )
    }

    @Test
    fun `should create trigger`() {
        server.stubResponse(body = """{"id": "${triggers[0].id}"}""")

        val id = moira.triggers.create(trigger = triggers[0].copy(id = null))

        assertEquals(triggers[0].id, id)
        verify(
            1,
            putRequestedFor(urlEqualTo("/api/trigger"))
        )
    }

    @Test
    fun `should throw an exception when server respond with non 200 status code while creating new trigger`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.triggers.create(trigger = triggers[0].copy(id = null)) }
        verify(
            1,
            putRequestedFor(urlEqualTo("/api/trigger"))
        )
    }

    @Test
    fun `should update existing trigger`() {
        server.stubResponse()

        val updated = moira.triggers.update(trigger = triggers[0])

        assertTrue(updated)
        verify(
            1,
            putRequestedFor(urlEqualTo("/api/trigger/${triggers[0].id}"))
        )
    }

    @Test
    fun `should throw an exception when trigger id not provided`() {
        assertThrows<IllegalArgumentException> { moira.triggers.update(trigger = triggers[0].copy(id = null)) }
    }

    @Test
    fun `should return false when server respond with non 200 status code while updating existing trigger`() {
        server.stubResponse(statusCode = 500)

        val updated = moira.triggers.update(trigger = triggers[0])

        assertFalse(updated)
        verify(
            1,
            putRequestedFor(urlEqualTo("/api/trigger/${triggers[0].id}"))
        )
    }

    @Test
    fun `should delete existing trigger by id`() {
        server.stubResponse()

        val deleted = moira.triggers.delete(id = triggers[0].id!!)

        assertTrue(deleted)
        verify(
            1,
            deleteRequestedFor(urlEqualTo("/api/trigger/${triggers[0].id}"))
        )
    }

    @Test
    fun `should return false when server respond with non 200 status code while deleting trigger by id`() {
        server.stubResponse(statusCode = 500)

        val deleted = moira.triggers.delete(id = triggers[0].id!!)

        assertFalse(deleted)
        verify(
            1,
            deleteRequestedFor(urlEqualTo("/api/trigger/${triggers[0].id}"))
        )
    }

    companion object {

        private val triggers = listOf(
            Trigger(
                id = "aeeed0e2-4b89-4e4b-9957-8edbbbaba892",
                name = "Test trigger 1",
                description = "This trigger is used for tests only",
                targets = listOf("alias(sumSeries(*.*.nginx.requests.count), 'Nginx Requests')"),
                tags = listOf("test", "nginx"),
                triggerExpression = AdvancedExpression("OK"),
                ttlCondition = TtlCondition(
                    ttl = Duration.ofMinutes(5),
                    state = TriggerState.ERROR
                ),
                schedule = Schedule(
                    start = LocalTime.parse("06:00"),
                    timeZoneOffset = ZoneOffset.ofHours(-3)
                ),
                patterns = listOf("*.*.nginx.requests.count"),
                muteNewMetrics = true
            ),
            Trigger(
                id = "654e3612-fa26-4626-a890-4027ba76d09b",
                name = "Test trigger 2",
                targets = listOf("*.*.nginx.requests.p95"),
                tags = listOf("test"),
                triggerExpression = SimpleExpression(
                    triggerType = TriggerType.FALLING,
                    warnValue = 50.0,
                    errorValue = 10.0
                ),
                schedule = Schedule(
                    start = LocalTime.parse("09:00"),
                    timeZoneOffset = ZoneOffset.ofHours(-3)
                ),
                patterns = listOf("*.*.nginx.requests")
            )
        )
    }
}
