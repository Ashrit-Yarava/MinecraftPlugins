package io.github.ashrityarava.compass.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class CommandKit implements CommandExecutor {

    Map<Player, Location> trackerList = new TreeMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {

            Player player = (Player) sender;

            if(args.length > 0 && !args[0].equals("reset")) {
                if(Bukkit.getPlayer(args[0]) != null) {
                    trackerList.put(player, Objects.requireNonNull(Bukkit.getPlayer(args[0])).getLocation());
                    player.sendMessage(ChatColor.GREEN + "Now tracking " + args[0]);
                } else {
                    player.sendMessage(ChatColor.RED + "Player is offline.");
                }
            } else if(args.length > 0) {
                trackerList.put(player, Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
            } else {
                player.sendMessage(ChatColor.GRAY + "Usage: /compass <player to track>, /compass reset");
            }

        } else {
            Bukkit.getLogger().info("Only players can run that command.");
        }
        return true;
    }

    public void track(Player sender) {
        sender.setCompassTarget(trackerList.get(sender));
    }

}
