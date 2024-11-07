package com.vjcspy.spring.base.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Env {
    private final Map<String, String> variables = new HashMap<>();

    public Env(List<String> fileNames, String directory) {
        for (String fileName : fileNames) {
            Dotenv dotenv = Dotenv.configure()
                    .directory(directory)
                    .filename(fileName)
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            // Merge variables from this dotenv file
            for (DotenvEntry entry : dotenv.entries()) {
                // Only add the key if it does not already exist in the map
                variables.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }
    }

    // Method to get a variable by key
    public String get(String key) {
        return variables.get(key);
    }

    // Method to get all entries
    public Set<Map.Entry<String, String>> entries() {
        return variables.entrySet();
    }
}
