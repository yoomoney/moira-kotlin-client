package ru.yoomoney.tech.moira.client.health

import org.json.JSONObject
import ru.yoomoney.tech.moira.client.http.HttpClient
import ru.yoomoney.tech.moira.client.http.HttpException
import ru.yoomoney.tech.moira.client.http.HttpRequest
import ru.yoomoney.tech.moira.client.json.getEnumByCode

/**
 * An implementation of [Health] that uses HTTP calls to manage notifier state.
 *
 * Property [httpClient] will be used to make HTTP requests.
 */
class MoiraHealth(private val httpClient: HttpClient) : Health {

    /**
     * Fetches the current state of the notifier.
     *
     * Throws [HttpException] if Moira responded with non 200 status code.
     */
    override fun fetchCurrentState(): NotifierState {
        val request = HttpRequest(path = "/health/notifier")
        val response = httpClient.get(request)
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to get notifier's state")
        }
        val json = JSONObject(response.body)
        return json.getEnumByCode("state")
    }

    /**
     * Enables notifier to send notifications.
     *
     * Throws [HttpException] if Moira responded with non 200 status code.
     */
    override fun enableNotifications() {
        setState(NotifierState.OK)
    }

    /**
     * Disables notifier to send notifications.
     *
     * Throws [HttpException] if Moira responded with non 200 status code.
     */
    override fun disableNotifications() {
        setState(NotifierState.ERROR)
    }

    private fun setState(notifierState: NotifierState) {
        val json = notifierState.toJson()
        val request = HttpRequest(path = "/health/notifier", body = json.toString())
        val response = httpClient.put(request)
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to set notifier's state: newState=$notifierState")
        }
    }
}
