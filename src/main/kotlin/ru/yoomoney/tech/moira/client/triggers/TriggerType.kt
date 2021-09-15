package ru.yoomoney.tech.moira.client.triggers

import ru.yoomoney.tech.moira.client.enum.EnumWithCode

/**
 * The type of a trigger.
 */
enum class TriggerType(override val code: String) : EnumWithCode {

    /**
     * Trigger uses []advanced mode](https://moira.readthedocs.io/en/latest/user_guide/advanced.html).
     */
    EXPRESSION("expression"),

    /**
     * Event triggers if value of the target metric exceeds the specified thresholds.
     */
    RISING("rising"),

    /**
     * Event triggers if value of the target metric falls below the specified thresholds.
     */
    FALLING("falling")
}
