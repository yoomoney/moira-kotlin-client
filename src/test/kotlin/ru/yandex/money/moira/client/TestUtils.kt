package ru.yandex.money.moira.client

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import org.json.JSONObject
import org.junit.jupiter.api.Assertions
import org.skyscreamer.jsonassert.JSONAssert
import ru.yandex.money.moira.client.json.JsonSerializable
import java.util.stream.Collectors
import kotlin.reflect.KClass

fun Any.fileContent(fileName: String): String =
    this::class.java.getResourceAsStream(fileName).bufferedReader().lines().collect(Collectors.joining("\n"))

infix fun JsonSerializable.shouldSerializeTo(json: String) {
    val actualJson = toJson().toString()
    JSONAssert.assertEquals(json, actualJson, true)
}

infix fun Any.shouldDeserializeFrom(json: String) {
    val jsonObject = JSONObject(json)
    val constructor = this::class.constructors.singleOrNull {
        it.parameters.size == 1 && (it.parameters[0].type.classifier as KClass<*>) == JSONObject::class
    } ?: throw IllegalArgumentException("No constructor that takes JSONObject found for class ${this::class}")
    val actualObject = constructor.call(jsonObject)
    Assertions.assertEquals(this, actualObject)
}

fun WireMockServer.stubResponse(statusCode: Int = 200, body: String = "{}") {
    this.stubFor(
        WireMock.any(WireMock.urlMatching(".*"))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(statusCode)
                    .withHeader("Content-Type", "application/json;charset=UTF-8")
                    .withBody(body)
            )
    )
}

fun WireMockServer.stubResponses(vararg mappings: MappingBuilder) {
    mappings.forEach { this.stubFor(it) }
}
