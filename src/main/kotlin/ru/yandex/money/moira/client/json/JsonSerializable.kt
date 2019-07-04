package ru.yandex.money.moira.client.json

import org.json.JSONObject

/**
 * Represents an object that can be serialized to JSON.
 */
interface JsonSerializable {

    /**
     * Returns JSON representation of this object.
     */
    fun toJson(): JSONObject
}
