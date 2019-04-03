package com.aang23.globaltab;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.cedarsoftware.util.io.JsonWriter;

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

        configfile.put("header", "&4Welcome, &6%username%&4 on");
        configfile.put("footer", "&7the server !");
        configfile.put("updatedelay", "1");
        configfile.put("player_format", "%prefix% %username%");
        configfile.put("customtabsenabled", true);

        List<String> customTabs = new ArrayList<String>();
        customTabs.add("&3Your ping : &e%ping%");
        customTabs.add("&3Current server : &e%server%");
        customTabs.add("&3Balance : &e%balance%");

        configfile.put("customtabs", customTabs);

        PrintWriter pw = new PrintWriter(GlobalTab.configspath.toString() + "/globaltab.json");
        pw.write(JsonWriter.formatJson(configfile.toJSONString()));

        pw.flush();
        pw.close();
    }
}