package com.aang23.globaltab;

import java.util.TimerTask;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.velocitypowered.api.util.GameProfile;

public class TimerHandler extends TimerTask {
	@Override
	public void run() {
		ProxyServer server = GlobalTab.server;
		if (server.getPlayerCount() > 0) {
			for (int i = 0; i < server.getPlayerCount(); i++) {
				Player currentPlayerToProcess = (Player) server.getAllPlayers().toArray()[i];

				for (int i2 = 0; i2 < server.getPlayerCount(); i2++) {
					Player currentPlayer = (Player) server.getAllPlayers().toArray()[i2];

					GameProfile currentProfile = GameProfile.forOfflinePlayer(currentPlayer.getUsername())
							.withId(currentPlayer.getUniqueId())
							.withProperties(currentPlayer.getGameProfileProperties());

					TabListEntry currentEntry = TabListEntry
							.builder().profile(currentProfile).displayName(TabBuilder
									.formatTab((String) ConfigManager.config.get("player_format"), currentPlayer))
							.tabList(currentPlayerToProcess.getTabList()).build();

					currentPlayerToProcess.getTabList().removeEntry(currentProfile.getId());
					currentPlayerToProcess.getTabList().addEntry(currentEntry);
				}
			}
		}
	}
}