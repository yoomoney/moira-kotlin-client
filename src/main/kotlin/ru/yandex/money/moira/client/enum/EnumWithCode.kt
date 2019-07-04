package ru.yandex.money.moira.client.enum

/**
 * Represents an enum with some unique [String] value assigned to each constant.
 *
 * For example, [code] property can be used for serialization purpose.
 */
interface EnumWithCode {

    /**
     * Value that assigned to this enum constant.
     *
     * Each value should be unique across single enum class.
     */
    val code: String
}

/**
 * Returns an enum constant of given enum class with concrete [code] value is assigned to.
 *
 * If no enum constant has been found by [code] value then *null* will be returned.
 */
inline fun <reified E> enumByCode(code: String): E?
        where E : Enum<E>,
              E : EnumWithCode {
    val values = E::class.java.enumConstants
    for (value in values) {
        if (value.code == code) {
            return value
        }
    }
    return null
}
