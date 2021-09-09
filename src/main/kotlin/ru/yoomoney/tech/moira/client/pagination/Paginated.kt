package ru.yoomoney.tech.moira.client.pagination

import org.json.JSONObject
import ru.yoomoney.tech.moira.client.json.toList

/**
 * Represents a paginated response.
 *
 * @property page the response page information
 * @property total the total number of [items]
 * @property items the list of items on this [page]
 */
data class Paginated<E>(val page: Page, val total: Long, val items: List<E>) {

    constructor(json: JSONObject, itemFactory: (JSONObject) -> E) : this(
        page = Page(json),
        total = json.getLong("total"),
        items = json.getJSONArray("list").toList { itemFactory(getJSONObject(it)) }
    )
}
