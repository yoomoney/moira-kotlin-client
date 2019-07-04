package ru.yandex.money.moira.client.http

/**
 * An implementation of [HttpClient] that modifies requests for Moira.
 *
 * See [MoiraHttpClient.modifyRequest] for modifications.
 */
class MoiraHttpClient(
    /**
     * Client for making HTTP calls.
     */
    private val delegate: HttpClient,
    /**
     * Optional login that used for [authorization](https://moira.readthedocs.io/en/latest/installation/security.html).
     */
    private val login: String? = null,
    /**
     * Optional credentials of current Moira user.
     */
    private val credentials: UserCredentials? = null,
    /**
     * The collection of HTTP headers that will be attached to every request.
     */
    private val customHeaders: Map<String, String> = emptyMap()
) : HttpClient {

    private val encodedCredentials: String? by lazy { credentials?.encode() }

    override fun get(request: HttpRequest): HttpResponse {
        return delegate.get(modifyRequest(request))
    }

    override fun put(request: HttpRequest): HttpResponse {
        return delegate.put(modifyRequest(request))
    }

    override fun delete(request: HttpRequest): HttpResponse {
        return delegate.delete(modifyRequest(request))
    }

    override fun post(request: HttpRequest): HttpResponse {
        return delegate.post(modifyRequest(request))
    }

    /**
     * Modifies request's headers:
     *
     * If property [login] is set then `X-Webauth-User` header will be added to request. Note that header can be
     * overridden by [customHeaders] and [HttpRequest.headers].
     *
     * If property [credentials] is set then `Authorization` header with basic authorization information will be added
     * to request. Note that header can be overridden by [customHeaders] and [HttpRequest.headers].
     *
     * [customHeaders] will be added to request. Note that all of [customHeaders] can be overridden by
     * [HttpRequest.headers].
     *
     * Then all [HttpRequest.headers] of initial [request] will be added.
     *
     * `User-Agent` header will be added.
     */
    private fun modifyRequest(request: HttpRequest): HttpRequest {
        val headers = mutableMapOf<String, String>()
        if (login != null) {
            headers["X-Webauth-User"] = login
        }
        if (credentials != null) {
            headers["Authorization"] = "Basic $encodedCredentials"
        }

        headers.putAll(customHeaders)
        headers.putAll(request.headers)

        headers["User-Agent"] = "Moira Kotlin Client"

        return request.copy(headers = headers)
    }
}
