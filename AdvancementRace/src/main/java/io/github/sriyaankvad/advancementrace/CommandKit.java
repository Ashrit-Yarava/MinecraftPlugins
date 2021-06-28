package io.github.sriyaankvad.advancementrace;

import io.github.sriyaankvad.advancementrace.plugin.Handler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandKit implements CommandExecutor {

    public Handler handler;
    public int time;

    public CommandKit() {
        handler = new Handler();
        time = 300;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!(commandSender instanceof Player)) {
            Bukkit.getLogger().warning("Only players can execute this command!");
            return true;
        }

        Player player = (Player) commandSender;
        if(strings[0] == null) {
            printUsage(player);
            return true;
        }

        if(strings[0].equalsIgnoreCase("addPlayer")) {
            if(Bukkit.getPlayer(strings[1]) != null) {
                if(handler.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(strings[1])))) {
                    player.sendMessage(strings[1] + " has been added to the game.");
                    Objects.requireNonNull(Bukkit.getPlayer(strings[1])).sendMessage("You have been added to the Advancement Race!");
                } else {
                    player.sendMessage(strings[1] + " has already been added.");
                }
            } else {
                player.sendMessage(strings[1] + " was not found.");
            }
        }

        if(strings[0].equalsIgnoreCase("removePlayer")) {
            if(Bukkit.getPlayer(strings[1]) != null) {
                if(handler.removePlayer(Objects.requireNonNull(Bukkit.getPlayer(strings[1])))) {
                    player.sendMessage(strings[1] + " has been removed to the game.");
                    Objects.requireNonNull(Bukkit.getPlayer(strings[1])).sendMessage("You have been removed from the Advancement Race :(");
                } else {
                    player.sendMessage(strings[1] + " was not part of the Advancement Race.");
                }
            } else {
                player.sendMessage(strings[1] + " was not found.");
            }
        }

        if(strings[0].equalsIgnoreCase("time") && strings.length > 1) {
            try {
                time = Integer.parseInt(strings[1]);
                player.sendMessage("The time for each round has been changed to " + time);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid Time");
            }
        }

        if(strings[0].equalsIgnoreCase("list"))
            handler.listPlayers();

        if(strings[0].equalsIgnoreCase("start")) {
            if (handler.gameInProgress)
                player.sendMessage(ChatColor.AQUA + "There is already a game in progress\n" +
                        "Type the command \"advancementrace addPlayer <Your name>\" to join it");
            else {
                handler.gameInProgress = true;
                handler.broadcast(ChatColor.GREEN + "The Advancement Race has begun!!!");
            }
        }

        if(strings[0].equalsIgnoreCase("stop")) {
            if (!handler.gameInProgress)
                player.sendMessage(ChatColor.DARK_RED +
                        "There is no game in progress.");
            else {
                handler.gameInProgress = false;
                handler.broadcast(ChatColor.AQUA + "The Advancement Race has ended. Thank you for playing.");
            }
        }

        return true;
    }

    private static void printUsage(Player player) {
        player.sendMessage("Usage:\n" +
                "/advancementrace start - Start the advancement race.\n" +
                "/advancementrace stop - Stop the advancement race.\n" +
                "/advancementrace time (seconds) - Set the amount of time for each round.\n" +
                "/advancementrace addPlayer (playername) - Adds a player to the race.\n" +
                "/advancementrace removePlayer (playername) - Removes a player from the race.\n" +
                "/advancementrace list - Lists the players that are currently playing.\n");
    }
}

