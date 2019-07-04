package ru.yandex.money.moira.client.patterns

import org.json.JSONObject
import ru.yandex.money.moira.client.http.HttpClient
import ru.yandex.money.moira.client.http.HttpException
import ru.yandex.money.moira.client.http.HttpRequest
import ru.yandex.money.moira.client.json.toList

/**
 * Remote collection of patterns stored in Moira.
 *
 * Uses [httpClient] to make HTTP calls.
 *
 * **Developers note**: this implementation is designed only for managing patterns using simple HTTP calls. If you need,
 * for example, caching, then you have to implement this functionality in a separate class using the Decorator pattern.
 * If you need complex logic that can be built on the existing API methods then you need to implement it as an
 * extension method.
 */
class MoiraPatterns(private val httpClient: HttpClient) : Patterns {

    /**
     * Fetches all patterns.
     *
     * Throws [HttpException] if server respond with non 200 status code.
     */
    override fun fetchAll(): List<Pattern> {
        val request = HttpRequest(path = "/pattern")
        val response = httpClient.get(request)
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to fetch all patterns")
        }

        val json = JSONObject(response.body)
        val array = json.getJSONArray("list")

        return array.toList { Pattern(getJSONObject(it)) }
    }

    /**
     * Deletes [pattern]. Returns `true` if [pattern] deleted successfully.
     * Removal is considered successful if the server responded with 200 status code. Returns `false` otherwise.
     */
    override fun delete(pattern: String): Boolean {
        val request = HttpRequest(path = "/pattern/$pattern")
        val response = httpClient.delete(request)
        return response.status == 200
    }
}
