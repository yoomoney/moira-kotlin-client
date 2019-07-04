package ru.yandex.money.moira.client.patterns

/**
 * A collection of graphite patterns.
 *
 * Instances of this interface should be used to manage events.
 */
interface Patterns {

    /**
     * Fetches all patterns.
     */
    fun fetchAll(): List<Pattern>

    /**
     * Deletes [pattern]. Returns `true` if [pattern] deleted successfully.
     */
    fun delete(pattern: String): Boolean
}
