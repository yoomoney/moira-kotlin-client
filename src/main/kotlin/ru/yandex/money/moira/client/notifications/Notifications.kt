package ru.yandex.money.moira.client.notifications

/**
 * A collection of notifications that should be sent. This collection contains notifications that not yet sent.
 *
 * Instances of this interface should be used to manage notifications.
 */
interface Notifications {

    /**
     * Fetches notifications by given ids range. Range represented by [start] and [end] `long` values. If [end] equals
     * to `-1` then range becomes half open interval.
     */
    fun fetchAll(start: Long = 0, end: Long = -1): NotificationList

    /**
     * Deletes notification by given [id]. Returns `true` if delete was successful.
     */
    fun delete(id: String): Boolean

    /**
     * Deletes all notifications. Returns `true` if delete was successful.
     */
    fun deleteAll(): Boolean
}
