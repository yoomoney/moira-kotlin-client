package ru.yoomoney.tech.moira.client.notifications

import org.json.JSONObject
import ru.yoomoney.tech.moira.client.triggers.Trigger
import ru.yoomoney.tech.moira.client.triggers.expression.SimpleExpression

/**
 * Trigger data for notification.
 */
data class TriggerData(
    /**
     * See [Trigger.id].
     */
    val id: String,
    /**
     * See [Trigger.name].
     */
    val name: String,
    /**
     * See [Trigger.description].
     */
    val description: String,
    /**
     * See [Trigger.targets].
     */
    val targets: List<String>,
    /**
     * See [SimpleExpression.warnValue].
     * Will be `0.0` if trigger in advanced mode.
     */
    val warnValue: Double,
    /**
     * See [SimpleExpression.errorValue].
     * Will be `0.0` if trigger in advanced mode.
     */
    val errorValue: Double,
    /**
     * Flag indicating that this trigger is remote or not.
     *
     * Remote trigger uses graphite storage to retrieve metrics.
     * Local trigger uses Moira's Redis storage.
     */
    val remote: Boolean,
    /**
     * See [Trigger.tags]
     */
    val tags: List<String>
) {

    constructor(json: JSONObject) : this(
        id = json.getString("id"),
        name = json.getString("name"),
        description = json.getString("desc"),
        targets = json.getJSONArray("targets").map { it as String },
        warnValue = json.getDouble("warn_value"),
        errorValue = json.getDouble("error_value"),
        remote = json.getBoolean("is_remote"),
        tags = json.getJSONArray("__notifier_trigger_tags").map { it as String }
    )
}
