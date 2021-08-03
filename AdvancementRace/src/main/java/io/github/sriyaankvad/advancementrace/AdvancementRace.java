package io.github.sriyaankvad.advancementrace;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Advancementrace extends JavaPlugin {

    int remainingseconds;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Advancement Race: A Plugin for Minecraft 1.17.1 by Sriyaank Vadlamani");

        CommandKit kit = new CommandKit();
        this.getCommand("advancementrace").setExecutor(kit);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if(kit.gameInProgress) {
                if (remainingseconds <= 10 && remainingseconds > 0) {
                    kit.broadcast(ChatColor.RED + "" + remainingseconds + " seconds left");
                    remainingseconds--;
                } else if (remainingseconds == 0) {
                    kit.endRound();
                    if(!kit.gameInProgress)
                        return;
                    kit.init();
                    remainingseconds = kit.time;
                } else {
                    remainingseconds--;
                }
            } else {
                remainingseconds = kit.time;
            }
        }, 0L, 20L);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            kit.checkAdvancementCompletion();
        }, 0, 2L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Thanks for using this plugin");
    }
}
