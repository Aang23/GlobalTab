package com.aang23.globaltab;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ConfigManager {
    public static Map<String, String> config = new HashMap<String, String>();

    public static void setupConfig() {
        if (!GlobalTab.configspath.toFile().exists())
            GlobalTab.configspath.toFile().mkdirs();

        // Configure settings
        config.put("header", "&4This is a header");
        config.put("footer", "&7This is a footer");
        config.put("updatedelay", "1");
        config.put("player_format", "%prefix% %username%");

        // Write & load config
        File f = new File(GlobalTab.configspath.toString() + "/config.txt");
        if (f.exists() && !f.isDirectory()) {
            Map<String, String> fileContent = new HashMap<String, String>();
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(GlobalTab.configspath.toString() + "/config.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (String key : properties.stringPropertyNames()) {
                fileContent.put(key, properties.get(key).toString());
            }
            config = fileContent;
        } else {
            Map<String, String> fileContent = config;
            Properties properties = new Properties();
            for (Map.Entry<String, String> entry : fileContent.entrySet()) {
                properties.put(entry.getKey(), entry.getValue());
            }
            try {
                properties.store(new FileOutputStream(GlobalTab.configspath.toString() + "/config.txt"), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}