package ru.yandex.money.moira.client.notifications

import org.json.JSONObject
import ru.yandex.money.moira.client.http.HttpClient
import ru.yandex.money.moira.client.http.HttpException
import ru.yandex.money.moira.client.http.HttpRequest
import ru.yandex.money.moira.client.json.toList

/**
 * Remote collection of notifications stored in Moira.
 *
 * Uses [httpClient] to make HTTP calls.
 *
 * **Developers note**: this implementation is designed only for managing notifications using simple HTTP calls. If you
 * need, for example, caching, then you have to implement this functionality in a separate class using the Decorator
 * pattern. If you need complex logic that can be built on the existing API methods then you need to implement it as an
 * extension method.
 */
class MoiraNotifications(private val httpClient: HttpClient) : Notifications {

    override fun fetchAll(start: Long, end: Long): NotificationList {
        val request = HttpRequest(
            path = "/notification",
            queryParameters = mapOf("start" to start.toString(), "end" to end.toString())
        )
        val response = httpClient.get(request)
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to fetch all notifications")
        }

        val json = JSONObject(response.body)
        return NotificationList(
            json.getLong("total"),
            json.getJSONArray("list").toList { Notification(getJSONObject(it)) }
        )
    }

    override fun delete(id: String): Boolean {
        val response = httpClient.delete(HttpRequest(path = "/notification", queryParameters = mapOf("id" to id)))
        return response.status == 200
    }

    override fun deleteAll(): Boolean {
        val response = httpClient.delete(HttpRequest(path = "/notification/all"))
        return response.status == 200
    }
}
