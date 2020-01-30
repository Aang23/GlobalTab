package com.aang23.globaltab;

import java.util.Timer;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

public class CommandGlobalTab implements Command {

    @Override
    public void execute(@NonNull CommandSource source, String[] args) {
        if (args.length > 0) {
            if (args[0].equals("restart")) {
                TimerHandler.stop = true;

                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerHandler(),
                        Integer.parseInt((String) ConfigManager.config.get("updatedelay")) * 1000,
                        Integer.parseInt((String) ConfigManager.config.get("updatedelay")) * 1000);

                source.sendMessage(TextComponent.of("Restarted the tablist updater.").color(TextColor.GREEN));
            } else if (args[0].equals("config")) {
                ConfigManager.setupConfig();

                source.sendMessage(TextComponent.of("Reloaded the configuration file.").color(TextColor.GREEN));
            }
        } else {
            source.sendMessage(TextComponent.of("Usage : /globaltab <restart/config>").color(TextColor.RED));
        }
    }
}