package ru.yandex.money.moira.client.schedule

import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.yandex.money.moira.client.shouldDeserializeFrom
import ru.yandex.money.moira.client.shouldSerializeTo
import java.lang.IllegalArgumentException
import java.time.DayOfWeek
import java.util.stream.Stream

class DayTest {

    @ParameterizedTest
    @MethodSource("days")
    fun `should deserialize from json correctly`(dayOfWeek: DayOfWeek, shortName: String) {
        val json = JSONObject()
        json.put("name", shortName)
        json.put("enabled", true)

        val actual = Day(dayOfWeek = dayOfWeek, enabled = true)

        actual shouldDeserializeFrom json.toString()
    }

    @ParameterizedTest
    @MethodSource("days")
    fun `should serialize to json correctly`(dayOfWeek: DayOfWeek, shortName: String) {
        val json = JSONObject()
        json.put("name", shortName)
        json.put("enabled", true)

        val actual = Day(dayOfWeek = dayOfWeek, enabled = true)

        actual shouldSerializeTo json.toString()
    }

    @Test
    fun `should throw an exception when deserializing with unknown day of week`() {
        val json = JSONObject()
        json.put("name", "Unknown")
        json.put("enabled", true)

        assertThrows<IllegalArgumentException> { Day(json) }
    }

    @Suppress("unused")
    companion object {

        @JvmStatic
        fun days(): Stream<Arguments> = Stream.of(
            Arguments.of(DayOfWeek.MONDAY, "Mon"),
            Arguments.of(DayOfWeek.TUESDAY, "Tue"),
            Arguments.of(DayOfWeek.WEDNESDAY, "Wed"),
            Arguments.of(DayOfWeek.THURSDAY, "Thu"),
            Arguments.of(DayOfWeek.FRIDAY, "Fri"),
            Arguments.of(DayOfWeek.SATURDAY, "Sat"),
            Arguments.of(DayOfWeek.SUNDAY, "Sun")
        )
    }
}
