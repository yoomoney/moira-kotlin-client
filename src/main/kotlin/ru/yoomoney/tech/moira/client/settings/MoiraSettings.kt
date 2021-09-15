package ru.yoomoney.tech.moira.client.settings

import org.apache.http.impl.client.HttpClientBuilder
import ru.yoomoney.tech.moira.client.http.UserCredentials

/**
 * Settings of Moira client.
 *
 * @property baseUrl the base URL to Moira API
 * @property login the optional login that used for authorization purpose
 * @property credentials the optional user credentials
 * @property httpClientConfigurator the configurator that used to configure Apache HTTP client
 */
data class MoiraSettings(
    val baseUrl: String,
    val login: String? = null,
    val credentials: UserCredentials? = null,
    private val httpClientConfigurator: (HttpClientBuilder) -> Unit = {}
) {

    fun configure(httpClientBuilder: HttpClientBuilder) {
        httpClientConfigurator(httpClientBuilder)
    }
}
