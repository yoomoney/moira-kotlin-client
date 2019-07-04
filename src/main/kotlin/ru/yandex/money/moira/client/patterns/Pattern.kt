package ru.yandex.money.moira.client.patterns

import org.json.JSONObject
import ru.yandex.money.moira.client.json.toList
import ru.yandex.money.moira.client.triggers.Trigger

/**
 * Represents pattern in Moira.
 *
 * Pattern is a single dot-separated metric name, possibly containing one or more wildcards.
 *
 * See [patterns](https://moira.readthedocs.io/en/latest/development/architecture.html?highlight=Pattern#pattern) for
 * more information.
 */
data class Pattern(
    /**
     * List of metrics.
     */
    val metrics: List<String>,
    /**
     * Pattern.
     */
    val pattern: String,
    /**
     * Triggers that use this pattern.
     */
    val triggers: List<Trigger>
) {

    constructor(json: JSONObject) : this(
        metrics = json.getJSONArray("metrics").map { it as String },
        pattern = json.getString("pattern"),
        triggers = json.getJSONArray("triggers").toList { Trigger(getJSONObject(it)) }
    )
}
