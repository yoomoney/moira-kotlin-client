package ru.yandex.money.moira.client.json

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.yandex.money.moira.client.TestJson
import ru.yandex.money.moira.client.enum.EnumWithCode
import java.util.stream.Stream

class JsonExtensionsTest {

    @ParameterizedTest
    @MethodSource("jsonObjects")
    fun `should return json object or null`(json: JSONObject, expected: JSONObject?) {
        val root = json.getJsonObjectOrNull("root")

        assertEquals(expected?.toString(), root?.toString())
    }

    @ParameterizedTest
    @MethodSource("doubles")
    fun `should return double or null`(json: JSONObject, expected: Double?) {
        val value = json.getDoubleOrNull("value")

        assertEquals(expected, value)
    }

    @ParameterizedTest
    @MethodSource("longs")
    fun `should return long or null`(json: JSONObject, expected: Long?) {
        val value = json.getLongOrNull("value")

        assertEquals(expected, value)
    }

    @ParameterizedTest
    @MethodSource("booleans")
    fun `should return boolean or null`(json: JSONObject, expected: Boolean?) {
        val value = json.getBooleanOrNull("value")

        assertEquals(expected, value)
    }

    @ParameterizedTest
    @MethodSource("strings")
    fun `should return string or null`(json: JSONObject, expected: String?) {
        val value = json.getStringOrNull("value")

        assertEquals(expected, value)
    }

    @Test
    fun `should throw an exception when resolving enum by code and no key present in json object`() {
        val json = JSONObject("""{}""")

        assertThrows<JSONException> { json.getEnumByCode<TestEnum>("enum") }
    }

    @Test
    fun `should throw an exception when resolving enum by code and no such code present in enum`() {
        val json = JSONObject("""{"enum": "prod"}""")

        assertThrows<JSONException> { json.getEnumByCode<TestEnum>("enum") }
    }

    @Test
    fun `should return enum by code`() {
        val json = JSONObject("""{"enum": "test"}""")

        val enum = json.getEnumByCode<TestEnum>("enum")

        assertEquals(TestEnum.TEST, enum)
    }

    @Test
    fun `should embed one json object into another`() {
        val first = JSONObject("""{"test": true}""")
        val second = JSONObject("""{"prod": false}""")
        val expected = JSONObject("""{"test": true, "prod": false}""")

        first.embed(second)

        assertEquals(expected.toString(), first.toString())
    }

    @Test
    fun `should convert list of json serializable to json array`() {
        val first = JSONObject()
        first.put("value", "1")

        val second = JSONObject()
        second.put("value", "2")

        val expected = JSONArray()
        expected.put(first)
        expected.put(second)

        val list = listOf(TestJson("1"), TestJson("2"))

        val array = list.toJsonArray()

        assertEquals(expected.toString(), array.toString())
    }

    @Test
    fun `should convert json array to list`() {
        val first = JSONObject()
        first.put("value", "1")

        val second = JSONObject()
        second.put("value", "2")

        val array = JSONArray()
        array.put(first)
        array.put(second)

        val list = array.toList { TestJson(getJSONObject(it)) }

        assertEquals(listOf(TestJson("1"), TestJson("2")), list)
    }

    @Suppress("unused")
    companion object {

        @JvmStatic
        fun jsonObjects(): Stream<Arguments> = Stream.of(
            Arguments.of(JSONObject("{}"), null),
            Arguments.of(JSONObject("""{"root": null}"""), null),
            Arguments.of(JSONObject("""{"root": {}}"""), JSONObject("{}"))
        )

        @JvmStatic
        fun doubles(): Stream<Arguments> = Stream.of(
            Arguments.of(JSONObject("{}"), null),
            Arguments.of(JSONObject("""{"value": null}"""), null),
            Arguments.of(JSONObject("""{"value": 42.0}"""), 42.0)
        )

        @JvmStatic
        fun longs(): Stream<Arguments> = Stream.of(
            Arguments.of(JSONObject("{}"), null),
            Arguments.of(JSONObject("""{"value": null}"""), null),
            Arguments.of(JSONObject("""{"value": 42}"""), 42L)
        )

        @JvmStatic
        fun booleans(): Stream<Arguments> = Stream.of(
            Arguments.of(JSONObject("{}"), null),
            Arguments.of(JSONObject("""{"value": null}"""), null),
            Arguments.of(JSONObject("""{"value": true}"""), true)
        )

        @JvmStatic
        fun strings(): Stream<Arguments> = Stream.of(
            Arguments.of(JSONObject("{}"), null),
            Arguments.of(JSONObject("""{"value": null}"""), null),
            Arguments.of(JSONObject("""{"value": "result"}"""), "result")
        )
    }

    enum class TestEnum(override val code: String) : EnumWithCode {
        TEST("test")
    }
}
