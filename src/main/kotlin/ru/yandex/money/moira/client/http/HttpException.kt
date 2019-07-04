package ru.yandex.money.moira.client.http

/**
 * An exception that can be thrown when any error occurred while HTTP-call.
 */
class HttpException(
    /**
     * Request data.
     */
    val request: HttpRequest,
    /**
     * Response data. If absent then no response has been received.
     */
    val response: HttpResponse?,
    /**
     * Optional message for exception.
     */
    message: String = "Failed to make HTTP-request"
) : RuntimeException("$message: $response")
