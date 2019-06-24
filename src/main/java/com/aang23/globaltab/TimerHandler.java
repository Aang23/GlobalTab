package com.aang23.globaltab;

import java.util.List;
import java.util.TimerTask;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.velocitypowered.api.util.GameProfile;

public class TimerHandler extends TimerTask {

	public static boolean stop = false;

	@Override
	public void run() {
		if (stop) {
			this.cancel();
			stop = false;
		}

		ProxyServer server = GlobalTab.server;
		if (server.getPlayerCount() > 0) {
			for (Player currentPlayerToProcess : server.getAllPlayers()) {
				if (ConfigManager.isServerAllowed(currentPlayerToProcess.getCurrentServer())) {

					for (TabListEntry current : currentPlayerToProcess.getTabList().getEntries())
						currentPlayerToProcess.getTabList().removeEntry(current.getProfile().getId());

					for (int i2 = 0; i2 < server.getPlayerCount(); i2++) {
						Player currentPlayer = (Player) server.getAllPlayers().toArray()[i2];

						TabListEntry currentEntry = TabListEntry.builder().profile(currentPlayer.getGameProfile())
								.displayName(TabBuilder.formatPlayerTab(
										(String) ConfigManager.config.get("player_format"), currentPlayer))
								.tabList(currentPlayerToProcess.getTabList()).build();

						// Just to make sure...
						if (!currentPlayerToProcess.getTabList().getEntries().contains(currentEntry)) {
							currentPlayerToProcess.getTabList().removeEntry(currentEntry.getProfile().getId());
							currentPlayerToProcess.getTabList().addEntry(currentEntry);
						}
					}

					if (ConfigManager.customTabsEnabled()) {
						List<String> customtabs = ConfigManager.getCustomTabs();

						for (int i3 = 0; i3 < customtabs.size(); i3++) {
							GameProfile tabProfile = GameProfile.forOfflinePlayer("customTab" + String.valueOf(i3));

							TabListEntry currentEntry = TabListEntry.builder().profile(tabProfile)
									.displayName(TabBuilder.formatCustomTab(customtabs.get(i3), currentPlayerToProcess))
									.tabList(currentPlayerToProcess.getTabList()).build();

							// Just to make sure...
							if (!currentPlayerToProcess.getTabList().getEntries().contains(currentEntry)) {
								currentPlayerToProcess.getTabList().removeEntry(currentEntry.getProfile().getId());
								currentPlayerToProcess.getTabList().addEntry(currentEntry);
							}
						}
					}

					currentPlayerToProcess.getTabList().setHeaderAndFooter(
							TabBuilder.formatCustomTab((String) ConfigManager.config.get("header"),
									currentPlayerToProcess),
							TabBuilder.formatCustomTab((String) ConfigManager.config.get("footer"),
									currentPlayerToProcess));
				}
			}
		}
	}
}