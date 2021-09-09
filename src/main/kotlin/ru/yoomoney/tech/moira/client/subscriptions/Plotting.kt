package ru.yoomoney.tech.moira.client.subscriptions

import org.json.JSONObject
import ru.yoomoney.tech.moira.client.json.JsonSerializable
import ru.yoomoney.tech.moira.client.json.getEnumByCode
import ru.yoomoney.tech.moira.client.json.hasNonNull

/**
 * The plotting settings of subscription.
 *
 * @property enabled the flag indicating that plotting is available for given subscription
 * @property theme the theme of plots
 */
data class Plotting(val enabled: Boolean, val theme: PlotTheme) : JsonSerializable {

    constructor(json: JSONObject) : this(
            enabled = json.getBoolean("enabled"),
            theme = if (json.hasNonNull("theme")) json.getEnumByCode("theme") else PlotTheme.LIGHT
    )

    override fun toJson(): JSONObject {
        val json = JSONObject()

        json.put("enabled", enabled)
        json.put("theme", theme.code)

        return json
    }
}
