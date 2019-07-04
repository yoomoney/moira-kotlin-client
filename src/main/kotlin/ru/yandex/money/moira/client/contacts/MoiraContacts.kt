package ru.yandex.money.moira.client.contacts

import org.json.JSONException
import org.json.JSONObject
import ru.yandex.money.moira.client.http.HttpClient
import ru.yandex.money.moira.client.http.HttpException
import ru.yandex.money.moira.client.http.HttpRequest
import ru.yandex.money.moira.client.json.toList

/**
 * Remote collection of contacts stored in Moira.
 *
 * Uses [httpClient] to make HTTP calls.
 *
 * **Developers note**: this implementation is designed only for managing contacts using simple HTTP calls. If you need,
 * for example, caching, then you have to implement this functionality in a separate class using the Decorator pattern.
 * If you need complex logic that can be built on the existing API methods, for example, check that there is no contact
 * with the same identifier before create a new one, then you need to implement it as an extension method.
 */
class MoiraContacts(private val httpClient: HttpClient) : Contacts {

    /**
     * Returns a list of all existing contacts stored in Moira.
     *
     * For each contact from the resulting list, the [Contact.id] is required.
     *
     * Throws [JSONException] if at least one fetched contact does not have the specified [Contact.id] property.
     * Throws [HttpException] if Moira responded with a non-200 status code.
     */
    override fun all(): List<Contact> {
        val request = HttpRequest(path = "/contact")
        val response = httpClient.get(request)
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to fetch all contacts")
        }

        val json = JSONObject(response.body)
        return json.getJSONArray("list").toList {
            val contact = Contact(getJSONObject(it))
            if (contact.id == null) {
                throw JSONException("ID is required for fetched contact: $contact")
            }
            contact
        }
    }

    /**
     * Creates a new [contact] and returns its id received from Moira.
     *
     * For this operation, the [Contact.id] is optional. In case it is not specified, then Moira will generate a random
     * identifier by itself.
     *
     * Note that if the [Contact.id] is specified, then no checks for the existence of a contact with the same
     * identifier will be made. It is assumed that such checks will be made before calling this method.
     *
     * This operation uses authorization in Moira. So, Moira will save information about the user who created this
     * contact. See [Security page](https://moira.readthedocs.io/en/latest/installation/security.html) for authorization
     * details.
     *
     * Throws [HttpException] if Moira responded with a non-200 status code.
     */
    override fun create(contact: Contact): String {
        val json = contact.toJson()
        val request = HttpRequest(path = "/contact", body = json.toString())
        val response = httpClient.put(request)
        if (response.status != 200) {
            throw HttpException(request, response, "Failed to create new contact: contact=$contact")
        }

        val jsonResponse = JSONObject(response.body)
        return jsonResponse.getString("id")
    }

    /**
     * Updates an existing [contact]. Returns `true` if the update was successful. Update is considered successful if
     * the server responded with 200 status code. Returns `false` otherwise.
     *
     * For this operation, the [Contact.id] is required. In case it is not specified, the [IllegalArgumentException]
     * will be thrown.
     *
     * Note that no checks for the existence of a contact with the same identifier will be made. It is assumed that
     * such checks will be made before calling this method.
     *
     * This operation uses authorization in Moira. So, a user performing this operation may not have enough permissions
     * to edit this contact. See [Security page](https://moira.readthedocs.io/en/latest/installation/security.html) for
     * authorization details.
     */
    override fun update(contact: Contact): Boolean {
        if (contact.id == null) {
            throw IllegalArgumentException("Given contact has no id: contact=$contact")
        }

        val json = contact.toJson()
        val request = HttpRequest(path = "/contact/${contact.id}", body = json.toString())
        val response = httpClient.put(request)

        return response.status == 200
    }

    /**
     * Deletes an existing contact by the given [id]. Returns `true` if the removal was successful. Removal is
     * considered successful if the server responded with 200 status code. Returns `false` otherwise.
     *
     * Note that no checks for the existence of a contact with the same identifier will be made. It is assumed that
     * such checks will be made before calling this method.
     *
     * This operation uses authorization in Moira. So, a user performing this operation may not have enough permissions
     * to delete this contact. See [Security page](https://moira.readthedocs.io/en/latest/installation/security.html)
     * for authorization details.
     */
    override fun delete(id: String): Boolean {
        val response = httpClient.delete(HttpRequest(path = "/contact/$id"))
        return response.status == 200
    }

    /**
     * Sends a test notification to an existing contact with the given [id]. Returns `true` if the notification was sent
     * successfully. Test notification is considered to be successfully sent if the server responded with 200 status
     * code. Returns `false` otherwise
     *
     * This is a common case when you want to test a newly created contact:
     * ```kotlin
     * val id = contacts.create(contact = Contact(...))
     * contacts.sendTestNotification(id)
     * ```
     *
     * Note that no checks for the existence of a contact with the same identifier will be made. It is assumed that
     * such checks will be made before calling this method.
     *
     * This operation uses authorization in Moira. So, a user performing this operation may not have enough permissions
     * to sent test notifications on this contact.
     * See [Security page](https://moira.readthedocs.io/en/latest/installation/security.html) for authorization details.
     */
    override fun sendTestNotification(id: String): Boolean {
        val response = httpClient.post(HttpRequest(path = "/contact/$id/test"))
        return response.status == 200
    }
}
