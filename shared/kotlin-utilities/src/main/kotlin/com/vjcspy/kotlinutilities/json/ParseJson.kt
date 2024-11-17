package com.vjcspy.kotlinutilities.json

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.*

private val logger = KotlinLogging.logger {}

/**
 * Parses a JSON string into native Kotlin types
 * @param jsonString The JSON string to parse
 * @return The parsed result as native Kotlin types, or null if parsing fails
 */
@Throws(IllegalArgumentException::class)
fun parseJson(jsonString: String): Any? =
    try {
        val jsonElement = Json.parseToJsonElement(jsonString)
        parseJsonValue(jsonElement)
    } catch (e: Exception) {
        logger.error(e) { "Parsing json error" }
        null
    }

/**
 * Recursively parses JsonElement into native Kotlin types
 * @param element The JsonElement to parse
 * @return The parsed value as a native Kotlin type
 */
private fun parseJsonValue(element: JsonElement): Any? =
    when (element) {
        is JsonObject -> element.mapValues { parseJsonValue(it.value) }
        is JsonArray -> element.map { parseJsonValue(it) }
        is JsonPrimitive ->
            when {
                element.isString -> element.content
                element.booleanOrNull != null -> element.boolean
                element.intOrNull != null -> element.int
                element.doubleOrNull != null -> element.double
                else -> null
            }
    }
