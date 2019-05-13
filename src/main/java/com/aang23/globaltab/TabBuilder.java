package com.aang23.globaltab;

import com.velocitypowered.api.proxy.Player;

import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

public class TabBuilder {
    public static TextComponent formatPlayerTab(String raw, Player player) {

        raw = raw.replace("%username%", player.getUsername());
        raw = raw.replace("%prefix%", UserInfoGetter.getPrefixFromUsername(player.getUsername()));
        raw = raw.replace("%suffix%", UserInfoGetter.getSuffixFromUsername(player.getUsername()));
        raw = raw.replace("%server%", getCurrentServer(player));

        return LegacyComponentSerializer.INSTANCE.deserialize(raw, '&');
    }

    public static TextComponent formatCustomTab(String raw, Player player) {

        raw = raw.replace("%username%", player.getUsername());
        raw = raw.replace("%prefix%", UserInfoGetter.getPrefixFromUsername(player.getUsername()));
        raw = raw.replace("%suffix%", UserInfoGetter.getSuffixFromUsername(player.getUsername()));
        raw = raw.replace("%server%", getCurrentServer(player));
        raw = raw.replace("%ping%", String.valueOf(player.getPing()));
        raw = raw.replace("%playercount%", String.valueOf(GlobalTab.server.getPlayerCount()));
        raw = raw.replace("%localplayercount%", String.valueOf(player.getCurrentServer().get().getServer().getPlayersConnected().size()));
        raw = raw.replace("%totalmaxplayer%", String.valueOf(GlobalTab.server.getConfiguration().getShowMaxPlayers())); //Not accurate ! To change soon.
        raw = raw.replace("%motd%", GlobalTab.server.getConfiguration().getMotdComponent().toString());
        raw = raw.replace("%uuid%", player.getUniqueId().toString());
        raw = raw.replace("%ip%", player.getRemoteAddress().toString());
        raw = raw.replace("%balance%", getBalance(player));

        return LegacyComponentSerializer.INSTANCE.deserialize(raw, '&');
    }

    private static String getCurrentServer(Player player) {
        if (player.getCurrentServer().isPresent())
            return player.getCurrentServer().get().getServerInfo().getName();
        else
            return "null";
    }

    private static String getBalance(Player player) {
        if (GlobalTab.playerBalances.containsKey(player.getUsername()))
            return String.valueOf(GlobalTab.playerBalances.get(player.getUsername()));
        else
            return "null";
    }
}