package ru.yandex.money.moira.client.tags

/**
 * An abstraction of tags collection.
 *
 * Tags are used for categorize triggers in Moira and to subscribe on it.
 * Tags may be used for searching at Moira triggers screen.
 *
 * More information about [tags](https://moira.readthedocs.io/en/latest/user_guide/simple.html#tags).
 */
interface Tags {

    /**
     * Fetches all available tags.
     */
    fun fetchAll(): List<String>

    /**
     * Fetches all available tags with triggers and subscriptions.
     */
    fun fetchAllStats(): List<TagStatistics>

    /**
     * Deletes existing tag. Return `true` if delete was successful.
     */
    fun delete(tag: String): Boolean
}
