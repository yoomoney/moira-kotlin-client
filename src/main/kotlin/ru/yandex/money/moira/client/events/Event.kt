package ru.yandex.money.moira.client.events

import org.json.JSONObject
import ru.yandex.money.moira.client.json.getBooleanOrNull
import ru.yandex.money.moira.client.json.getDoubleOrNull
import ru.yandex.money.moira.client.json.getEnumByCode
import ru.yandex.money.moira.client.json.getStringOrNull
import ru.yandex.money.moira.client.triggers.TriggerState
import java.time.Instant

/**
 * Represents trigger's state changes event: [oldState] -> [state].
 */
data class Event(
    /**
     * The trigger ID that emitted this event.
     */
    val triggerId: String,
    /**
     * The subscription to which notification was sent.
     */
    val subscriptionId: String? = null,
    /**
     * The contact to which notification was sent.
     */
    val contactId: String? = null,
    /**
     * The current state of the trigger.
     */
    val state: TriggerState,
    /**
     * The previous state of the trigger.
     */
    val oldState: TriggerState,
    /**
     * The message for this event.
     */
    val message: String? = null,
    /**
     * The current value of the [metric].
     */
    val value: Double? = null,
    /**
     * The metric in *graphite* format that caused this event to be emitted.
     */
    val metric: String,
    /**
     * The time when the event was emitted.
     */
    val timestamp: Instant,
    /**
     * The flag indicating that this event has been emitted by trigger.
     */
    val triggerEvent: Boolean
) {

    constructor(json: JSONObject) : this(
        triggerId = json.getString("trigger_id"),
        subscriptionId = json.getStringOrNull("sub_id"),
        contactId = json.getStringOrNull("contactId"),
        state = json.getEnumByCode("state"),
        oldState = json.getEnumByCode("old_state"),
        message = json.getStringOrNull("msg"),
        value = json.getDoubleOrNull("value"),
        metric = json.getString("metric"),
        timestamp = Instant.ofEpochSecond(json.getLong("timestamp")),
        triggerEvent = json.getBooleanOrNull("trigger_event") ?: false
    )
}
