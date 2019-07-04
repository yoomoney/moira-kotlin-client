package ru.yandex.money.moira.client.patterns

import com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import org.json.JSONArray
import org.json.JSONObject
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
import ru.yandex.money.moira.client.triggers.Trigger
import ru.yandex.money.moira.client.triggers.TriggerState
import ru.yandex.money.moira.client.triggers.expression.AdvancedExpression
import ru.yandex.money.moira.client.triggers.ttl.TtlCondition
import java.time.Duration
import java.time.ZoneOffset

class MoiraPatternsTest : AbstractMoiraTest() {

    @Test
    fun `should fetch all patterns`() {
        val json = JSONObject()
        val list = JSONArray()
        list.put(JSONObject(fileContent("pattern.json")))
        json.put("list", list)

        server.stubResponse(body = json.toString())

        val actual = moira.patterns.fetchAll()

        assertEquals(patterns, actual)
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/pattern"))
        )
    }

    @Test
    fun `should throw an exception when fetching all patterns and server respond with 500`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.patterns.fetchAll() }
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/pattern"))
        )
    }

    @Test
    fun `should delete concrete pattern`() {
        server.stubResponse()

        val deleted = moira.patterns.delete(patterns[0].pattern)

        assertTrue(deleted)
        verify(
            1,
            deleteRequestedFor(urlEqualTo("/api/pattern/${patterns[0].pattern}"))
        )
    }

    @Test
    fun `should return false when deleting pattern and server respond with 500`() {
        server.stubResponse(statusCode = 500)

        val deleted = moira.patterns.delete(patterns[0].pattern)

        assertFalse(deleted)
        verify(
            1,
            deleteRequestedFor(urlEqualTo("/api/pattern/${patterns[0].pattern}"))
        )
    }

    companion object {

        private val patterns = listOf(
            Pattern(
                metrics = listOf(
                    "test1.metric.process.succeeded.count",
                    "test2.metric.process.succeeded.count"
                ),
                pattern = "*.metric.process.succeeded.count",
                triggers = listOf(
                    Trigger(
                        id = "10cd6a8d-0b49-4c34-a8fa-b01548b28fcf",
                        name = "No test metrics",
                        description = "Test description",
                        targets = listOf("sumSeries(*.metric.process.succeeded.count)"),
                        triggerExpression = AdvancedExpression("OK"),
                        tags = listOf("test"),
                        ttlCondition = TtlCondition(ttl = Duration.ofSeconds(120L), state = TriggerState.ERROR),
                        schedule = Schedule(timeZoneOffset = ZoneOffset.ofHours(-5)),
                        patterns = listOf("*.metric.process.succeeded.count")
                    )
                )
            )
        )
    }
}
