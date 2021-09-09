package ru.yoomoney.tech.moira.client.utils

/**
 * Returns `null` if current string is blank.
 */
fun String.omitEmpty(): String? = if (isNotBlank()) {
    this
} else {
    null
}
