package ru.yoomoney.tech.moira.client.schedule

import org.json.JSONObject
import ru.yoomoney.tech.moira.client.json.JsonSerializable
import ru.yoomoney.tech.moira.client.json.toJsonArray
import ru.yoomoney.tech.moira.client.json.toList
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

/**
 * Represents a watch time schedule.
 *
 * Note that [end] can be greater than [start] property. It means that your watch time will be: from [start] of one day
 * to [end] of next day.
 *
 * @property start the start of watch time period
 * @property end the end of watch time period
 * @property timeZoneOffset the time zone offset of watch time period
 * @property days the information about day
 */
data class Schedule(
    val start: LocalTime = LocalTime.MIN,
    val end: LocalTime = LocalTime.MAX.truncatedTo(ChronoUnit.MINUTES),
    val timeZoneOffset: ZoneOffset = ZoneOffset.ofTotalSeconds(0),
    val days: List<Day> = DayOfWeek.values().map { Day(it, true) }
) : JsonSerializable {

    constructor(json: JSONObject) : this(
        start = LocalTime.ofSecondOfDay(TimeUnit.MINUTES.toSeconds(json.getLong("startOffset"))),
        end = LocalTime.ofSecondOfDay(TimeUnit.MINUTES.toSeconds(json.getLong("endOffset"))),
        timeZoneOffset = ZoneOffset.ofTotalSeconds(TimeUnit.MINUTES.toSeconds(json.getLong("tzOffset")).toInt()),
        days = json.getJSONArray("days").toList { Day(getJSONObject(it)) }
    )

    override fun toJson(): JSONObject {
        val root = JSONObject()

        root.put("startOffset", start.get(ChronoField.MINUTE_OF_DAY))
        root.put("endOffset", end.get(ChronoField.MINUTE_OF_DAY))
        root.put("tzOffset", TimeUnit.SECONDS.toMinutes(timeZoneOffset.totalSeconds.toLong()))
        root.put("days", days.toJsonArray())

        return root
    }
}
