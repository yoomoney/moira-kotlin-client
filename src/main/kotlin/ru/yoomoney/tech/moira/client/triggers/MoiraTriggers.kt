package ru.yoomoney.tech.moira.client.triggers

import org.json.JSONObject
import ru.yoomoney.tech.moira.client.http.HttpClient
import ru.yoomoney.tech.moira.client.http.HttpException
import ru.yoomoney.tech.moira.client.http.HttpRequest
import ru.yoomoney.tech.moira.client.json.toList

/**
 * An implementation of [Triggers] that uses HTTP-calls to Moira backend for all operations.
 *
 * @property httpClient a client that will be used for making HTTP-calls
 *
 * @author Dmitry Komarov [komarovdmitry@yamoney.ru]
 * @since 15.02.2019
 */
class MoiraTriggers(private val httpClient: HttpClient) : Triggers {

    /**
     * Doing **GET/trigger** HTTP-request to fetch list of all triggers.
     */
    override fun fetchAll(): List<Trigger> {
        val request = HttpRequest(path = "/trigger")
        val response = httpClient.get(request)
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to fetch all triggers")
        }

        val json = JSONObject(response.body)
        val list = json.getJSONArray("list")

        return list.toList { Trigger(getJSONObject(it)) }
    }

    /**
     * Doing **GET/trigger/$id** HTTP-request to fetch one trigger by given [id].

     * Returns null if HTTP status code equals to 404.
     */
    override fun fetchById(id: String): Trigger? {
        val request = HttpRequest(path = "/trigger/$id")
        val response = httpClient.get(request)
        if (response.status == 404) {
            return null
        }
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to fetch trigger by id: id=$id")
        }

        val json = JSONObject(response.body)

        return Trigger(json)
    }

    override fun create(trigger: Trigger): String {
        val json = trigger.toJson()
        val request = HttpRequest(path = "/trigger", body = json.toString())
        val response = httpClient.put(request)
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to save new trigger: trigger=$trigger")
        }
        val responseBody = JSONObject(response.body)

        return responseBody.getString("id")
    }

    override fun update(trigger: Trigger): Boolean {
        if (trigger.id == null) {
            throw IllegalArgumentException("Trigger id is mandatory when updating trigger: trigger=$trigger")
        }

        val json = trigger.toJson()
        val request = HttpRequest(path = "/trigger/${trigger.id}", body = json.toString())
        val response = httpClient.put(request)
        return response.status == 200
    }

    override fun delete(id: String): Boolean {
        val response = httpClient.delete(HttpRequest(path = "/trigger/$id"))
        return response.status == 200
    }
}
