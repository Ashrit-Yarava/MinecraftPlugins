package io.github.sriyaankvad.advancementrace;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandKit implements CommandExecutor {

//    ArrayList<Player> players;
//    ArrayList<Advancement> advancements;
//    AdvancementGenerator generator;
//    boolean gameInProgress;
//    int time;
//
//    public CommandKit() {
//        players = new ArrayList<>();
//        advancements = new ArrayList<>();
//        generator = new AdvancementGenerator();
//        gameInProgress = false;
//        time = 300;
//    }

    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Advancement> advancements = new ArrayList<>();
    AdvancementGenerator generator = new AdvancementGenerator();
    boolean gameInProgress = false;
    int time = 300;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Bukkit.getLogger().warning("Only players can execute this command!");
            return true;
        }

        Player player = (Player) sender;

        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("addPlayer")) {
                if(Bukkit.getPlayer(args[1]) != null) {
                    if(addPlayer(Bukkit.getPlayer(args[1]))) {
                        player.sendMessage(args[1] + " has been added to the game.");
                        Bukkit.getPlayer(args[1]).sendMessage("You have been added to the Advancement Race!");
                    } else {
                        player.sendMessage(args[1] + " has already been added.");
                    }
                } else {
                    player.sendMessage(args[1] + " was not found.");
                }
            } else if(args[0].equalsIgnoreCase("removePlayer")) {
                if(Bukkit.getPlayer(args[1]) != null) {
                    if(removePlayer(Bukkit.getPlayer(args[1]))) {
                        player.sendMessage(args[1] + " has been removed to the game.");
                        Bukkit.getPlayer(args[1]).sendMessage("You have been removed from the Advancement Race :(");
                    } else {
                        player.sendMessage(args[1] + " was not part of the Advancement Race.");
                    }
                } else {
                    player.sendMessage(args[1] + " was not found.");
                }
            } else if(args[0].equalsIgnoreCase("time") && args.length > 1) {
                try {
                    time = Integer.parseInt(args[1]);
                    player.sendMessage("The time for each round has been changed to " + time);
                } catch (NumberFormatException e) {
                    player.sendMessage("Invalid Time");
                }
            } else if(args[0].equalsIgnoreCase("list")) {
                listPlayers(player);
            } else if(args[0].equalsIgnoreCase("start")) {
                if (gameInProgress)
                    player.sendMessage(ChatColor.AQUA + "There is already a game in progress\n" +
                            "Type the command \"advancementrace addPlayer <Your name>\" to join it");
                else {
                    gameInProgress = true;
                    broadcast(ChatColor.GREEN + "The Advancement Race has begun!!!");
                }
            } else if(args[0].equalsIgnoreCase("stop")) {
                if (!gameInProgress)
                    player.sendMessage(ChatColor.DARK_RED +
                            "There is no game in progress.");
                else {
                    gameInProgress = false;
                    broadcast(ChatColor.AQUA + "The Advancement Race has ended. Thank you for playing.");
                }
            }
        } else {
            printUsage(player);
        }

        return true;
    }

    public void broadcast(String message) {
        for (Player player : players)
            player.sendMessage(message);
    }

    public void init() {
        for(Player player : players) {
            Advancement advancement = generator.randomAdvancement();
            advancements.add(advancement);
            AdvancementProgress progress = player.getAdvancementProgress(advancement);
            for(String s : progress.getAwardedCriteria())
                progress.revokeCriteria(s);

            player.sendMessage(ChatColor.DARK_AQUA + "Your Advancement is "
                    + advancement.getKey().getKey());
        }
    }

    public void endRound() {
        for(int i = 0; i < advancements.size(); i++) {
            if(advancements.get(i) != null) {
                players.get(i).sendMessage(ChatColor.DARK_RED + "You failed to complete your advancement. You have been removed from the game.");
                removePlayer(players.get(i));
            }
        }
        if(players.size() == 1) {
            players.get(0).sendMessage(ChatColor.GREEN + "You have won the advancement race!");
            gameInProgress = false;
        } else {
            advancements.clear();
        }
    }

    public void checkAdvancementCompletion() {
        for(int i = 0; i < players.size(); i++) {
            if((advancements.get(i) != null)
                    && players.get(i).getAdvancementProgress(advancements.get(i)).isDone()) {
                broadcast(ChatColor.YELLOW + "" + players.get(i)
                        + " has completed their advancement.");
                advancements.set(i, null);
            }
        }
    }

    private boolean addPlayer(Player player) {
        if(!players.contains(player)) {
            players.add(player);
            return true;
        }
        return false;
    }

    private boolean removePlayer(Player player) {
        if(players.contains(player)) {
            players.remove(player);
            return true;
        }
        return false;
    }

    private void listPlayers(Player player) {
        String msg = ChatColor.GOLD + "Players in Advancement Race:\n----------------------------\n\t";
        for(Player p : players) {
            msg += p + "\n\t";
        }
        player.sendMessage(msg);
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
