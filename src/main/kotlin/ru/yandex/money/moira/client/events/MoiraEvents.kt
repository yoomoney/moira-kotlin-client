package ru.yandex.money.moira.client.events

import org.json.JSONObject
import ru.yandex.money.moira.client.http.HttpClient
import ru.yandex.money.moira.client.http.HttpException
import ru.yandex.money.moira.client.http.HttpRequest
import ru.yandex.money.moira.client.pagination.Page
import ru.yandex.money.moira.client.pagination.Paginated

/**
 * Remote collection of events stored in Moira.
 *
 * Uses [httpClient] to make HTTP calls.
 *
 * **Developers note**: this implementation is designed only for managing events using simple HTTP calls. If you need,
 * for example, caching, then you have to implement this functionality in a separate class using the Decorator pattern.
 * If you need complex logic that can be built on the existing API methods then you need to implement it as an
 * extension method.
 */
class MoiraEvents(private val httpClient: HttpClient) : Events {

    /**
     * Fetches events that emitted by a trigger with a given [triggerId]. Events list is paginated by a given [page].
     *
     * Note that no checks for the existence of a trigger with the same [triggerId] will be made. It is assumed that
     * such checks will be made before calling this method.
     *
     * Throws [HttpException] if Moira responded with a non-200 status code.
     */
    override fun fetchByTriggerId(triggerId: String, page: Page): Paginated<Event> {
        val request = HttpRequest(
            path = "/event/$triggerId",
            queryParameters = mapOf("p" to page.number.toString(), "size" to page.size.toString())
        )
        val response = httpClient.get(request)
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to fetch events by trigger id")
        }

        val json = JSONObject(response.body)
        return Paginated(json) { Event(it) }
    }

    /**
     * Deletes all events history for every trigger. Returns `true` if the removal was successful. Removal is
     * considered successful if the server responded with 200 status code. Returns `false` otherwise.
     */
    override fun deleteAll(): Boolean {
        val response = httpClient.delete(HttpRequest(path = "/event/all"))
        return response.status == 200
    }
}
