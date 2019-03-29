package com.aang23.globaltab;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

public class ConfigManager {
    public static JSONObject config = null;

    public static void setupConfig() {
        if (!GlobalTab.configspath.toFile().exists())
            GlobalTab.configspath.toFile().mkdirs();
        if (!new File(GlobalTab.configspath.toString() + "/globaltab.json").exists()) {
            try {
                writeInitialConfig();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        Object configobj = null;
        try {
            configobj = new JSONParser().parse(new FileReader(GlobalTab.configspath.toString() + "/globaltab.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        config = (JSONObject) configobj;
    }

    private static void writeInitialConfig() throws FileNotFoundException {
        JSONObject configfile = new JSONObject();

        configfile.put("header", "&4This is a header");
        configfile.put("footer", "&7This is a footer");
        configfile.put("updatedelay", "1");
        configfile.put("player_format", "%prefix% %username%");

        PrintWriter pw = new PrintWriter(GlobalTab.configspath.toString() + "/globaltab.json");
        pw.write(configfile.toJSONString());

        pw.flush();
        pw.close();
    }
}