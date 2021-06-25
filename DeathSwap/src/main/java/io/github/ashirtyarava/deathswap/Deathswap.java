package io.github.ashirtyarava.deathswap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;


public final class Deathswap extends JavaPlugin {

    private int seconds;
    private int remainingSeconds;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Print the startup message.
        Logger logger = getLogger();
        logger.info("\n ____             _   _     ____                     \n" +
                "|  _ \\  ___  __ _| |_| |__ / ___|_      ____ _ _ __  \n" +
                "| | | |/ _ \\/ _` | __| '_ \\\\___ \\ \\ /\\ / / _` | '_ \\ \n" +
                "| |_| |  __/ (_| | |_| | | |___) \\ V  V / (_| | |_) |\n" +
                "|____/ \\___|\\__,_|\\__|_| |_|____/ \\_/\\_/ \\__,_| .__/ \n" +
                "                                              |_|    \n");
        logger.info("-----------------------------------------------\n");
        logger.info("A plugin by: Ashrit Yarava (https://ashrit-yarava.github.io/plugins/deathswap/)");

        // Set up the executor for the plugin.
        CommandKit kit = new CommandKit();
        Objects.requireNonNull(this.getCommand("deathswap")).setExecutor(kit);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if(kit.handler.gameInProgress) if (remainingSeconds <= 10 && remainingSeconds > 0) {
                kit.printTime(remainingSeconds);
                remainingSeconds--;
            } else if (remainingSeconds == 0) {
                kit.teleport();
                remainingSeconds = seconds;
            } else {
                remainingSeconds--;
            }
            else {
                seconds = kit.time;
                remainingSeconds = seconds;
            }
        }, 0L, 20);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            kit.handler.removeDead();
            if(kit.handler.players.size() == 1 && kit.handler.gameInProgress) {
                kit.handler.gameInProgress = false;
                kit.handler.players.get(0).sendMessage(ChatColor.GREEN + "You have won the DeathSwap game!");
                kit.handler.players.clear();
				kit.handler.gameInProgress = false;
            }
        }, 0, 10);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Thanks for using this plugin.");
    }
}
