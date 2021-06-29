package io.github.sriyaankvad.advancementrace;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class AdvancementRace extends JavaPlugin {

    private int remainingseconds;

    @Override
    public void onEnable() {
        getLogger().info("Advancement Race: A Plugin for Minecraft 1.16.5 by Sriyaank Vadlamani");

        CommandKit kit = new CommandKit();
        this.getCommand("advancementrace").setExecutor(kit);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if(kit.handler.gameInProgress) {
                if (remainingseconds <= 10 && remainingseconds > 0) {
                    kit.handler.broadcast(ChatColor.RED + "" + remainingseconds + " seconds left");
                    remainingseconds--;
                } else if (remainingseconds == 0) {
                    kit.handler.endRound();
                    if(!kit.handler.gameInProgress)
                        return;
                    kit.handler.init();
                    remainingseconds = kit.time;
                } else {
                    remainingseconds--;
                }
            }
            else {
                remainingseconds = kit.time;
            }
        }, 0L, 20L);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            kit.handler.checkAdvancementCompletion();
        }, 0, 2L);

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Thank you for using this plugin");
    }
}
