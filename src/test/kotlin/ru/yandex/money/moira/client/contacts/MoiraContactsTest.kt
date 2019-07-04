package ru.yandex.money.moira.client.contacts

import com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.yandex.money.moira.client.AbstractMoiraTest
import ru.yandex.money.moira.client.fileContent
import ru.yandex.money.moira.client.http.HttpException
import ru.yandex.money.moira.client.stubResponse
import java.lang.IllegalArgumentException

class MoiraContactsTest : AbstractMoiraTest() {

    @Test
    fun `should fetch all contacts`() {
        server.stubResponse(body = fileContent("contacts.json"))

        val actual = moira.contacts.all()

        assertEquals(contacts, actual)
        verify(1, getRequestedFor(urlEqualTo("/api/contact")))
    }

    @Test
    fun `should throw http exception when fetching all contacts and server respond with non 200`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.contacts.all() }
        verify(1, getRequestedFor(urlEqualTo("/api/contact")))
    }

    @Test
    fun `should create new contact when id is not set`() {
        val json = JSONObject(fileContent("contact.json"))
        json.remove("user")

        server.stubResponse(body = json.toString())

        val id = moira.contacts.create(contact = contacts[0].copy(id = null))

        assertEquals(contacts[0].id, id)

        json.remove("id")
        verify(1, putRequestedFor(urlEqualTo("/api/contact"))
            .withRequestBody(EqualToJsonPattern(json.toString(), false, false)))
    }

    @Test
    fun `should throw http exception when creating new contact and server respond with non 200`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.contacts.create(contact = contacts[0].copy(id = null)) }
        verify(1, putRequestedFor(urlEqualTo("/api/contact")))
    }

    @Test
    fun `should create new contact when id is set`() {
        val json = JSONObject(fileContent("contact.json"))
        json.put("id", "test")
        json.remove("user")

        server.stubResponse(body = json.toString())

        val contact = contacts[0].copy(id = "test")
        val id = moira.contacts.create(contact = contact)

        assertEquals(contact.id, id)
        verify(1, putRequestedFor(urlEqualTo("/api/contact"))
            .withRequestBody(EqualToJsonPattern(json.toString(), false, false)))
    }

    @Test
    fun `should throw http exception when creating new contact with id and server respond with non 200`() {
        val json = JSONObject(fileContent("contact.json"))
        json.put("id", "test")
        json.remove("user")

        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.contacts.create(contact = contacts[0].copy(id = "test")) }
        verify(1, putRequestedFor(urlEqualTo("/api/contact"))
            .withRequestBody(EqualToJsonPattern(json.toString(), false, false)))
    }

    @Test
    fun `should update existing contact`() {
        server.stubResponse(body = fileContent("contact.json"))

        val updated = moira.contacts.update(contacts[0])

        assertTrue(updated)
        verify(1, putRequestedFor(urlEqualTo("/api/contact/${contacts[0].id}")))
    }

    @Test
    fun `should throw http exception when updating existing contact and server respond with non 200`() {
        server.stubResponse(statusCode = 500)

        val updated = moira.contacts.update(contacts[0])

        assertFalse(updated)
        verify(1, putRequestedFor(urlEqualTo("/api/contact/${contacts[0].id}")))
    }

    @Test
    fun `should throw an exception when updating existing contact and no id given`() {
        assertThrows<IllegalArgumentException> { moira.contacts.update(contact = contacts[0].copy(id = null)) }
    }

    @Test
    fun `should return true when contact deleted successfully`() {
        server.stubResponse()

        val deleted = moira.contacts.delete(id = contacts[0].id!!)

        assertTrue(deleted)
        verify(1, deleteRequestedFor(urlEqualTo("/api/contact/${contacts[0].id}")))
    }

    @Test
    fun `should return false when deleting contact and server respond with 500`() {
        server.stubResponse(statusCode = 500)

        val deleted = moira.contacts.delete(id = contacts[0].id!!)

        assertFalse(deleted)
        verify(1, deleteRequestedFor(urlEqualTo("/api/contact/${contacts[0].id}")))
    }

    @Test
    fun `should return true when test notification sent successfully`() {
        server.stubResponse()

        val sent = moira.contacts.sendTestNotification(id = contacts[0].id!!)

        assertTrue(sent)
        verify(1, postRequestedFor(urlEqualTo("/api/contact/${contacts[0].id}/test")))
    }

    @Test
    fun `should return false when sending notification and server respond with 500`() {
        server.stubResponse(statusCode = 500)

        val sent = moira.contacts.sendTestNotification(id = contacts[0].id!!)

        assertFalse(sent)
        verify(1, postRequestedFor(urlEqualTo("/api/contact/${contacts[0].id}/test")))
    }

    companion object {

        private val contacts = listOf(
            Contact(
                id = "dc322a8d-5005-4a21-bf87-2101816c861f",
                type = "telegram",
                value = "@komarovd95"
            ),
            Contact(
                id = "6e86ae27-2b81-4c6b-a2bb-a05ef5e3f29a",
                type = "mail",
                value = "komarovdmitry@yamoney.ru",
                user = "komarovdmitry"
            )
        )
    }
}
