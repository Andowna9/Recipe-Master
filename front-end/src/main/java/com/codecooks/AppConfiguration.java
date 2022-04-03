package com.codecooks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Utility class for saving app configuration.
 * It currently uses Java Properties to store key/value pairs in a file.
 */
public class AppConfiguration {

    private static Properties props = new Properties();
    private static final String FILE_PATH = "config.xml";

    public static void loadConfig() {

        try {

            File file = new File(FILE_PATH);
            if (file.exists()) {
                InputStream in = new FileInputStream(file);
                props.loadFromXML(in);
            }
        } catch (IOException e) {

            System.err.println("Error reading " + FILE_PATH);
            e.printStackTrace();
        }
    }

    public static Object getConfig(String key, String defaultValue) {

        return props.getOrDefault(key, defaultValue);
    }

    public static void addConfig(String key, String value) {

        props.put(key, value);
        saveConfig();
    }

    public static void removeConfig(String key) {

        props.remove(key);
        saveConfig();
    }

    private static void saveConfig() {

        try {

            File configFile = new File(FILE_PATH);
            FileOutputStream out = new FileOutputStream(configFile);
            props.storeToXML(out, "Application Configuration", StandardCharsets.UTF_8);

        } catch (IOException e) {

            System.err.println("Error saving app configuration data to " + FILE_PATH);
            e.printStackTrace();
        }
    }
}
