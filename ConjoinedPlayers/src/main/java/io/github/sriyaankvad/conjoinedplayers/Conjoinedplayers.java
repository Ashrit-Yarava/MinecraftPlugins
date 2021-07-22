package io.github.sriyaankvad.conjoinedplayers;

import io.github.sriyaankvad.conjoinedplayers.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Conjoinedplayers extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Minecraft Randomizer: A Plugin for Minecraft 1.171 by Sriyaank Vadlamani");

        CommandKit kit = new CommandKit();
        this.getCommand("conjoinplayers").setExecutor(kit);
        RestrictedListener[] listeners = {
                new TwoPlayers(), new ThreePlayers(), new FourPlayers(), new FivePlayers()
        };

        final syncInventories[] syncer = new syncInventories[1];
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if(kit.enableListeners) {
                getServer().getPluginManager().registerEvents(listeners[kit.players.size()-2], this);
                syncer[0] = new syncInventories(kit.players);
                kit.enableListeners = false;
            }

            if(kit.disableListeners) {
                getServer().getPluginManager().disablePlugin(this);
                kit.disableListeners = false;
            }
        }, 0, 20L);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, syncer[0]::checkForSyncInventory, 0, 1);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Thank you for using this plugin");
    }
}
