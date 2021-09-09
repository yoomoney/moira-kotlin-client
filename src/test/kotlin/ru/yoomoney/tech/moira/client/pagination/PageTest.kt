package ru.yoomoney.tech.moira.client.pagination

import org.junit.jupiter.api.Test
import ru.yoomoney.tech.moira.client.fileContent
import ru.yoomoney.tech.moira.client.shouldDeserializeFrom

class PageTest {

    @Test
    fun `should deserialize from json correctly`() {
        Page(number = 1, size = 10) shouldDeserializeFrom fileContent("page.json")
    }
}
