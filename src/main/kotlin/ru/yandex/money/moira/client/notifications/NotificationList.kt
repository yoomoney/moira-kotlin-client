package ru.yandex.money.moira.client.notifications

/**
 * A list of [notifications] with additional information about the [total] number of notifications.
 */
data class NotificationList(val total: Long, val notifications: List<Notification>)
