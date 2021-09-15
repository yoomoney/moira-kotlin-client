package ru.yoomoney.tech.moira.client.triggers.expression

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.yoomoney.tech.moira.client.fileContent
import ru.yoomoney.tech.moira.client.shouldSerializeTo
import ru.yoomoney.tech.moira.client.triggers.TriggerType
import java.util.stream.Stream

class TriggerExpressionTest {

    @ParameterizedTest
    @MethodSource("expressions")
    fun `should make trigger expression from json correctly`(json: JSONObject, expression: TriggerExpression) {
        val actual = TriggerExpression.makeTriggerExpression(json)

        assertEquals(expression, actual)
    }

    @ParameterizedTest
    @MethodSource("expressions")
    fun `should serialize to json correctly`(json: JSONObject, expression: TriggerExpression) {
        expression shouldSerializeTo json.toString()
    }

    @ParameterizedTest
    @MethodSource("incorrectExpressions")
    fun `should throw an exception when wrong json given`(json: JSONObject, message: String) {
        val exception = assertThrows<RuntimeException> { TriggerExpression.makeTriggerExpression(json) }

        assertEquals(message, exception.message)
    }

    @Suppress("unused")
    companion object {

        @JvmStatic
        fun expressions(): Stream<Arguments> = Stream.of(
            Arguments.of(JSONObject(fileContent("falling_with_type.json")), SimpleExpression(TriggerType.FALLING, 20.0, 10.0)),
            Arguments.of(JSONObject(fileContent("falling_with_type_and_warn_only.json")), SimpleExpression(TriggerType.FALLING, 20.0, null)),
            Arguments.of(JSONObject(fileContent("falling_with_type_and_error_only.json")), SimpleExpression(TriggerType.FALLING, null, 10.0)),
            Arguments.of(JSONObject(fileContent("rising_with_type.json")), SimpleExpression(TriggerType.RISING, 10.0, 20.0)),
            Arguments.of(JSONObject(fileContent("rising_with_type_and_warn_only.json")), SimpleExpression(TriggerType.RISING, 10.0, null)),
            Arguments.of(JSONObject(fileContent("rising_with_type_and_error_only.json")), SimpleExpression(TriggerType.RISING, null, 20.0)),
            Arguments.of(JSONObject(fileContent("advanced_with_type.json")), AdvancedExpression("OK"))
        )

        @JvmStatic
        fun incorrectExpressions(): Stream<Arguments> = Stream.of(
            Arguments.of(JSONObject(fileContent("incorrect_falling.json")), "WARN threshold cannot be less than ERROR threshold when trigger is FALLING"),
            Arguments.of(JSONObject(fileContent("incorrect_rising.json")), "WARN threshold cannot be greater than ERROR threshold when trigger is RISING"),
            Arguments.of(JSONObject(), "Unavailable to parse trigger expression: {}")
        )
    }
}
