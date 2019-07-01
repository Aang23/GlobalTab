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
import net.kyori.text.TextComponent;

public class TimerHandler extends TimerTask {

	static boolean stop = false;

	@Override
	public void run() {
		if (stop) {
			this.cancel();
			stop = false;
		}

		ProxyServer server = GlobalTab.server;

		if (server.getPlayerCount() > 0) {
			for (Player currentPlayer : server.getAllPlayers()) {
				TabList tabList = currentPlayer.getTabList();

				if (!ConfigManager.isServerAllowed(currentPlayer.getCurrentServer())) {
					continue;
				}

				List<UUID> toKeep = new ArrayList<>();

				// Real players
				for (Player tabPlayer : server.getAllPlayers()) {
					String playerFormat = (String) ConfigManager.config.get("player_format");

					TabListEntry currentEntry = TabListEntry.builder()
							.profile(tabPlayer.getGameProfile())
							.displayName(TabBuilder.formatPlayerTab(playerFormat, tabPlayer))
							.tabList(tabList).build();

					GlobalTab.insertIntoTabListCleanly(tabList, currentEntry, toKeep);
				}

				// Fake players
				if (ConfigManager.customTabsEnabled()) {
					List<String> customtabs = ConfigManager.getCustomTabs();

					for (int i3 = 0; i3 < customtabs.size(); i3++) {
						GameProfile tabProfile = GameProfile.forOfflinePlayer("customTab" + i3);

						TabListEntry currentEntry = TabListEntry.builder()
								.profile(tabProfile)
								.displayName(TabBuilder.formatCustomTab(customtabs.get(i3), currentPlayer))
								.tabList(tabList).build();

						GlobalTab.insertIntoTabListCleanly(tabList, currentEntry, toKeep);
					}
				}

				// Update entries in tab list
				for (TabListEntry current : tabList.getEntries()) {
					if (!toKeep.contains(current.getProfile().getId()))
						tabList.removeEntry(current.getProfile().getId());
				}

				// Tab list header
				String header = (String) ConfigManager.config.get("header");
				TextComponent headerComponent = TabBuilder.formatCustomTab(header, currentPlayer);

				// Tab list footer
				String footer = (String) ConfigManager.config.get("footer");
				TextComponent footerComponent = TabBuilder.formatCustomTab(footer, currentPlayer);

				// Set tab list headers and footers
				tabList.setHeaderAndFooter(headerComponent, footerComponent);
			}
		}
	}
}