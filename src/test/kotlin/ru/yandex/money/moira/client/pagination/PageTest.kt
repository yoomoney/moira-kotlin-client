package ru.yandex.money.moira.client.pagination

import org.junit.jupiter.api.Test
import ru.yandex.money.moira.client.fileContent
import ru.yandex.money.moira.client.shouldDeserializeFrom

class PageTest {

    @Test
    fun `should deserialize from json correctly`() {
        Page(number = 1, size = 10) shouldDeserializeFrom fileContent("page.json")
    }
}
