package ru.yandex.money.moira.client.http.apache

import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils
import ru.yandex.money.moira.client.http.HttpClient
import ru.yandex.money.moira.client.http.HttpException
import ru.yandex.money.moira.client.http.HttpRequest
import ru.yandex.money.moira.client.http.HttpResponse
import java.net.URI

/**
 * An implementation of [HttpClient] that uses Apache HTTP Client as a backend.
 */
class ApacheHttpClient(
    private val httpClient: org.apache.http.client.HttpClient,
    private val baseUrl: String
) : HttpClient {

    override fun get(request: HttpRequest): HttpResponse {
        val httpGet = HttpGet(buildUri(request.path, request.queryParameters))
        request.headers.forEach { name, value -> httpGet.addHeader(name, value) }
        if (request.body != null) {
            throw HttpException(request, null, "Body not allowed for GET requests")
        }
        return execute(httpGet)
    }

    override fun put(request: HttpRequest): HttpResponse {
        val httpPut = HttpPut(buildUri(request.path, request.queryParameters))
        request.headers.forEach { name, value -> httpPut.addHeader(name, value) }
        if (request.body != null) {
            httpPut.entity = StringEntity(request.body, ContentType.APPLICATION_JSON)
        }
        return execute(httpPut)
    }

    override fun delete(request: HttpRequest): HttpResponse {
        val httpDelete = HttpDelete(buildUri(request.path, request.queryParameters))
        request.headers.forEach { name, value -> httpDelete.addHeader(name, value) }
        if (request.body != null) {
            throw HttpException(request, null, "Body not allowed for DELETE requests")
        }
        return execute(httpDelete)
    }

    override fun post(request: HttpRequest): HttpResponse {
        val httpPost = HttpPost(buildUri(request.path, request.queryParameters))
        request.headers.forEach { name, value -> httpPost.addHeader(name, value) }
        if (request.body != null) {
            httpPost.entity = StringEntity(request.body, ContentType.APPLICATION_JSON)
        }
        return execute(httpPost)
    }

    private fun buildUri(path: String, queryParameters: Map<String, String>): URI {
        val builder = URIBuilder("$baseUrl/$path")
        queryParameters.forEach { name, value -> builder.addParameter(name, value) }
        return builder.build()
    }

    private fun execute(request: HttpUriRequest): HttpResponse {
        val httpResponse = httpClient.execute(request)
        return HttpResponse(
            status = httpResponse.statusLine.statusCode,
            headers = extractHeaders(httpResponse),
            body = EntityUtils.toString(httpResponse.entity)
        )
    }

    private fun extractHeaders(httpResponse: org.apache.http.HttpResponse): Map<String, String> {
        val headers = httpResponse.allHeaders
        val responseHeaders = HashMap<String, String>(headers.size)
        headers.forEach { responseHeaders[it.name] = it.value }
        return responseHeaders.toMap()
    }
}
