package com.aang23.globaltab;

import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class TabBuilder {

    private GlobalTab plugin;

    public TabBuilder(GlobalTab plugin) {
        this.plugin = plugin;
    }

    public Component formatPlayerTab(String raw, Player player) {

        raw = raw.replace("%username%", player.getUsername());
        raw = raw.replace("%prefix%", plugin.userInfoGetter.getPrefixFromUsername(player.getUsername()));
        raw = raw.replace("%suffix%", plugin.userInfoGetter.getSuffixFromUsername(player.getUsername()));
        raw = raw.replace("%server%", getCurrentServer(player));

        return LegacyComponentSerializer.legacyAmpersand().deserialize(raw);
    }

    public Component formatCustomTab(String raw, Player player) {

        raw = raw.replace("%username%", player.getUsername());
        raw = raw.replace("%prefix%", plugin.userInfoGetter.getPrefixFromUsername(player.getUsername()));
        raw = raw.replace("%suffix%", plugin.userInfoGetter.getSuffixFromUsername(player.getUsername()));
        raw = raw.replace("%server%", getCurrentServer(player));
        raw = raw.replace("%ping%", String.valueOf(player.getPing()));
        raw = raw.replace("%playercount%", String.valueOf(plugin.server.getPlayerCount()));
        raw = raw.replace("%localplayercount%", getServerPlayerCount(player));
        raw = raw.replace("%totalmaxplayer%", String.valueOf(plugin.server.getConfiguration().getShowMaxPlayers()));
        raw = raw.replace("%motd%", plugin.server.getConfiguration().getMotd().toString());
        raw = raw.replace("%uuid%", player.getUniqueId().toString());
        raw = raw.replace("%ip%", player.getRemoteAddress().toString());
        raw = raw.replace("%balance%", getBalance(player));

        return LegacyComponentSerializer.legacyAmpersand().deserialize(raw);
    }

    private String getCurrentServer(Player player) {
        if (player.getCurrentServer().isPresent())
            return player.getCurrentServer().get().getServerInfo().getName();
        else
            return "null";
    }

    private String getBalance(Player player) {
        if (plugin.playerBalances.containsKey(player.getUsername()))
            return String.valueOf(plugin.playerBalances.get(player.getUsername()));
        else
            return "null";
    }

    private String getServerPlayerCount(Player player) {
        if (player.getCurrentServer().isPresent())
            return String.valueOf(player.getCurrentServer().get().getServer().getPlayersConnected().size());
        else
            return "null";
    }
}