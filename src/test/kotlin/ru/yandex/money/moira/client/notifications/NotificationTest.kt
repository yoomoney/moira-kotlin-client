package ru.yandex.money.moira.client.notifications

import org.junit.jupiter.api.Test
import ru.yandex.money.moira.client.contacts.Contact
import ru.yandex.money.moira.client.events.Event
import ru.yandex.money.moira.client.fileContent
import ru.yandex.money.moira.client.shouldDeserializeFrom
import ru.yandex.money.moira.client.subscriptions.PlotTheme
import ru.yandex.money.moira.client.subscriptions.Plotting
import ru.yandex.money.moira.client.triggers.TriggerState
import java.time.Instant

class NotificationTest {

    @Test
    fun `should deserialize from json correctly`() {
        notification shouldDeserializeFrom fileContent("notification.json")
    }

    companion object {

        private val notification = Notification(
            event = Event(
                triggerId = "42e978e6-ab0b-44c0-b89f-acb3191ca9a5",
                subscriptionId = "269817c9-ce89-4566-8a94-ffd9f5db79cf",
                contactId = null,
                state = TriggerState.OK,
                oldState = TriggerState.ERROR,
                message = "This is test event",
                value = 8.0,
                metric = "sumSeries(*.metric.process.succeeded.count)",
                timestamp = Instant.ofEpochSecond(1552805773L),
                triggerEvent = false
            ),
            trigger = TriggerData(
                id = "42e978e6-ab0b-44c0-b89f-acb3191ca9a5",
                name = "No test metrics",
                description = "Test description",
                targets = listOf("sumSeries(*.metric.process.succeeded.count)"),
                warnValue = 0.0,
                errorValue = 0.0,
                remote = false,
                tags = listOf("test")
            ),
            contact = Contact(
                id = "dc322a8d-5005-4a21-bf87-2101816c861f",
                type = "telegram",
                value = "@komarovd95",
                user = "komarovdmitry"
            ),
            plotting = Plotting(enabled = true, theme = PlotTheme.DARK),
            throttled = false,
            sendFails = 0,
            timestamp = Instant.ofEpochSecond(1552805773L)
        )
    }
}
