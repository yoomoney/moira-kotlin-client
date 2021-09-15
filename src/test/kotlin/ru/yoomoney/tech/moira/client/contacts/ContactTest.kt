package ru.yoomoney.tech.moira.client.contacts

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.yoomoney.tech.moira.client.fileContent
import ru.yoomoney.tech.moira.client.shouldDeserializeFrom
import ru.yoomoney.tech.moira.client.shouldSerializeTo

class ContactTest {

    @Test
    fun `should deserialize from json correctly`() {
        contact shouldDeserializeFrom fileContent("contact.json")
    }

    @Test
    fun `should deserialize from json when user is empty`() {
        val json = JSONObject(fileContent("contact.json"))
        json.put("user", "")

        val actual = Contact(json)

        assertEquals(contact.copy(user = null), actual)
    }

    @Test
    fun `should deserialize from json when id is null`() {
        val json = JSONObject(fileContent("contact.json"))
        json.remove("id")

        val actual = Contact(json)

        assertEquals(contact.copy(id = null), actual)
    }

    @Test
    fun `should serialize to json correctly`() {
        val json = JSONObject(fileContent("contact.json"))
        json.remove("user")

        contact shouldSerializeTo json.toString()
    }

    @Test
    fun `should serialize to json correctly when id is null`() {
        val json = JSONObject(fileContent("contact.json"))
        json.remove("id")
        json.remove("user")

        contact.copy(id = null) shouldSerializeTo json.toString()
    }

    companion object {

        private val contact = Contact(
            id = "dc322a8d-5005-4a21-bf87-2101816c861f",
            type = "telegram",
            value = "@komarovd95",
            user = "komarovdmitry"
        )
    }
}
