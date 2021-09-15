package ru.yoomoney.tech.moira.client

import org.json.JSONObject
import ru.yoomoney.tech.moira.client.json.JsonSerializable

data class TestJson(private val value: String) : JsonSerializable {

    constructor(json: JSONObject) : this(
        value = json.getString("value")
    )

    override fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("value", value)
        return json
    }
}
