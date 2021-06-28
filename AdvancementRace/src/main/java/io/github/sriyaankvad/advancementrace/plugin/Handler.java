package io.github.sriyaankvad.advancementrace.plugin;

import io.github.sriyaankvad.advancementrace.AdvancementGenerator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Handler {
    public ArrayList<Player> players;
    public ArrayList<Advancement> advancements;
    public boolean gameInProgress;
    public AdvancementGenerator generator;

    private ArrayList<Player> finishedAdvancement;

    public Handler(){
        players = new ArrayList<>();
        advancements = new ArrayList<>();
        gameInProgress = false;
        generator = new AdvancementGenerator();

        finishedAdvancement = new ArrayList<>();
    }

    public boolean addPlayer(Player player) {
        if(!players.contains(player)) {
            players.add(player);
            if(gameInProgress) {
                finishedAdvancement.add(player);
                player.sendMessage("There is a game in progress. You will join the next round");
            }
            return true;
        }
        return false;
    }

    public boolean removePlayer(Player player) {
        if(players.contains(player)){
            players.remove(player);
            return true;
        }
        return false;
    }

    public String listPlayers(){
        String output = "Players in the Advancement Race:\n"
                + "________________________________\n";
        for(Player p : players)
            output += ChatColor.GREEN + p.getName() + "\n";
        return output;
    }

    public void init() {
        finishedAdvancement.clear();
        for(int i = 0; i < players.size(); i++) {
            advancements.add(generator.randomAdvancement());
            AdvancementProgress progress = players.get(i).getAdvancementProgress(advancements.get(i));
            for(String s : progress.getAwardedCriteria())
                progress.revokeCriteria(s);

            players.get(i).sendMessage(ChatColor.DARK_AQUA + "Your Advancement is "
                    + advancements.get(i).getKey().getKey());
        }
    }

    public void endRound() {
        if(finishedAdvancement.isEmpty()) {
            broadcast(ChatColor.DARK_RED + "Nobody could get their advancement.\n"
                + "This game ends in a draw.");
            gameInProgress = false;
            return;
        }
        else if(finishedAdvancement.size() == 1) {
            broadcast(ChatColor.GREEN + "The game has ended.\n"
                + finishedAdvancement.get(0).getName() + " is the winner!!!");
            gameInProgress = false;
            return;
        }

        for(Player p : players) {
            if(!finishedAdvancement.contains(p)) {
                p.sendMessage(ChatColor.DARK_RED + "You failed to get your advancement. You lose.");
                p.setGameMode(GameMode.SPECTATOR);
                p.teleport(finishedAdvancement.get(0).getLocation());

                broadcast(p.getName() + " is out of the game.");
                players.remove(p);
            }
        }
    }

    public void checkAdvancementCompletion() {
        for(int i = 0; i < players.size(); i++) {
            if((!finishedAdvancement.contains(players.get(i)))
                    && players.get(i).getAdvancementProgress(advancements.get(i)).isDone()) {
                broadcast(ChatColor.YELLOW + "" + players.get(i)
                    + " has completed their advancement.");
                finishedAdvancement.add(players.get(i));
            }
        }
    }

    public void broadcast(String message){
        for(Player p : players)
            p.sendMessage(message);
    }


}
