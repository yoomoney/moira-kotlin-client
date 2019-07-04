package ru.yandex.money.moira.client.tags

import org.junit.jupiter.api.Test
import ru.yandex.money.moira.client.fileContent
import ru.yandex.money.moira.client.schedule.Schedule
import ru.yandex.money.moira.client.shouldDeserializeFrom
import ru.yandex.money.moira.client.subscriptions.PlotTheme
import ru.yandex.money.moira.client.subscriptions.Plotting
import ru.yandex.money.moira.client.subscriptions.Subscription
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
