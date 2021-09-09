package ru.yoomoney.tech.moira.client.http

/**
 * Simple interface for making HTTP calls.
 */
interface HttpClient {

    /**
     * Makes **GET** HTTP call.
     *
     * Throws [HttpException] if [HttpRequest.body] is not `null`.
     */
    fun get(request: HttpRequest): HttpResponse

    /**
     * Makes **PUT** HTTP call.
     */
    fun put(request: HttpRequest): HttpResponse

    /**
     * Makes **DELETE** HTTP call.
     *
     * Throws [HttpException] if [HttpRequest.body] is not `null`.
     */
    fun delete(request: HttpRequest): HttpResponse

    /**
     * Makes **POST** HTTP call.
     */
    fun post(request: HttpRequest): HttpResponse
}
