package com.aang23.globaltab;

import java.util.List;
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

					TabListEntry currentEntry = TabListEntry
							.builder().profile(currentPlayer.getGameProfile()).displayName(TabBuilder
									.formatPlayerTab((String) ConfigManager.config.get("player_format"), currentPlayer))
							.tabList(currentPlayerToProcess.getTabList()).build();

					currentPlayerToProcess.getTabList().removeEntry(currentPlayer.getUniqueId());
					currentPlayerToProcess.getTabList().addEntry(currentEntry);
				}

				if ((boolean) ConfigManager.config.get("customtabsenabled")) {
					List<String> customtabs = (List<String>) ConfigManager.config.get("customtabs");

					for (int i3 = 0; i3 < customtabs.size(); i3++) {
						GameProfile tabProfile = GameProfile.forOfflinePlayer("customTab" + String.valueOf(i3));

						TabListEntry currentEntry = TabListEntry.builder().profile(tabProfile)
								.displayName(TabBuilder.formatCustomTab(customtabs.get(i3), currentPlayerToProcess))
								.tabList(currentPlayerToProcess.getTabList()).build();

						currentPlayerToProcess.getTabList().removeEntry(tabProfile.getId());
						currentPlayerToProcess.getTabList().addEntry(currentEntry);
					}
				}

				currentPlayerToProcess.getTabList().setHeaderAndFooter(
                TabBuilder.formatCustomTab((String) ConfigManager.config.get("header"), currentPlayerToProcess),
                TabBuilder.formatCustomTab((String) ConfigManager.config.get("footer"), currentPlayerToProcess));
			}
		}
	}
}