package ru.yoomoney.tech.moira.client.subscriptions

import ru.yoomoney.tech.moira.client.enum.EnumWithCode

/**
 * The theme of plot that can be sent with notification.
 */
enum class PlotTheme(override val code: String) : EnumWithCode {

    /**
     * Light theme.
     */
    LIGHT("light"),

    /**
     * Dark theme.
     */
    DARK("dark")
}
