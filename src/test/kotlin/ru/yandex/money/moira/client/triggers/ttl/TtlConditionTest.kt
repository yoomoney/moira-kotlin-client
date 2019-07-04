package ru.yandex.money.moira.client.triggers.ttl

import org.json.JSONObject
import org.junit.jupiter.api.Test
import ru.yandex.money.moira.client.fileContent
import ru.yandex.money.moira.client.shouldDeserializeFrom
import ru.yandex.money.moira.client.shouldSerializeTo
import ru.yandex.money.moira.client.triggers.TriggerState
import java.time.Duration

class TtlConditionTest {

    @Test
    fun `should deserialize from json correctly`() {
        ttl shouldDeserializeFrom fileContent("ttl.json")
    }

    @Test
    fun `should deserialize from json without ttl`() {
        val json = JSONObject()
        json.put("ttl_state", "NODATA")

        val actual = TtlCondition(state = TriggerState.NO_DATA)

        actual shouldDeserializeFrom json.toString()
    }

    @Test
    fun `should serialize to json correctly`() {
        ttl shouldSerializeTo fileContent("ttl.json")
    }

    companion object {

        private val ttl = TtlCondition(ttl = Duration.ofMinutes(5), state = TriggerState.NO_DATA)
    }
}
