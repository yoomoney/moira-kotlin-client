package ru.yandex.money.moira.client.health

import org.json.JSONObject
import ru.yandex.money.moira.client.enum.EnumWithCode
import ru.yandex.money.moira.client.json.JsonSerializable

/**
 * Notifier state.
 */
enum class NotifierState(override val code: String) : EnumWithCode, JsonSerializable {

    /**
     * Notifier is enabled to send notifications.
     */
    OK("OK"),

    /**
     * Notifier is disabled to send notifications.
     */
    ERROR("ERROR")
    ;

    override fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("state", code)
        return json
    }
}
