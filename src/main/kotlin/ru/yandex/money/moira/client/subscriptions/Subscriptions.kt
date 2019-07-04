package ru.yandex.money.moira.client.subscriptions

import ru.yandex.money.moira.client.http.MoiraHttpClient

/**
 * An abstraction of collection of subscriptions.
 *
 * See more information about [subscriptions](https://moira.readthedocs.io/en/latest/user_guide/subscriptions.html).
 */
interface Subscriptions {

    /**
     * Fetches all available subscriptions. If your Moira installation is configured with separate user accounts,
     * only your subscriptions will be fetched.
     *
     * See [MoiraHttpClient.login] property.
     */
    fun fetchAll(): List<Subscription>

    /**
     * Creates a new [subscription] in Moira and returns id of newly created [subscription].
     *
     * If [Subscription.id] is not `null` then subscription will be created with this id. Otherwise, Moira will generate
     * id of new subscription by itself.
     */
    fun create(subscription: Subscription): String

    /**
     * Updates existing [subscription] in Moira. Returns `true` if subscription was updated successfully.
     *
     * If [Subscription.id] is `null` then [IllegalArgumentException] will be thrown.
     */
    fun update(subscription: Subscription): Boolean

    /**
     * Deletes existing subscription in Moira by given [id]. Returns `true` if subscription was deleted successfully.
     */
    fun delete(id: String): Boolean

    /**
     * Sends test notification for existing subscription in Moira by given subscription [id]. Returns `true` if test
     * notification was sent successfully.
     */
    fun sendTestNotification(id: String): Boolean
}
