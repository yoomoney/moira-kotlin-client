package ru.yoomoney.tech.moira.client.schedule

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.yoomoney.tech.moira.client.fileContent
import ru.yoomoney.tech.moira.client.shouldDeserializeFrom
import ru.yoomoney.tech.moira.client.shouldSerializeTo
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.stream.Stream

class ScheduleTest {

    @ParameterizedTest
    @MethodSource("schedules")
    fun `should deserialize from json correctly`(schedule: Schedule, json: String) {
        schedule shouldDeserializeFrom json
    }

    @ParameterizedTest
    @MethodSource("schedules")
    fun `should serialize to json correctly`(schedule: Schedule, json: String) {
        schedule shouldSerializeTo json
    }

    @Suppress("unused")
    companion object {

        @JvmStatic
        fun schedules(): Stream<Arguments> = Stream.of(
            Arguments.of(Schedule(), fileContent("schedule_default.json")),
            Arguments.of(
                Schedule(
                    start = LocalTime.parse("10:00"),
                    end = LocalTime.parse("20:00"),
                    timeZoneOffset = ZoneOffset.ofHours(-5),
                    days = listOf(
                        Day(dayOfWeek = DayOfWeek.MONDAY, enabled = true),
                        Day(dayOfWeek = DayOfWeek.TUESDAY, enabled = true),
                        Day(dayOfWeek = DayOfWeek.WEDNESDAY, enabled = true),
                        Day(dayOfWeek = DayOfWeek.THURSDAY, enabled = true),
                        Day(dayOfWeek = DayOfWeek.FRIDAY, enabled = true),
                        Day(dayOfWeek = DayOfWeek.SATURDAY, enabled = false),
                        Day(dayOfWeek = DayOfWeek.SUNDAY, enabled = false)
                    )
                ),
                fileContent("schedule.json")
            )
        )
    }
}
