package io.github.sriyaankvad.minecraftrandomizer.minecraftrandomizer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftRandomizer extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Minecraft Randomizer: A Plugin for Minecraft 1.17 by Sriyaank Vadlamani");
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Thank you for using this plugin");
    }
}
