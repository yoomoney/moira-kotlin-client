package ru.yoomoney.tech.moira.client.triggers.expression

import org.json.JSONObject
import ru.yoomoney.tech.moira.client.json.JsonSerializable
import ru.yoomoney.tech.moira.client.json.getDoubleOrNull
import ru.yoomoney.tech.moira.client.json.getEnumByCode
import ru.yoomoney.tech.moira.client.json.hasNonNull
import ru.yoomoney.tech.moira.client.triggers.TriggerType

/**
 * The trigger expression that used to describe condition when new trigger's events will be fired.
 *
 * @property triggerType the type of given trigger
 */
sealed class TriggerExpression : JsonSerializable {

    abstract val triggerType: TriggerType

    companion object {

        /**
         * Creates trigger expression from given [json]. If json data cannot be parsed as trigger then
         * [IllegalArgumentException] will be thrown.
         */
        operator fun invoke(json: JSONObject): TriggerExpression =
            if (json.hasNonNull("warn_value") || json.hasNonNull("error_value")) {
                SimpleExpression(json)
            } else if (json.hasNonNull("expression")) {
                AdvancedExpression(json)
            } else {
                throw IllegalArgumentException("Unavailable to parse trigger expression: $json")
            }

        /**
         * Creates trigger expression from given [json]. This method is an alias for [TriggerExpression.invoke] method.
         */
        @JvmStatic
        fun makeTriggerExpression(json: JSONObject): TriggerExpression = invoke(json)
    }
}

/**
 * Expression for [simple trigger mode](https://moira.readthedocs.io/en/latest/user_guide/simple.html).
 */
data class SimpleExpression(
    override val triggerType: TriggerType,
    val warnValue: Double?,
    val errorValue: Double?
) : TriggerExpression() {

    init {
        if (warnValue == null && errorValue == null) {
            throw IllegalStateException("Simple expression must have at least one of WARN or ERROR thresholds")
        }

        when (triggerType) {
            TriggerType.RISING -> if (warnValue != null && errorValue != null && warnValue > errorValue) {
                throw IllegalStateException(
                    "WARN threshold cannot be greater than ERROR threshold when trigger is RISING"
                )
            }
            TriggerType.FALLING -> if (warnValue != null && errorValue != null && warnValue < errorValue) {
                throw IllegalStateException(
                    "WARN threshold cannot be less than ERROR threshold when trigger is FALLING"
                )
            }
            TriggerType.EXPRESSION -> throw IllegalStateException("Simple expression cannot have EXPRESSION type")
        }
    }

    constructor(json: JSONObject) : this(
        triggerType = json.getEnumByCode("trigger_type"),
        warnValue = json.getDoubleOrNull("warn_value"),
        errorValue = json.getDoubleOrNull("error_value")
    )

    override fun toJson(): JSONObject {
        val root = JSONObject()

        root.put("trigger_type", triggerType.code)
        if (warnValue != null) {
            root.put("warn_value", warnValue)
        }
        if (errorValue != null) {
            root.put("error_value", errorValue)
        }

        return root
    }
}

/**
 * Expression for [advanced trigger mode](https://moira.readthedocs.io/en/latest/user_guide/advanced.html).
 */
data class AdvancedExpression(val expression: String) : TriggerExpression() {

    constructor(json: JSONObject) : this(json.getString("expression"))

    override val triggerType = TriggerType.EXPRESSION

    override fun toJson(): JSONObject {
        val root = JSONObject()

        root.put("trigger_type", triggerType.code)
        root.put("expression", expression)

        return root
    }
}
