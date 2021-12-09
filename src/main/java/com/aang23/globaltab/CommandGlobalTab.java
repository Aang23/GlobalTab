package com.aang23.globaltab;

import java.util.Timer;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandGlobalTab implements SimpleCommand {

    private GlobalTab plugin;

    public CommandGlobalTab(GlobalTab plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length > 0) {
            if (args[0].equals("restart")) {
                plugin.timerHandler.stop = true;

                Timer timer = new Timer();
                timer.scheduleAtFixedRate(plugin.timerHandler,
                        Integer.parseInt((String) plugin.configManager.config.get("updatedelay")) * 1000,
                        Integer.parseInt((String) plugin.configManager.config.get("updatedelay")) * 1000);

                source.sendMessage(Component.text("Restarted the tab !", NamedTextColor.GREEN));
            } else if (args[0].equals("config")) {
                plugin.configManager.setupConfig();

                source.sendMessage(Component.text("Reloaded the configuration file !", NamedTextColor.GREEN));
            }
        } else
            source.sendMessage(Component.text("Usage : /globaltab restart/config", NamedTextColor.RED));
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("globaltab.admin");
    }
}