package com.vjcspy.spring.tedbed;

import org.jetbrains.annotations.NotNull;

public class TedbedHelper {
    @NotNull
    public static String getSystemInfo() {
        return String.format(
                "OS: %s, Version: %s, Arch: %s",
                System.getProperty("os.name"),
                System.getProperty("os.version"),
                System.getProperty("os.arch")
        );
    }
}