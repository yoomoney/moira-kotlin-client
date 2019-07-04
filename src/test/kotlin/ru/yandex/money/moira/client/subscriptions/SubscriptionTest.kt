package ru.yandex.money.moira.client.subscriptions

import org.junit.jupiter.api.Test
import ru.yandex.money.moira.client.fileContent
import ru.yandex.money.moira.client.schedule.Schedule
import ru.yandex.money.moira.client.shouldDeserializeFrom
import ru.yandex.money.moira.client.shouldSerializeTo
import java.time.ZoneOffset

class SubscriptionTest {

    @Test
    fun `should deserialize from json correctly`() {
        subscription shouldDeserializeFrom fileContent("subscription.json")
    }

    @Test
    fun `should serialize to json correctly`() {
        subscription shouldSerializeTo fileContent("subscription.json")
    }

    companion object {

        private val subscription = Subscription(
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
    }
}
