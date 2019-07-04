package ru.yandex.money.moira.client.patterns

import org.junit.jupiter.api.Test
import ru.yandex.money.moira.client.fileContent
import ru.yandex.money.moira.client.schedule.Schedule
import ru.yandex.money.moira.client.shouldDeserializeFrom
import ru.yandex.money.moira.client.triggers.Trigger
import ru.yandex.money.moira.client.triggers.TriggerState
import ru.yandex.money.moira.client.triggers.expression.AdvancedExpression
import ru.yandex.money.moira.client.triggers.ttl.TtlCondition
import java.time.Duration
import java.time.ZoneOffset

class PatternTest {

    @Test
    fun `should deserialize from json correctly`() {
        pattern shouldDeserializeFrom fileContent("pattern.json")
    }

    companion object {

        private val pattern = Pattern(
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
    }
}
