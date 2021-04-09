package com.mouradelamrani.salaam.extensions

import com.google.gson.JsonObject

/**
 * Convenience method to get the specified member as a [String].
 * Return null is case of any exception.
 */
fun JsonObject.getString(memberName: String): String? = try {
    if (has(memberName)) getAsJsonPrimitive(memberName).asString
    else null
} catch (e: Exception) {
    null
}

/**
 * Convenience method to get the specified member as a [Long].
 * Return null is case of any exception.
 */
fun JsonObject.getLong(memberName: String): Long? = try {
    if (has(memberName)) getAsJsonPrimitive(memberName).asLong
    else null
} catch (e: Exception) {
    null
}

/**
 * Convenience method to get the specified member as a [Int].
 * Return null is case of any exception.
 */
fun JsonObject.getInt(memberName: String): Int? = try {
    if (has(memberName)) getAsJsonPrimitive(memberName).asInt
    else null
} catch (e: Exception) {
    null
}

/**
 * Convenience method to get the specified member as a [Boolean].
 * Return null is case of any exception.
 */
fun JsonObject.getBoolean(memberName: String): Boolean? = try {
    if (has(memberName)) getAsJsonPrimitive(memberName).asBoolean
    else null
} catch (e: Exception) {
    null
}

fun JsonObject.getJsonObject(memberName: String): JsonObject? = try {
    if (has(memberName)) getAsJsonObject(memberName)
    else null
} catch (e: Exception) {
    null
}
