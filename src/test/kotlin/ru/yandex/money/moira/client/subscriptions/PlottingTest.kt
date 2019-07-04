package ru.yandex.money.moira.client.subscriptions

import org.junit.jupiter.api.Test
import ru.yandex.money.moira.client.fileContent
import ru.yandex.money.moira.client.shouldDeserializeFrom
import ru.yandex.money.moira.client.shouldSerializeTo

class PlottingTest {

    @Test
    fun `should deserialize from json correctly`() {
        plotting shouldDeserializeFrom fileContent("plotting.json")
    }

    @Test
    fun `should serialize to json correctly`() {
        plotting shouldSerializeTo fileContent("plotting.json")
    }

    companion object {

        private val plotting = Plotting(enabled = true, theme = PlotTheme.DARK)
    }
}
