@file:Suppress("TooManyFunctions")
package ru.yandex.money.moira.client.json

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import ru.yandex.money.moira.client.enum.EnumWithCode
import ru.yandex.money.moira.client.enum.enumByCode

/**
 * Puts new [value] that associated by the given [key].
 *
 * This method is an alias for [JSONObject.put] method.
 */
operator fun JSONObject.set(key: String, value: Any?) {
    put(key, value)
}

/**
 * Returns `true` if this [JSONObject] has non-null value mapped by the given [key].
 */
fun JSONObject.hasNonNull(key: String): Boolean = has(key) && !isNull(key)

/**
 * Returns [JSONObject] if this [JSONObject] has non-null value mapped by the given [key]. Returns `null` otherwise.
 */
fun JSONObject.getJsonObjectOrNull(key: String): JSONObject? = if (hasNonNull(key)) {
    getJSONObject(key)
} else {
    null
}

/**
 * Returns [Double] value if this [JSONObject] has non-null value mapped by the given [key]. Returns `null` otherwise.
 */
fun JSONObject.getDoubleOrNull(key: String): Double? = if (hasNonNull(key)) {
    getDouble(key)
} else {
    null
}

/**
 * Returns [Long] value if this [JSONObject] has non-null value mapped by the given [key]. Returns `null` otherwise.
 */
fun JSONObject.getLongOrNull(key: String): Long? = if (hasNonNull(key)) {
    getLong(key)
} else {
    null
}

/**
 * Returns [Boolean] value if this [JSONObject] has non-null value mapped by the given [key]. Returns `null` otherwise.
 */
fun JSONObject.getBooleanOrNull(key: String): Boolean? = if (hasNonNull(key)) {
    getBoolean(key)
} else {
    null
}

/**
 * Returns [String] value if this [JSONObject] has non-null value mapped by the given [key]. Returns `null` otherwise.
 */
fun JSONObject.getStringOrNull(key: String): String? = if (hasNonNull(key)) {
    getString(key)
} else {
    null
}

/**
 * Returns [JSONArray] value if this [JSONObject] has non-null value mapped by the given [key].
 * Returns `null` otherwise.
 */
fun JSONObject.getJsonArrayOrNull(key: String): JSONArray? = if (hasNonNull(key)) {
    getJSONArray(key)
} else {
    null
}

/**
 * Returns an enum constant associated with value that mapped by the given [key].
 * If there is no value mapped by the given [key] in this [JSONObject] then [JSONException] will be thrown.
 * If there is no enum constant associated with value that mapped by the given [key] in [E] then [JSONException] will be
 * thrown.
 */
inline fun <reified E> JSONObject.getEnumByCode(key: String): E
    where E : Enum<E>,
          E : EnumWithCode {
    val code = getString(key)
    return enumByCode(code) ?: throw JSONException("Unknown code: class=${E::class} code=$code")
}

/**
 * Embeds [other] [JSONObject] to this [JSONObject].
 *
 * Note that this [JSONObject] will be mutated after calling this method.
 *
 * Note that if some key from [other] exists in this [JSONObject] too then value in this [JSONObject] will be
 * overwritten by value from [other].
 *
 * Example of embedding:
 * ```
 * JSONObject a = JSONObject("""{"key1": "val1", "key2": "val2"}""")
 * JSONObject b = JSONObject("""{"key2": "val3", "key4": "val4"}""")
 * a.embed(b) // a = {"key1": "val1", "key2": "val3", "key4": "val4"}
 * ```
 */
fun JSONObject.embed(other: JSONObject) {
    other.keySet().forEach { this.put(it, other[it]) }
}

/**
 * Return [JSONArray] representation of this [List] of [JsonSerializable].
 * Order of elements will be preserved.
 *
 * To get element of [JSONArray] [JsonSerializable.toJson] method will be called.
 */
fun List<JsonSerializable>.toJsonArray(): JSONArray {
    val array = JSONArray()
    this.forEach { array.put(it.toJson()) }
    return array
}

/**
 * Return [List] representation of this [JSONArray].
 * Order of elements will be preserved.
 *
 * [itemFactory] is used to get new element of result list.
 */
fun <T> JSONArray.toList(itemFactory: JSONArray.(Int) -> T): List<T> {
    val items = ArrayList<T>(length())
    for (i in 0 until length()) {
        items += itemFactory(i)
    }
    return items
}
