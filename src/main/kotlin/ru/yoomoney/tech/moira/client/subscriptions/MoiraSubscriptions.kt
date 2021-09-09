package ru.yoomoney.tech.moira.client.subscriptions

import org.json.JSONObject
import ru.yoomoney.tech.moira.client.http.HttpClient
import ru.yoomoney.tech.moira.client.http.HttpException
import ru.yoomoney.tech.moira.client.http.HttpRequest
import ru.yoomoney.tech.moira.client.json.toList

/**
 * An implementation of [Subscriptions] that uses given [httpClient] to make HTTP calls.
 */
class MoiraSubscriptions(private val httpClient: HttpClient) : Subscriptions {

    override fun fetchAll(): List<Subscription> {
        val request = HttpRequest(path = "/subscription")
        val response = httpClient.get(request)
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to get all user's subscriptions")
        }
        val json = JSONObject(response.body)
        val list = json.getJSONArray("list")

        return list.toList { Subscription(getJSONObject(it)) }
    }

    override fun create(subscription: Subscription): String {
        val json = subscription.toJson()
        val request = HttpRequest(path = "/subscription", body = json.toString())
        val response = httpClient.put(request)
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to create new subscription")
        }
        val jsonResponse = JSONObject(response.body)

        return jsonResponse.getString("id")
    }

    override fun update(subscription: Subscription): Boolean {
        if (subscription.id == null) {
            throw IllegalArgumentException("Subscription must have id when updating")
        }

        val json = subscription.toJson()
        val request = HttpRequest(path = "/subscription/${subscription.id}", body = json.toString())
        val response = httpClient.put(request)
        return response.status == 200
    }

    override fun delete(id: String): Boolean {
        val response = httpClient.delete(HttpRequest(path = "/subscription/$id"))
        return response.status == 200
    }

    override fun sendTestNotification(id: String): Boolean {
        val response = httpClient.put(HttpRequest(path = "/subscription/$id/test"))
        return response.status == 200
    }
}
