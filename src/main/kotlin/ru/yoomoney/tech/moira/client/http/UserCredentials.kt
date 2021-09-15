package ru.yoomoney.tech.moira.client.http

import java.util.Base64

/**
 * The credentials of current Moira user.
 */
class UserCredentials(private val username: String, private val password: String) {

    /**
     * Encodes credentials to Base64 string with format: `username:password`
     */
    fun encode(): String = Base64.getEncoder().encodeToString("$username:$password".toByteArray())
}
