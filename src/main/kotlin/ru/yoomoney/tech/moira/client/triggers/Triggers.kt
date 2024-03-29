package ru.yoomoney.tech.moira.client.triggers

/**
 * An abstraction of triggers collection that stored in Moira backend.
 *
 * @author Dmitry Komarov [komarovdmitry@yamoney.ru]
 * @since 15.02.2019
 */
interface Triggers {

    /**
     * Returns all triggers.
     */
    fun fetchAll(): List<Trigger>

    /**
     * Return trigger by given [id] or null if trigger not found in Moira.
     */
    fun fetchById(id: String): Trigger?

    /**
     * Creates new **trigger** in Moira and returns **id** of newly created trigger.
     *
     * You can generate [Trigger.id] by yourself and set it to any non-null value. In such case this **id** will be send
     * to Moira and this method will return that **id**.
     *
     * You can leave [Trigger.id] as `null` and Moira will generate **id** by itself. This method will return **id**
     * generated by Moira.
     */
    fun create(trigger: Trigger): String

    /**
     * Updates already existing **trigger** by **id** in Moira. Returns `true` if update was successful.
     *
     * If [Trigger.id] property is missed in [trigger] then [IllegalArgumentException] will be thrown.
     */
    fun update(trigger: Trigger): Boolean

    /**
     * Deletes trigger by given id.
     *
     * Returns true if trigger was deleted successfully.
     */
    fun delete(id: String): Boolean
}
