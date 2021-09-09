package ru.yoomoney.tech.moira.client.events

import org.junit.jupiter.api.Test
import ru.yoomoney.tech.moira.client.fileContent
import ru.yoomoney.tech.moira.client.shouldDeserializeFrom
import ru.yoomoney.tech.moira.client.triggers.TriggerState
import java.time.Instant

class EventTest {

    @Test
    fun `should deserialize from json correctly`() {
        event shouldDeserializeFrom fileContent("event.json")
    }

    companion object {

        private val event = Event(
            triggerId = "42e978e6-ab0b-44c0-b89f-acb3191ca9a5",
            subscriptionId = null,
            contactId = null,
            state = TriggerState.ERROR,
            oldState = TriggerState.OK,
            message = "This is test event",
            value = 1.0,
            metric = "Test metric",
            timestamp = Instant.ofEpochSecond(1552805773L),
            triggerEvent = false
        )
    }
}
