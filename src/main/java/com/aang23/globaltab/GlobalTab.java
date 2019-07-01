package com.aang23.globaltab;

import com.google.common.io.ByteArrayDataInput;
import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.player.TabListEntry;

import org.slf4j.Logger;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;

@Plugin(id = "globaltab", name = "GlobalTab", version = "1.0", description = "A plugin", authors = { "Aang23" })
public class GlobalTab {
    public static ProxyServer server;
    public static Logger logger;
    public static Path configspath;
    public static LuckPermsApi luckpermsapi;

    public static final Map<String, Double> playerBalances = new HashMap<>();

    @Inject
    public GlobalTab(ProxyServer lserver, CommandManager commandManager, Logger llogger, @DataDirectory Path configpaths) {
        server = lserver;
        logger = llogger;
        configspath = configpaths;
        logger.info("Loading GlobalTab");

        ConfigManager.setupConfig();

        commandManager.register(new CommandGlobalTab(), "globaltab");

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerHandler(),
                Integer.parseInt((String) ConfigManager.config.get("updatedelay")) * 1000,
                Integer.parseInt((String) ConfigManager.config.get("updatedelay")) * 1000);
    }

    @Subscribe
    public void onInitialization(ProxyInitializeEvent event) {
        if (GlobalTab.server.getPluginManager().isLoaded("luckperms")) {
            luckpermsapi = LuckPerms.getApi();
        }
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

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        server.getChannelRegistrar().register(new LegacyChannelIdentifier("globaltab:sync"));
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(new LegacyChannelIdentifier("globaltab:sync"))) {
            return;
        }

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        if (!(event.getSource() instanceof ServerConnection)) {
            return;
        }

        ByteArrayDataInput in = event.dataAsDataStream();
        String subChannel = in.readUTF();

        if (subChannel.equals("Balance")) {
            String[] packet = in.readUTF().split(":");
            String username = packet[0];
            Double balance = Double.parseDouble(packet[1]);

            if (playerBalances.containsKey(username)) {
                playerBalances.replace(username, balance);
            } else {
                playerBalances.put(username, balance);
            }
        }
    }

    public static void insertIntoTabListCleanly(TabList list, TabListEntry entry, List<UUID> toKeep) {
        UUID inUUID = entry.getProfile().getId();
        List<UUID> containedUUIDs = new ArrayList<>();
        Map<UUID, TabListEntry> cache = new HashMap<>();

        for (TabListEntry current : list.getEntries()) {
            containedUUIDs.add(current.getProfile().getId());
            cache.put(current.getProfile().getId(), current);
        }

        if (!containedUUIDs.contains(inUUID)) {
            list.addEntry(entry);
            return;
        } else {
            TabListEntry currentEntry = cache.get(inUUID);
            if (!currentEntry.getDisplayName().equals(entry.getDisplayName())) {
                list.removeEntry(inUUID);
                list.addEntry(entry);
            }
        }

        toKeep.add(inUUID);
    }
}
