package com.aang23.globaltab;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.cedarsoftware.util.io.JsonWriter;
import com.velocitypowered.api.proxy.ServerConnection;

public class ConfigManager {

    public GlobalTab plugin;

    public JSONObject config = null;

    private List<String> disabledServers = new ArrayList<>();
    private List<String> customTabs = new ArrayList<>();

    public ConfigManager(GlobalTab plugin) {
        this.plugin = plugin;
    }

    public void setupConfig() {
        if (!plugin.configspath.toFile().exists())
            plugin.configspath.toFile().mkdirs();
        if (!new File(plugin.configspath.toString() + "/globaltab.json").exists()) {
            try {
                writeInitialConfig();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        Object configobj = null;
        try {
            configobj = new JSONParser().parse(new FileReader(plugin.configspath.toString() + "/globaltab.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        config = (JSONObject) configobj;

        disabledServers = (List<String>) plugin.configManager.config.get("disabled_servers");
        customTabs = (List<String>) plugin.configManager.config.get("customtabs");
    }

    private void writeInitialConfig() throws FileNotFoundException {
        JSONObject configfile = new JSONObject();

        configfile.put("header", "&4Welcome, &6%username%&4 on");
        configfile.put("footer", "&7the server !");
        configfile.put("updatedelay", "1");
        configfile.put("player_format", "%prefix% %username%");
        configfile.put("customtabsenabled", true);

        customTabs.clear();
        customTabs.add("&3Your ping : &e%ping%");
        customTabs.add("&3Current server : &e%server%");
        customTabs.add("&3Balance : &e%balance%");
        configfile.put("customtabs", customTabs);

        disabledServers.clear();
        disabledServers.add("thisserverwonthavethetab");
        configfile.put("disabled_servers", disabledServers);

        PrintWriter pw = new PrintWriter(plugin.configspath.toString() + "/globaltab.json");
        pw.write(JsonWriter.formatJson(configfile.toJSONString()));

        pw.flush();
        pw.close();
    }

    public boolean isServerAllowed(Optional<ServerConnection> server) {
        String name;
        if (server.isPresent())
            name = server.get().getServerInfo().getName();
        else
            return false;

        if (disabledServers != null)
            return !disabledServers.contains(name);
        else
            return true;
    }

    public List<String> getCustomTabs() {
        return customTabs;
    }

    public boolean customTabsEnabled() {
        return (boolean) plugin.configManager.config.get("customtabsenabled");
    }
}