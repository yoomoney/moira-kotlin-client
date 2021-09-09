package ru.yoomoney.tech.moira.client.http

/**
 * Response data.
 */
data class HttpResponse(
    /**
     * The HTTP status code.
     */
    val status: Int,
    /**
     * The collection of HTTP headers.
     */
    val headers: Map<String, String> = emptyMap(),
    /**
     * The received UTF-8 encoded response body.
     */
    val body: String
)
