package com.aang23.globaltab;

import com.velocitypowered.api.proxy.Player;

import net.kyori.text.TextComponent;
import net.kyori.text.serializer.ComponentSerializers;

public class TabBuilder {
    public static TextComponent formatTab(String raw, Player player) {

        raw = raw.replace("%username%", player.getUsername());
        raw = raw.replace("%prefix%", UserInfoGetter.getPrefixFromUsername(player.getUsername()));
        raw = raw.replace("%suffix%", UserInfoGetter.getSuffixFromUsername(player.getUsername()));
        raw = raw.replace("%server%", player.getCurrentServer().get().getServerInfo().getName());

        return ComponentSerializers.LEGACY.deserialize(raw, '&');
    }
}