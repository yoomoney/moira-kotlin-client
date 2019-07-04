package ru.yandex.money.moira.client.pagination

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.yandex.money.moira.client.TestJson
import ru.yandex.money.moira.client.fileContent

class PaginatedTest {

    @Test
    fun `should deserialize from json correctly`() {
        val json = JSONObject(fileContent("paginated.json"))

        val actual = Paginated(json) { TestJson(it) }

        assertEquals(paginated, actual)
    }

    companion object {

        private val paginated = Paginated(
            page = Page(number = 0, size = 2),
            total = 20,
            items = listOf(
                TestJson("1"),
                TestJson("2")
            )
        )
    }
}
