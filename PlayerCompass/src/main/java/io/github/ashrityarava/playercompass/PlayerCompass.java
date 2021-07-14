package io.github.ashrityarava.playercompass;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.map.MinecraftFont;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Logger;

public final class PlayerCompass extends JavaPlugin {

    Trackers list = new Trackers();
    Logger logger = Bukkit.getLogger();
    FileConfiguration config;

    @Override
    public void onEnable() {

        config = this.getConfig();

        // Load the previous locations.
        list.loadLocations(config);



        // Register the /compass command.
        this.getCommand("compass").setExecutor(new CommandKit());

        // Register the Player Join Event Listener.
        Bukkit.getServer().getPluginManager().registerEvents(new CompassListener(), this);

        // Code to update the compass upon a certain number of ticks.
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            list.updateCompass();
        }, 0L, 20);
    }

    @Override
    public void onDisable() {

        list.saveLocations(config);
        logger.info("Thanks for using the plugin.");
        saveConfig();

    }

    public class CommandKit implements CommandExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(args.length == 1) {
                    if(args[0].equals("give")) {
                        Trackers.givePlayerCompass(player);
                    } else {
                        Player target = Bukkit.getPlayer(args[0]);
                        if(target == null)
                            player.sendMessage(ChatColor.DARK_RED + "Player does not exist.");
                        else
                            list.changeTarget(player, target);
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    public class CompassListener implements Listener {

        @EventHandler(priority = EventPriority.HIGH)
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            list.addPlayer(player);
        }

    }

}
