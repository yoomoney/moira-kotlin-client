package ru.yandex.money.moira.client.triggers

import org.json.JSONObject
import org.junit.jupiter.api.Test
import ru.yandex.money.moira.client.fileContent
import ru.yandex.money.moira.client.schedule.Schedule
import ru.yandex.money.moira.client.shouldDeserializeFrom
import ru.yandex.money.moira.client.shouldSerializeTo
import ru.yandex.money.moira.client.triggers.expression.AdvancedExpression
import ru.yandex.money.moira.client.triggers.ttl.TtlCondition
import java.time.Duration
import java.time.ZoneOffset

class TriggerTest {

    @Test
    fun `should deserialize from json correctly`() {
        trigger shouldDeserializeFrom fileContent("trigger.json")
    }

    @Test
    fun `should deserialize from json correctly when patterns and mute new metrics not provided`() {
        val json = JSONObject(fileContent("trigger.json"))
        json.remove("patterns")
        json.remove("mute_new_metrics")

        trigger.copy(patterns = emptyList()) shouldDeserializeFrom json.toString()
    }

    @Test
    fun `should serialize to json correctly`() {
        val json = JSONObject(fileContent("trigger.json"))
        json.remove("patterns") // when serializing no patterns should be included

        trigger shouldSerializeTo json.toString()
    }

    @Test
    fun `should serialize to json correctly when no id provided`() {
        val json = JSONObject(fileContent("trigger.json"))
        json.remove("id")
        json.remove("patterns") // when serializing no patterns should be included

        trigger.copy(id = null) shouldSerializeTo json.toString()
    }

    companion object {

        private val trigger = Trigger(
            id = "10cd6a8d-0b49-4c34-a8fa-b01548b28fcf",
            name = "No test metrics",
            description = "Test description",
            targets = listOf("sumSeries(*.metric.process.succeeded.count)"),
            triggerExpression = AdvancedExpression("OK"),
            patterns = listOf("*.metric.process.succeeded.count"),
            muteNewMetrics = false,
            ttlCondition = TtlCondition(state = TriggerState.ERROR, ttl = Duration.ofMinutes(2)),
            tags = listOf("test"),
            schedule = Schedule(timeZoneOffset = ZoneOffset.ofHours(-5))
        )
    }
}
