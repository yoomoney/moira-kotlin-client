package ru.yoomoney.tech.moira.client.tags

import org.junit.jupiter.api.Test
import ru.yoomoney.tech.moira.client.fileContent
import ru.yoomoney.tech.moira.client.schedule.Schedule
import ru.yoomoney.tech.moira.client.shouldDeserializeFrom
import ru.yoomoney.tech.moira.client.subscriptions.PlotTheme
import ru.yoomoney.tech.moira.client.subscriptions.Plotting
import ru.yoomoney.tech.moira.client.subscriptions.Subscription
import java.time.ZoneOffset

class TagStatisticsTest {

    @Test
    fun `should deserialize from json correctly`() {
        tag shouldDeserializeFrom fileContent("tag.json")
    }

    companion object {

        private val tag = TagStatistics(
            name = "moiraclient",
            triggers = listOf("8ee8303c-8f4c-40fa-ae42-a6106f45a5f6"),
            subscriptions = listOf(
                Subscription(
                    id = "bbd79cfd-61cb-4d65-bf7c-16b195a47da7",
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
        )
    }
}
