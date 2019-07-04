package ru.yandex.money.moira.client.triggers.ttl

import org.json.JSONObject
import ru.yandex.money.moira.client.json.JsonSerializable
import ru.yandex.money.moira.client.json.getEnumByCode
import ru.yandex.money.moira.client.json.getLongOrNull
import ru.yandex.money.moira.client.triggers.TriggerState
import java.time.Duration

/**
 * Set new trigger [state] when no data available for [ttl] period.
 *
 * See more information at [Dealing with NODATA](https://moira.readthedocs.io/en/latest/user_guide/nodata.html).
 */
data class TtlCondition(val ttl: Duration = DEFAULT_TTL, val state: TriggerState) : JsonSerializable {

    constructor(json: JSONObject) : this(
        ttl = json.getLongOrNull("ttl")?.let { Duration.ofSeconds(it) } ?: DEFAULT_TTL,
        state = json.getEnumByCode("ttl_state")
    )

    override fun toJson(): JSONObject {
        val root = JSONObject()

        root.put("ttl", ttl.seconds)
        root.put("ttl_state", state.code)

        return root
    }

    companion object {

        /**
         * Default TTL period.
         */
        @Suppress("ObjectPropertyNaming")
        @JvmStatic private val DEFAULT_TTL = Duration.ofMinutes(10L)
    }
}
