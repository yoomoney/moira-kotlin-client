package ru.yandex.money.moira.client.notifications

import com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.yandex.money.moira.client.AbstractMoiraTest
import ru.yandex.money.moira.client.fileContent
import ru.yandex.money.moira.client.http.HttpException
import ru.yandex.money.moira.client.stubResponse

class MoiraNotificationsTest : AbstractMoiraTest() {

    @Test
    fun `should fetch all notifications successfully`() {
        server.stubResponse(body = fileContent("notifications.json"))

        val notifications = moira.notifications.fetchAll()

        assertEquals(0, notifications.total)
        assertEquals(emptyList<Notification>(), notifications.notifications)
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/notification?start=0&end=-1"))
        )
    }

    @Test
    fun `should fetch all notifications successfully when range set explicitly`() {
        server.stubResponse(body = fileContent("notifications.json"))

        val notifications = moira.notifications.fetchAll(start = 2, end = 10)

        assertEquals(0, notifications.total)
        assertEquals(emptyList<Notification>(), notifications.notifications)
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/notification?start=2&end=10"))
        )
    }

    @Test
    fun `should throw http exception when server respond with 500`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.notifications.fetchAll() }
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/notification?start=0&end=-1"))
        )
    }

    @Test
    fun `should return true when all notifications deleted`() {
        server.stubResponse()

        val deleted = moira.notifications.deleteAll()

        assertTrue(deleted)
        verify(1, deleteRequestedFor(urlEqualTo("/api/notification/all")))
    }

    @Test
    fun `should return false when server respond with 500 while deleting all notifications`() {
        server.stubResponse(statusCode = 500)

        val deleted = moira.notifications.deleteAll()

        assertFalse(deleted)
        verify(1, deleteRequestedFor(urlEqualTo("/api/notification/all")))
    }

    @Test
    fun `should return true when single notification deleted`() {
        server.stubResponse()

        val deleted = moira.notifications.delete(id = "6")

        assertTrue(deleted)
        verify(1, deleteRequestedFor(urlEqualTo("/api/notification?id=6")))
    }

    @Test
    fun `should return false when server respond with 500 while deleting single notification`() {
        server.stubResponse(statusCode = 500)

        val deleted = moira.notifications.delete(id = "6")

        assertFalse(deleted)
        verify(1, deleteRequestedFor(urlEqualTo("/api/notification?id=6")))
    }
}
