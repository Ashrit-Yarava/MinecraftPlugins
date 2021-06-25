package io.github.ashrityarava.compass;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Compass extends JavaPlugin {

    Logger logger = Bukkit.getLogger();

    @Override
    public void onEnable() {
        // Plugin startup logic

        printStartup();



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void printStartup() {
        logger.info("\n  ____                                    \n" +
                " / ___|___  _ __ ___  _ __   __ _ ___ ___ \n" +
                "| |   / _ \\| '_ ` _ \\| '_ \\ / _` / __/ __|\n" +
                "| |__| (_) | | | | | | |_) | (_| \\__ \\__ \\\n" +
                " \\____\\___/|_| |_| |_| .__/ \\__,_|___/___/\n" +
                "                     |_|                  \n");
        logger.info("-----------------------------------------------\n");
        logger.info("A plugin by: Ashrit Yarava (https://ashrit-yarava.github.io/plugins/compass)");
    }

}
