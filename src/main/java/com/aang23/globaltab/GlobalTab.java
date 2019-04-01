package com.aang23.globaltab;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import java.nio.file.Path;
import java.util.Timer;

@Plugin(id = "globaltab", name = "GlobalTab", version = "1.0", description = "A plugin", authors = { "Aang23" })
public class GlobalTab {
    public static ProxyServer server;
    public static Logger logger;
    public static Path configspath;
    public static LuckPermsApi luckpermsapi;

    @Inject
    public GlobalTab(ProxyServer lserver, CommandManager commandManager, EventManager eventManager, Logger llogger,
            @DataDirectory Path configpaths) {

        server = lserver;
        logger = llogger;
        configspath = configpaths;
        logger.info("Loading GlobalTab");

        ConfigManager.setupConfig();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerHandler(),
                Integer.parseInt((String) ConfigManager.config.get("updatedelay")) * 1000,
                Integer.parseInt((String) ConfigManager.config.get("updatedelay")) * 1000);
    }

    @Subscribe
    public void onInitialization(ProxyInitializeEvent event) {
        if (GlobalTab.server.getPluginManager().isLoaded("luckperms"))
            luckpermsapi = LuckPerms.getApi();
    }

    @Subscribe
    public void onLeave(DisconnectEvent event) {
        if (server.getPlayerCount() > 0) {
            for (int i = 0; i < server.getPlayerCount(); i++) {
                Player currentPlayerToProcess = (Player) server.getAllPlayers().toArray()[i];
                currentPlayerToProcess.getTabList().removeEntry(event.getPlayer().getUniqueId());
            }
        }
    }
}
