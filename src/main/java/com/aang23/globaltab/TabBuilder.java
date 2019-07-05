package com.aang23.globaltab;

import com.velocitypowered.api.proxy.Player;

import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

public class TabBuilder {
    public static TextComponent formatPlayerTab(String raw, Player player) {
        raw = raw.replace("%username%", player.getUsername())
                .replace("%prefix%", UserInfoGetter.getPrefixFromUsername(player.getUsername()))
                .replace("%suffix%", UserInfoGetter.getSuffixFromUsername(player.getUsername()))
                .replace("%server%", getCurrentServer(player));

        return LegacyComponentSerializer.legacy().deserialize(raw, '&');
    }

    public static TextComponent formatCustomTab(String raw, Player player) {
        raw = raw.replace("%username%", player.getUsername())
                .replace("%prefix%", UserInfoGetter.getPrefixFromUsername(player.getUsername()))
                .replace("%suffix%", UserInfoGetter.getSuffixFromUsername(player.getUsername()))
                .replace("%server%", getCurrentServer(player))
                .replace("%ping%", String.valueOf(player.getPing()))
                .replace("%playercount%", String.valueOf(GlobalTab.server.getPlayerCount()))
                .replace("%localplayercount%", getServerPlayerCount(player))
                .replace("%totalmaxplayer%", String.valueOf(GlobalTab.server.getConfiguration().getShowMaxPlayers()))
                .replace("%motd%", GlobalTab.server.getConfiguration().getMotdComponent().toString())
                .replace("%uuid%", player.getUniqueId().toString())
                .replace("%ip%", player.getRemoteAddress().toString())
                .replace("%balance%", getBalance(player));

        return LegacyComponentSerializer.legacy().deserialize(raw, '&');
    }

    private static String getCurrentServer(Player player) {
        if (player.getCurrentServer().isPresent()) {
            return player.getCurrentServer().get().getServerInfo().getName();
        } else {
            return "null";
        }
    }

    private static String getBalance(Player player) {
        if (GlobalTab.playerBalances.containsKey(player.getUsername())) {
            return String.valueOf(GlobalTab.playerBalances.get(player.getUsername()));
        } else {
            return "0";
        }
    }

    private static String getServerPlayerCount(Player player) {
        if (player.getCurrentServer().isPresent()) {
            return String.valueOf(player.getCurrentServer().get().getServer().getPlayersConnected().size());
        } else {
            return "0";
        }
    }
}