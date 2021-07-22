package io.github.sriyaankvad.conjoinedplayers;

import io.github.sriyaankvad.conjoinedplayers.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Conjoinedplayers extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Minecraft Randomizer: A Plugin for Minecraft 1.17.1 by Sriyaank Vadlamani");

        CommandKit kit = new CommandKit();
        this.getCommand("conjoinplayers").setExecutor(kit);
        RestrictedListener[] listeners = {
                new TwoPlayers(), new ThreePlayers(), new FourPlayers(), new FivePlayers()
        };

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if(kit.isEnabled) {
                getServer().getPluginManager().registerEvents(listeners[kit.players.size()-2], this);
                syncInventories syncer = new syncInventories(kit.players);
                syncer.checkForSyncInventory();
            }
        }, 0, 20L);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Thank you for using this plugin");
    }
}
