package ru.yandex.money.moira.client.triggers

import org.json.JSONObject
import ru.yandex.money.moira.client.json.JsonSerializable
import ru.yandex.money.moira.client.json.embed
import ru.yandex.money.moira.client.json.getBooleanOrNull
import ru.yandex.money.moira.client.json.getJsonArrayOrNull
import ru.yandex.money.moira.client.json.getStringOrNull
import ru.yandex.money.moira.client.json.set
import ru.yandex.money.moira.client.triggers.expression.SimpleExpression
import ru.yandex.money.moira.client.triggers.expression.TriggerExpression
import ru.yandex.money.moira.client.schedule.Schedule
import ru.yandex.money.moira.client.triggers.ttl.TtlCondition

/**
 * A model that represents trigger.
 *
 * @property id unique trigger id. When fetching trigger from Moira [id] is required and cannot be `null`. When creating
 * new trigger in Moira [id] can be `null`. In this case Moira will generate that [id] by itself.
 *
 * @property name the name of the trigger. This [name] is displayed in Moira and cannot be empty. [name] is required
 * while fetching and creating new trigger. [name] may not be unique.
 *
 * @property description the brief description of the trigger. This [description] is displayed in Moira.
 * [description] is required while fetching and creating new trigger. When creating new trigger you can leave it
 * an empty string if no description for trigger is available.
 *
 * @property targets the list of metrics in [graphite format](https://graphite.readthedocs.io/en/latest/functions.html).
 * This metrics are used in [triggerExpression] via aliases to describe condition for trigger events firing. Metric's
 * alias has form `t${index}`, e.g. t1, t2 and so on. This list cannot be empty while fetching and creating new trigger.
 *
 * @property tags the list of tags that used for searching and subscription purposes. This list cannot be empty while
 * fetching and creating new trigger. If one of the given tags is not created in Moira yet then Moira will create by
 * itself.
 *
 * @property triggerExpression the expression that describes condition when trigger's event can be fired. This value
 * cannot be `null` while fetching and creating new trigger. Note that when using [SimpleExpression] you can provide
 * only one target metric in [targets] list.
 *
 * @property ttlCondition the condition that describes condition when trigger's event can be fired if no data for
 * [targets] is not available. This value cannot be `null` while fetching and creating new trigger.
 *
 * @property schedule the schedule of watch time. Watch time is the time when trigger events' notifications can be
 * delivered. This value cannot be `null` while fetching and creating new trigger.
 *
 * @property patterns the list of metrics that uses wildcards. This value will be ignored while serialization of this
 * trigger.
 *
 * @property muteNewMetrics the flag that defines that if new metrics are available then notification should be
 * delivered. If `true` then no notification will be send.
 *
 * @author Dmitry Komarov [komarovdmitry@yamoney.ru]
 * @since 15.02.2019
 */
data class Trigger(
    val id: String? = null,
    val name: String,
    val description: String = "",
    val targets: List<String>,
    val tags: List<String>,
    val triggerExpression: TriggerExpression,
    val ttlCondition: TtlCondition = TtlCondition(state = TriggerState.NO_DATA),
    val schedule: Schedule = Schedule(),
    val patterns: List<String> = emptyList(),
    val muteNewMetrics: Boolean = false
) : JsonSerializable {

    constructor(json: JSONObject) : this(
        id = json.getStringOrNull("id"),
        name = json.getString("name"),
        description = json.getString("desc"),
        targets = json.getJSONArray("targets").map { it as String },
        tags = json.getJSONArray("tags").map { it as String },
        triggerExpression = TriggerExpression(json),
        ttlCondition = TtlCondition(json),
        schedule = Schedule(json.getJSONObject("sched")),
        patterns = json.getJsonArrayOrNull("patterns")?.map { it as String } ?: emptyList(),
        muteNewMetrics = json.getBooleanOrNull("mute_new_metrics") ?: false
    )

    override fun toJson(): JSONObject {
        val root = JSONObject()

        if (id != null) {
            root["id"] = id
        }

        root["name"] = name
        root["desc"] = description
        root["tags"] = tags
        root["targets"] = targets
        root.embed(triggerExpression.toJson())
        root.embed(ttlCondition.toJson())
        root["mute_new_metrics"] = muteNewMetrics
        root["sched"] = schedule.toJson()

        return root
    }
}
