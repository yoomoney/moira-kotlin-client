package ru.yandex.money.moira.client.http

import ru.yandex.money.moira.client.settings.MoiraSettings

/**
 * Request data.
 */
data class HttpRequest(
    /**
     * The relative path of requested resource (see [MoiraSettings.baseUrl]).
     */
    val path: String,
    /**
     * The collection of HTTP headers.
     */
    val headers: Map<String, String> = emptyMap(),
    /**
     * The collection of query string parameters.
     */
    val queryParameters: Map<String, String> = emptyMap(),
    /**
     * The optional UTF-8 encoded body.
     */
    val body: String? = null
)
