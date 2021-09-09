package ru.yoomoney.tech.moira.client.tags

import com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.yoomoney.tech.moira.client.AbstractMoiraTest
import ru.yoomoney.tech.moira.client.fileContent
import ru.yoomoney.tech.moira.client.http.HttpException
import ru.yoomoney.tech.moira.client.schedule.Schedule
import ru.yoomoney.tech.moira.client.stubResponse
import ru.yoomoney.tech.moira.client.subscriptions.PlotTheme
import ru.yoomoney.tech.moira.client.subscriptions.Plotting
import ru.yoomoney.tech.moira.client.subscriptions.Subscription
import java.time.ZoneOffset

class MoiraTagsTest : AbstractMoiraTest() {

    @Test
    fun `should fetch all tags`() {
        server.stubResponse(body = """{"list": ["tag1", "tag2"]}""")

        val tags = moira.tags.fetchAll()

        assertEquals(listOf("tag1", "tag2"), tags)
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/tag"))
        )
    }

    @Test
    fun `should throw an exception when fetching all tags and server respond with 500`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.tags.fetchAll() }
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/tag"))
        )
    }

    @Test
    fun `should fetch all tags statistics`() {
        server.stubResponse(body = fileContent("tags.json"))

        val actual = moira.tags.fetchAllStats()

        assertEquals(tags, actual)
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/tag/stats"))
        )
    }

    @Test
    fun `should throw an exception when fetching all tags statistics and server respond with 500`() {
        server.stubResponse(statusCode = 500)

        assertThrows<HttpException> { moira.tags.fetchAllStats() }
        verify(
            1,
            getRequestedFor(urlEqualTo("/api/tag/stats"))
        )
    }

    @Test
    fun `should delete tag`() {
        server.stubResponse()

        val deleted = moira.tags.delete(tag = "moiraclient")

        assertTrue(deleted)
        verify(
            1,
            deleteRequestedFor(urlEqualTo("/api/tag/moiraclient"))
        )
    }

    @Test
    fun `should not delete tag when server respond with 500`() {
        server.stubResponse(statusCode = 500)

        val deleted = moira.tags.delete(tag = "moiraclient")

        assertFalse(deleted)
        verify(
            1,
            deleteRequestedFor(urlEqualTo("/api/tag/moiraclient"))
        )
    }

    companion object {

        private val tags = listOf(
            TagStatistics(
                name = "moiraclient",
                triggers = listOf("8ee8303c-8f4c-40fa-ae42-a6106f45a5f6"),
                subscriptions = listOf(
                    Subscription(
                        id = "bbd80cfd-61cb-4b75-bf7c-16b195a47da7",
                        contacts = listOf("dc228a8d-5005-4a21-bf87-2101816c851f"),
                        tags = listOf("moiraclient"),
                        schedule = Schedule(timeZoneOffset = ZoneOffset.ofHours(-3)),
                        plotting = Plotting(enabled = false, theme = PlotTheme.LIGHT),
                        enabled = true,
                        throttlingEnabled = false,
                        ignoreWarnings = false,
                        ignoreRecoverings = false,
                        user = ""
                    )
                )
            ),
            TagStatistics(
                name = "test",
                triggers = listOf("8ee8303c-8f4c-40fa-ae42-a6106f45a5e5"),
                subscriptions = listOf(
                    Subscription(
                        id = "71bd046e-0f4e-42fb-94c3-b4a929737dd8",
                        contacts = listOf("539c9321-a151-4c1d-a2e9-7c4ab84c7fba"),
                        tags = listOf("test"),
                        schedule = Schedule(timeZoneOffset = ZoneOffset.ofHours(-5)),
                        plotting = Plotting(enabled = false, theme = PlotTheme.DARK),
                        enabled = true,
                        throttlingEnabled = true,
                        ignoreWarnings = false,
                        ignoreRecoverings = false,
                        user = ""
                    )
                )
            )
        )
    }
}
