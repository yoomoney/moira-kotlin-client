package ru.yoomoney.tech.moira.client.events

import ru.yoomoney.tech.moira.client.pagination.Page
import ru.yoomoney.tech.moira.client.pagination.Paginated

/**
 * A collection of events that emitted by the concrete trigger. This collection represents events history of trigger.
 *
 * Instances of this interface should be used to manage events.
 */
interface Events {

    /**
     * Fetches events that emitted by a trigger with a given [triggerId]. Events list is paginated by a given [page].
     *
     * Note that it is not guaranteed that this method will check for the existence of a trigger with the same
     * [triggerId]. The behavior in this case depends on the concrete implementation. It is assumed that such checks
     * will be made before calling this method.
     */
    fun fetchByTriggerId(triggerId: String, page: Page = Page(0, 100)): Paginated<Event>

    /**
     * Deletes all events history for every trigger. Returns `true` if the removal was successful.
     */
    fun deleteAll(): Boolean
}
