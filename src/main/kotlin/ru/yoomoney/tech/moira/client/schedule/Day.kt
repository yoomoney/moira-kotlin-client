package ru.yoomoney.tech.moira.client.schedule

import org.json.JSONObject
import ru.yoomoney.tech.moira.client.json.JsonSerializable
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

/**
 * Represents a day in watch time schedule.
 *
 * @property dayOfWeek the day of the week
 * @property enabled the flag indicating whether notifications can be sent on this [dayOfWeek]
 */
data class Day(val dayOfWeek: DayOfWeek, val enabled: Boolean) : JsonSerializable {

    constructor(json: JSONObject) : this(
        dayOfWeek = mapDayOfWeek(json.getString("name")),
        enabled = json.getBoolean("enabled")
    )

    override fun toJson(): JSONObject {
        val root = JSONObject()

        root.put("name", dayOfWeek.displayName)
        root.put("enabled", enabled)

        return root
    }

    companion object {

        /**
         * Returns day of week by given 3-letter name, e.g. `Mon`, `Tue` and so on.
         *
         * Throws [IllegalArgumentException] if no day of week found by given [name].
         */
        @JvmStatic
        private fun mapDayOfWeek(name: String): DayOfWeek {
            for (dayOfWeek in DayOfWeek.values()) {
                if (name == dayOfWeek.displayName) {
                    return dayOfWeek
                }
            }
            throw IllegalArgumentException("Unknown day of week: $name")
        }

        private val DayOfWeek.displayName
            get() = getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ENGLISH)
    }
}
