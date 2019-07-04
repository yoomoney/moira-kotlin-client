package ru.yandex.money.moira.client.tags

import org.json.JSONObject
import ru.yandex.money.moira.client.json.toList
import ru.yandex.money.moira.client.subscriptions.Subscription

/**
 * Tag with information about triggers and subscriptions.
 *
 * @property name the name of tag
 * @property triggers the list of triggers that categorized by this tag
 * @property subscriptions the list of subscriptions that subscribed on this tag
 */
data class TagStatistics(
    val name: String,
    val triggers: List<String>,
    val subscriptions: List<Subscription>
) {

    constructor(json: JSONObject) : this(
            name = json.getString("name"),
            triggers = json.getJSONArray("triggers").map { it.toString() },
            subscriptions = json.getJSONArray("subscriptions").toList { Subscription(getJSONObject(it)) }
    )
}
