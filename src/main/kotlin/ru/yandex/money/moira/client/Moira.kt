package ru.yandex.money.moira.client

import org.apache.http.impl.client.HttpClients
import ru.yandex.money.moira.client.contacts.MoiraContacts
import ru.yandex.money.moira.client.events.MoiraEvents
import ru.yandex.money.moira.client.health.MoiraHealth
import ru.yandex.money.moira.client.http.HttpClient
import ru.yandex.money.moira.client.http.MoiraHttpClient
import ru.yandex.money.moira.client.http.apache.ApacheHttpClient
import ru.yandex.money.moira.client.notifications.MoiraNotifications
import ru.yandex.money.moira.client.patterns.MoiraPatterns
import ru.yandex.money.moira.client.settings.MoiraSettings
import ru.yandex.money.moira.client.subscriptions.MoiraSubscriptions
import ru.yandex.money.moira.client.tags.MoiraTags
import ru.yandex.money.moira.client.triggers.MoiraTriggers

/**
 * A Moira client.
 */
class Moira(private val httpClient: HttpClient) {

    constructor(settings: MoiraSettings) : this(createClient(settings))

    val triggers by lazy { MoiraTriggers(httpClient) }

    val contacts by lazy { MoiraContacts(httpClient) }

    val events by lazy { MoiraEvents(httpClient) }

    val health by lazy { MoiraHealth(httpClient) }

    val notifications by lazy { MoiraNotifications(httpClient) }

    val patterns by lazy { MoiraPatterns(httpClient) }

    val subscriptions by lazy { MoiraSubscriptions(httpClient) }

    val tags by lazy { MoiraTags(httpClient) }

    companion object {

        @JvmStatic
        private fun createClient(settings: MoiraSettings): HttpClient {
            val builder = HttpClients.custom()
            settings.configure(builder)

            val client = ApacheHttpClient(builder.build(), settings.baseUrl)

            return MoiraHttpClient(
                delegate = client,
                login = settings.login,
                credentials = settings.credentials
            )
        }
    }
}
