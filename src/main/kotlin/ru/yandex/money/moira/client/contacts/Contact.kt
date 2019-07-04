package ru.yandex.money.moira.client.contacts

import org.json.JSONObject
import ru.yandex.money.moira.client.json.JsonSerializable
import ru.yandex.money.moira.client.json.getStringOrNull
import ru.yandex.money.moira.client.json.set
import ru.yandex.money.moira.client.utils.omitEmpty

/**
 * The contact describes the channel that can be used to receive notifications. One contact can be used in multiple
 * subscriptions.
 *
 * To manage Moira contacts, use the [Contacts] instance.
 */
data class Contact(
    /**
     * The unique identifier of the contact. When creating a new contact, it can be `null` (see [Contacts.create] for
     * details). When updating or fetching an existing contact, this property is required.
     */
    val id: String? = null,
    /**
     * The type of notification channel. For example, `telegram`,` mail`, etc.
     */
    val type: String,
    /**
     * The address of the notification channel. The format of the value depends on the [type] property.
     * For example, if [type] is `telegram`, then the valid value for this type will be your Telegram login (prefixed
     * with `@` symbol).
     */
    val value: String,
    /**
     * The user who created this contact.
     *
     * To identify a user, Moira will never use this property, so it will never be serialized if you create a new or
     * update an existing contact.
     *
     * When a contact is fetched from Moira, this property can be `null` if an empty string is received as a value.
     */
    val user: String? = null
) : JsonSerializable {

    constructor(json: JSONObject) : this(
        id = json.getStringOrNull("id"),
        type = json.getString("type"),
        value = json.getString("value"),
        user = json.getStringOrNull("user")?.omitEmpty()
    )

    override fun toJson(): JSONObject {
        val json = JSONObject()

        if (id != null) {
            json["id"] = id
        }
        json["type"] = type
        json["value"] = value

        return json
    }
}
