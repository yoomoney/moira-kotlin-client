package ru.yandex.money.moira.client.tags

import org.json.JSONObject
import ru.yandex.money.moira.client.http.HttpClient
import ru.yandex.money.moira.client.http.HttpException
import ru.yandex.money.moira.client.http.HttpRequest
import ru.yandex.money.moira.client.json.toList

/**
 * An implementation of [Tags] that uses given [httpClient] to make calls to Moira backend.
 */
class MoiraTags(private val httpClient: HttpClient) : Tags {

    override fun fetchAll(): List<String> {
        val request = HttpRequest(path = "/tag")
        val response = httpClient.get(request)
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to fetch all tags")
        }

        val json = JSONObject(response.body)

        return json.getJSONArray("list").map { it as String }
    }

    override fun fetchAllStats(): List<TagStatistics> {
        val request = HttpRequest(path = "/tag/stats")
        val response = httpClient.get(request)
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to fetch all tags with stats")
        }

        val json = JSONObject(response.body)
        val list = json.getJSONArray("list")

        return list.toList { TagStatistics(getJSONObject(it)) }
    }

    override fun delete(tag: String): Boolean {
        val response = httpClient.delete(HttpRequest(path = "/tag/$tag"))
        return response.status == 200
    }
}
