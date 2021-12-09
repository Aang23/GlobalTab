package com.aang23.globaltab;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.velocitypowered.api.util.GameProfile;

public class TimerHandler extends TimerTask {

	private GlobalTab plugin;

	public boolean stop = false;

	public TimerHandler(GlobalTab plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		if (stop) {
			this.cancel();
			stop = false;
		}

		try {
			ProxyServer server = plugin.server;
			if (server.getPlayerCount() > 0) {
				for (Player currentPlayerToProcess : server.getAllPlayers()) {
					if (plugin.configManager.isServerAllowed(currentPlayerToProcess.getCurrentServer())) {

						List<UUID> toKeep = new ArrayList<>();

						for (int i2 = 0; i2 < server.getPlayerCount(); i2++) {
							Player currentPlayer = (Player) server.getAllPlayers().toArray()[i2];

							TabListEntry currentEntry = TabListEntry.builder().profile(currentPlayer.getGameProfile())
									.displayName(plugin.tabBuilder.formatPlayerTab(
											(String) plugin.configManager.config.get("player_format"), currentPlayer))
									.tabList(currentPlayerToProcess.getTabList()).build();

							plugin.insertIntoTabListCleanly(currentPlayerToProcess.getTabList(), currentEntry,
									toKeep);
						}

						if (plugin.configManager.customTabsEnabled()) {
							List<String> customtabs = plugin.configManager.getCustomTabs();

							for (int i3 = 0; i3 < customtabs.size(); i3++) {
								GameProfile tabProfile = GameProfile.forOfflinePlayer("customTab" + i3);

								TabListEntry currentEntry = TabListEntry.builder().profile(tabProfile)
										.displayName(
												plugin.tabBuilder.formatCustomTab(customtabs.get(i3), currentPlayerToProcess))
										.tabList(currentPlayerToProcess.getTabList()).build();

								plugin.insertIntoTabListCleanly(currentPlayerToProcess.getTabList(), currentEntry,
										toKeep);
							}
						}

						for (TabListEntry current : currentPlayerToProcess.getTabList().getEntries()) {
							if (!toKeep.contains(current.getProfile().getId()))
								currentPlayerToProcess.getTabList().removeEntry(current.getProfile().getId());
						}
						currentPlayerToProcess.sendPlayerListHeaderAndFooter(plugin.tabBuilder.formatCustomTab((String) plugin.configManager.config.get("header"),
										currentPlayerToProcess),
								plugin.tabBuilder.formatCustomTab((String) plugin.configManager.config.get("footer"),
										currentPlayerToProcess));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}