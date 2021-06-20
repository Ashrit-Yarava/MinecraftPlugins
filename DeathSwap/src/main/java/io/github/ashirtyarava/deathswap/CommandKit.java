package io.github.ashirtyarava.deathswap;

import io.github.ashirtyarava.deathswap.plugin.Handler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Objects;

public class CommandKit implements CommandExecutor {

    Handler handler = new Handler();

    public CommandKit() {
        handler = new Handler();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Length Check, Print usage if wrong.
        if(sender instanceof Player ) {
            Player player = (Player) sender;
            // Print usage if no arguments are given.
            if(args.length == 0) {
                printUsage((Player) sender);
                return true;
            }
            // Add Player Command.
            if(args[0].equalsIgnoreCase("addPlayer") && args.length > 1) {
                if(Bukkit.getPlayer(args[1]) != null) {
                    if(handler.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(args[1])))) {
                        player.sendMessage(args[1] + " has been added to the game.");
                        Objects.requireNonNull(Bukkit.getPlayer(args[1])).sendMessage("You have been added to DeathSwap!");
                    } else {
                        player.sendMessage(args[1] + " has already been added.");
                    }
                } else {
                    player.sendMessage(args[1] + " was not found.");
                }
                return true;
            } else if(args[0].equalsIgnoreCase("list")) {
                // List playing players.
                player.sendMessage(handler.listPlayers());
                return true;
            } else if(args[0].equalsIgnoreCase("removePlayer") && args.length > 1) {
                if(Bukkit.getPlayer(args[1]) != null) {
                    if(handler.removePlayer(Bukkit.getPlayer(args[1]))) {
                        player.sendMessage(args[1] + " has been remove from the game.");
                        Bukkit.getPlayer(args[1]).sendMessage("You have been removed from DeathSwap.");
                    }
                }
            }
        } else {
            // only players should be able to run this command.
            Bukkit.getLogger().warning("Only players can execute this command!");
        }
        return true;
    }

    private static void printUsage(Player player) {
        player.sendMessage("Usage:\n" +
                "/deathswap addPlayer (playername) - Adds a player to the game.\n" +
                "/deathswap removePlayer (playername) - Removes a player from the game.\n" +
                "/deathswap list - Lists the players that are currently playing.\n" +
                "/deathswap reload - Reload the plugin. (Deletes all players that are playing.)");
    }

    public void teleport() {
        handler.randomizePlayers();
    }

    @SuppressWarnings("unused")
    public void printTime(int secondsLeft) {
        handler.printTime(secondsLeft);
    }

}
