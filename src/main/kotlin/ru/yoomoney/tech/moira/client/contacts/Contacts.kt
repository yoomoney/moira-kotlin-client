package ru.yoomoney.tech.moira.client.contacts

/**
 * A collection of contacts that can be used in subscriptions for notifications delivery.
 *
 * Instances of this interface should be used to manage contacts.
 */
interface Contacts {

    /**
     * Returns a list of all existing contacts.
     *
     * For each contact from the resulting list, the [Contact.id] is required.
     */
    fun all(): List<Contact>

    /**
     * Creates a new [contact] and returns its id.
     *
     * For this operation, the [Contact.id] is optional. In case it is not specified, then Moira will generate a random
     * identifier by itself.
     *
     * Note that if the [Contact.id] is specified, then it is not guaranteed that this method will check for the
     * existence of a contact with the same identifier. The behavior in this case depends on the concrete
     * implementation. It is assumed that such checks will be made before calling this method.
     *
     * This operation uses authorization in Moira. So, Moira will save information about the user who created this
     * contact. See [Security page](https://moira.readthedocs.io/en/latest/installation/security.html) for authorization
     * details.
     */
    fun create(contact: Contact): String

    /**
     * Updates an existing [contact]. Returns `true` if the update was successful.
     *
     * For this operation, the [Contact.id] is required. In case it is not specified, the [IllegalArgumentException]
     * will be thrown.
     *
     * Note that it is not guaranteed that this method will check for the existence of contact with the same identifier.
     * The behavior in this case depends on the concrete implementation. It is assumed that such checks will be made
     * before calling this method.
     *
     * This operation uses authorization in Moira. So, a user performing this operation may not have enough permissions
     * to edit this contact. See [Security page](https://moira.readthedocs.io/en/latest/installation/security.html) for
     * authorization details.
     */
    fun update(contact: Contact): Boolean

    /**
     * Deletes an existing contact by the given [id]. Returns `true` if the removal was successful.
     *
     * Note that it is not guaranteed that this method will check for the existence of contact with the same identifier.
     * The behavior in this case depends on the concrete implementation. It is assumed that such checks will be made
     * before calling this method.
     *
     * This operation uses authorization in Moira. So, a user performing this operation may not have enough permissions
     * to delete this contact. See [Security page](https://moira.readthedocs.io/en/latest/installation/security.html)
     * for authorization details.
     */
    fun delete(id: String): Boolean

    /**
     * Sends a test notification to an existing contact with the given [id]. Returns `true` if the notification was sent
     * successfully.
     *
     * This is a common case when you want to test a newly created contact:
     * ```kotlin
     * val id = contacts.create(contact = Contact(...))
     * contacts.sendTestNotification(id)
     * ```
     *
     * Note that it is not guaranteed that this method will check for the existence of contact with the same identifier.
     * The behavior in this case depends on the concrete implementation. It is assumed that such checks will be made
     * before calling this method.
     *
     * This operation uses authorization in Moira. So, a user performing this operation may not have enough permissions
     * to sent test notifications on this contact.
     * See [Security page](https://moira.readthedocs.io/en/latest/installation/security.html) for authorization details.
     */
    fun sendTestNotification(id: String): Boolean
}
