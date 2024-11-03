package com.vjcspy.tedbed;

public class TedbedHelper {
    public static String getMessage() {
        return "Hello from TedbedHelper!";
    }

    public static void printInfo(String message) {
        System.out.println("TedbedHelper Info: " + message);
    }

    public static String getSystemInfo() {
        return String.format(
                "OS: %s, Version: %s, Arch: %s",
                System.getProperty("os.name"),
                System.getProperty("os.version"),
                System.getProperty("os.arch")
        );
    }
}