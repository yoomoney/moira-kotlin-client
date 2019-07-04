package ru.yandex.money.moira.client.triggers

import ru.yandex.money.moira.client.enum.EnumWithCode

/**
 * The state of the trigger.
 */
enum class TriggerState(override val code: String) : EnumWithCode {

    /**
     * Trigger is in OK state.
     */
    OK("OK"),

    /**
     * Trigger is in WARN state.
     */
    WARN("WARN"),

    /**
     * Trigger is in ERROR state.
     */
    ERROR("ERROR"),

    /**
     * No data available for given trigger.
     */
    NO_DATA("NODATA"),

    /**
     * Trigger is in exceptional state. It may mean that your trigger expression has illegal format.
     */
    EXCEPTION("EXCEPTION"),

    /**
     * Target metric will be deleted if no data is available.
     */
    DEL("DEL")
}
