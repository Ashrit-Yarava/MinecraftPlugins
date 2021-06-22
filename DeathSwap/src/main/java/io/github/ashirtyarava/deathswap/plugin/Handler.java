package io.github.ashirtyarava.deathswap.plugin;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class Handler {

    public ArrayList<Player> players;
    public boolean gameInProgress;

    public Handler() {
        players = new ArrayList<>();
        gameInProgress = false;
    }

    public boolean addPlayer(Player player) {
        if(!players.contains(player)) {
            players.add(player);
            return true;
        }
        return false;
    }

    public boolean removePlayer(Player player) {
        if(!players.contains(player))
            return false;
        else {
            players.remove(player);
            return true;
        }
    }

    public String listPlayers() {
        StringBuilder text = new StringBuilder("Players:\n---------------\n");
        for(Player i: players) {
            text.append(i.getName()).append("\n");
        }
        return text + "---------------";
    }

    public void randomizePlayers() {
        // Generate new locations for each of the players.
        Location[] positions = new Location[players.size()];
        for(int i = 0; i < players.size(); i++) {
            int randomPosition;
            do {
                randomPosition = new Random().nextInt((players.size()));
            } while(randomPosition == i);
            positions[randomPosition] = players.get(i).getLocation();
        }
        // Teleport the players to their new locations.
        for(int i = 0; i < players.size(); i++) {
            players.get(i).teleport(positions[i]);
        }
    }

    public void removeDead() {
        for(int i = 0; i < players.size(); i++) {
            if(i < players.size()) {
                if(players.get(i).isDead()) {
                    players.get(i).setGameMode(GameMode.SPECTATOR);
                    players.remove(i);
                }
            }
        }
    }

    public void printTime(int seconds) {
        for(Player i: players) {
            i.sendMessage(ChatColor.RED + "" + seconds + " left till the next swap.");
        }
    }

}
