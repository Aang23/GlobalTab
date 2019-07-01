package com.aang23.globaltab;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.player.TabList;
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

		try {
			ProxyServer server = GlobalTab.server;
			if (server.getPlayerCount() > 0) {
				for (Player currentPlayerToProcess : server.getAllPlayers()) {
					TabList tabList = currentPlayerToProcess.getTabList();

					if (ConfigManager.isServerAllowed(currentPlayerToProcess.getCurrentServer())) {

						List<UUID> toKeep = new ArrayList<>();

						for (int i2 = 0; i2 < server.getPlayerCount(); i2++) {
							Player currentPlayer = (Player) server.getAllPlayers().toArray()[i2];

							TabListEntry currentEntry = TabListEntry.builder().profile(currentPlayer.getGameProfile())
									.displayName(TabBuilder.formatPlayerTab(
											(String) ConfigManager.config.get("player_format"), currentPlayer))
									.tabList(tabList).build();

							GlobalTab.insertIntoTabListCleanly(tabList, currentEntry,
									toKeep);
						}

						if (ConfigManager.customTabsEnabled()) {
							List<String> customtabs = ConfigManager.getCustomTabs();

							for (int i3 = 0; i3 < customtabs.size(); i3++) {
								GameProfile tabProfile = GameProfile.forOfflinePlayer("customTab" + i3);

								TabListEntry currentEntry = TabListEntry.builder().profile(tabProfile)
										.displayName(
												TabBuilder.formatCustomTab(customtabs.get(i3), currentPlayerToProcess))
										.tabList(tabList).build();

								GlobalTab.insertIntoTabListCleanly(tabList, currentEntry,
										toKeep);
							}
						}

						for (TabListEntry current : tabList.getEntries()) {
							if (!toKeep.contains(current.getProfile().getId()))
								tabList.removeEntry(current.getProfile().getId());
						}

						tabList.setHeaderAndFooter(
								TabBuilder.formatCustomTab((String) ConfigManager.config.get("header"),
										currentPlayerToProcess),
								TabBuilder.formatCustomTab((String) ConfigManager.config.get("footer"),
										currentPlayerToProcess));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}