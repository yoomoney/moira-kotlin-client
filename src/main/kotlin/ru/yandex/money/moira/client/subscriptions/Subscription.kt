package ru.yandex.money.moira.client.subscriptions

import org.json.JSONObject
import ru.yandex.money.moira.client.json.JsonSerializable
import ru.yandex.money.moira.client.schedule.Schedule

/**
 * Represents subscription in Moira.
 *
 * See more information about [subscriptions](https://moira.readthedocs.io/en/latest/user_guide/subscriptions.html).
 *
 * @property id the unique id of subscription. When fetching or updating existing subscription in Moira this property
 * cannot be `null`. When creating new subscription this property may be `null`.
 * @property contacts the list of contacts that subscribed on this subscription
 * @property tags the list of tags that used to collect trigger events
 * @property schedule the schedule of subscription
 * @property plotting the plotting settings of subscription
 * @property enabled the flag indicating the current subscription is enabled
 * @property ignoreWarnings the flag indicating that WARN notifications should not be sent by this subscription
 * @property ignoreRecoverings the flag indicating that only trigger degradation events should be sent by this
 * subscription
 * @property throttlingEnabled the flag indicating that notifications may be throttled by this subscription
 * @property user the user that created this subscription
 */
data class Subscription(
    val id: String? = null,
    val contacts: List<String>,
    val tags: List<String>,
    val schedule: Schedule = Schedule(),
    val plotting: Plotting = Plotting(enabled = true, theme = PlotTheme.DARK),
    val enabled: Boolean = true,
    val ignoreWarnings: Boolean = false,
    val ignoreRecoverings: Boolean = false,
    val throttlingEnabled: Boolean = false,
    val user: String = ""
) : JsonSerializable {

    constructor(json: JSONObject) : this(
            id = json.getString("id"),
            contacts = json.getJSONArray("contacts").map { it.toString() },
            tags = json.getJSONArray("tags").map { it.toString() },
            schedule = Schedule(json.getJSONObject("sched")),
            plotting = Plotting(json.getJSONObject("plotting")),
            enabled = json.getBoolean("enabled"),
            ignoreWarnings = json.getBoolean("ignore_warnings"),
            ignoreRecoverings = json.getBoolean("ignore_recoverings"),
            throttlingEnabled = json.getBoolean("throttling"),
            user = json.getString("user")
    )

    override fun toJson(): JSONObject {
        val json = JSONObject()

        if (id != null) {
            json.put("id", id)
        }
        json.put("contacts", contacts)
        json.put("tags", tags)
        json.put("sched", schedule.toJson())
        json.put("plotting", plotting.toJson())
        json.put("enabled", enabled)
        json.put("ignore_warnings", ignoreWarnings)
        json.put("ignore_recoverings", ignoreRecoverings)
        json.put("throttling", throttlingEnabled)
        json.put("user", user)

        return json
    }
}
