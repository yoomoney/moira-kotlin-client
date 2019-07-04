package ru.yandex.money.moira.client.health

/**
 * Notifier health state.
 *
 * Instances of this interface  should be used to manage notifier state.
 *
 * It is recommended to use methods of this interface when something bad happened to your Moira's environment. For
 * example, it may be Graphite Relay failure or Moira Redis-DB breakdown. In this case you have to disable notifier via
 * [Health.disableNotifications] method call to prevent Moira from sending alert messages to end users but will notify
 * administrators of the existing problem.
 */
interface Health {

    /**
     * Fetches the current state of the notifier.
     */
    fun fetchCurrentState(): NotifierState

    /**
     * Enable notifier to send notifications.
     */
    fun enableNotifications()

    /**
     * Disable notifier to send notifications.
     */
    fun disableNotifications()
}
