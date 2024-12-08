package com.vjcspy.spring.base.config

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvEntry

class Env(
    fileNames: List<String>,
    directory: String
) {
    private val variables: MutableMap<String, String> = mutableMapOf()

    init {
        fileNames.forEach { fileName ->
            val dotenv = Dotenv.configure()
                .directory(directory)
                .filename(fileName)
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .systemProperties()
                .load()

            // Merge variables from this dotenv file
            dotenv.entries().forEach { entry: DotenvEntry ->
                // Only add the key if it does not already exist in the map
                variables.put(entry.key, entry.value)
            }
        }
    }

    // Method to get a variable by key
    fun get(key: String): String? = variables[key]

    // Method to get all entries
    fun entries(): Set<Map.Entry<String, String>> = variables.entries
}
