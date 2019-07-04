package ru.yandex.money.moira.client.notifications

import org.json.JSONObject
import ru.yandex.money.moira.client.contacts.Contact
import ru.yandex.money.moira.client.events.Event
import ru.yandex.money.moira.client.subscriptions.Plotting
import java.time.Instant

/**
 * Notification that will be sent through Moira.
 */
data class Notification(
    /**
     * The event that caused this notification to be sent.
     */
    val event: Event,
    /**
     * The trigger that caused this notification to be sent.
     */
    val trigger: TriggerData,
    /**
     * The contact to which notification will be sent.
     */
    val contact: Contact,
    /**
     * The settings of plotting in this notification.
     */
    val plotting: Plotting,
    /**
     * The flag indicating whether the notification was throttled.
     * See more about [throttling](https://moira.readthedocs.io/en/latest/user_guide/throttling.html)
     */
    val throttled: Boolean,
    /**
     * The number of failed attempts to send this notification.
     */
    val sendFails: Int,
    /**
     * The timestamp when this notification is scheduled to be sent.
     */
    val timestamp: Instant
) {

    constructor(json: JSONObject) : this(
        event = Event(json.getJSONObject("event")),
        trigger = TriggerData(json.getJSONObject("trigger")),
        contact = Contact(json.getJSONObject("contact")),
        plotting = Plotting(json.getJSONObject("plotting")),
        throttled = json.getBoolean("throttled"),
        sendFails = json.getInt("send_fail"),
        timestamp = Instant.ofEpochSecond(json.getLong("timestamp"))
    )
}
