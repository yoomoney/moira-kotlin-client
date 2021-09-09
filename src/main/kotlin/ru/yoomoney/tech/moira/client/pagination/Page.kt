package ru.yoomoney.tech.moira.client.pagination

import org.json.JSONObject

/**
 * Represents a page in paginated request/response.
 *
 * @property number the number of page
 * @property size the size of page
 */
data class Page(val number: Long, val size: Long) {

    constructor(json: JSONObject) : this(number = json.getLong("page"), size = json.getLong("size"))
}
