package zerovad.thirdlife.thirdlife;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

/**
 * The class that handels everything regarding player lives
 */
public class Lives {

    private final TreeMap<UUID, Integer> playerLives;

    private final ChatColor[] lifeColors;

    public Lives() {
        this.playerLives = new TreeMap<UUID, Integer>();
        this.lifeColors = new ChatColor[4];
        this.lifeColors[0] = ChatColor.GRAY;
        this.lifeColors[1] = ChatColor.RED;
        this.lifeColors[2] = ChatColor.YELLOW;
        this.lifeColors[3] = ChatColor.GREEN;
    }

    /**
     * Decrease the number of lives of a player, putting them in spectator mode when out of lives
     * @param loser the player whose lost a life
     * @return true if lives were successfully decreased, false otherwise
     */
    public boolean decreaseLives(Player loser) {
        loser.sendMessage("tough luck");

        if(this.playerLives.containsKey(loser.getUniqueId())) {
            if(this.playerLives.get(loser.getUniqueId()) == 0) return false;

            this.playerLives.replace(loser.getUniqueId(), this.playerLives.get(loser.getUniqueId()) - 1);

            if (this.playerLives.get(loser.getUniqueId()) == 0) {
                loser.setGameMode(GameMode.SPECTATOR);
            }

            this.updateColors();

            return true;
        }
        return false;
    }

    /**
     * Updates the colors of all the player names in chat and player list
     */
    private void updateColors() {
        for(UUID playerId : this.playerLives.keySet()){
            Player player = Bukkit.getPlayer(playerId);
            if(player != null) {
                player.setDisplayName(this.lifeColors[this.playerLives.get(playerId)] + player.getName());
                player.setPlayerListName(this.lifeColors[this.playerLives.get(playerId)] + player.getName());
            }
        }
    }

    /**
     * Increase player life by one
     * Only meant to be used through commands in case of emergency
     * @param player the player whose life to increase
     * @return true if life increased successfully, false otherwise
     */
    public boolean increaseLife(Player player) {
        if(this.playerLives.containsKey(player.getUniqueId())) {
            if (this.playerLives.get(player.getUniqueId()) == 3) return false;

            if (this.playerLives.get(player.getUniqueId()) == 0) {
                player.setGameMode(GameMode.SURVIVAL);
            }
            this.playerLives.replace(player.getUniqueId(), this.playerLives.get(player.getUniqueId()) + 1);
            this.updateColors();
            return true;
        }
        return false;
    }

    /**
     * Prints the number of lives of each player in list form
     * Meant for testing purposes
     * @return the number of lives for each player in list form
     */
    public String printLives() {
        String s = "Lives: ";
        for(UUID playerId : this.playerLives.keySet()) {
            Player player = Bukkit.getPlayer(playerId);
            s += player.getName() + ": " + this.playerLives.get(playerId) + "\n";
        }
        return s;
    }

    /**
     * Updates all offline player's lives to be no more than the
     * average number of lives of all online players
     */
    public String endSession() {
        String s = "Ending session....\n";
        int average = 0;
        for(UUID playerId : this.playerLives.keySet()) {
            average += this.playerLives.get(playerId);
        }
        average = Math.max(1, average/Bukkit.getOnlinePlayers().size());

        for(UUID playerId : this.playerLives.keySet()) {
            Player player = Bukkit.getPlayer(playerId);
            if (!Bukkit.getOnlinePlayers().contains(player)) {
                this.playerLives.put(playerId, Math.min(average, this.playerLives.get(playerId)));
                s += player.getName() + " now has " + this.playerLives.get(playerId) + " lives.\n";
            }
        }
        return s + "Session ended.";
    }

    /**
     * Adds a player joining for the first time to playerLives
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        if(this.playerLives.containsKey(player.getUniqueId())) {
            player.sendMessage("welcome back");
            return;
        }

        if(this.playerLives.isEmpty()) {
            player.sendMessage("numero uno I see");
            this.playerLives.put(player.getUniqueId(), 3);
        } else {
            int average = 0;
            for(UUID playerId : this.playerLives.keySet()) {
                average += this.playerLives.get(playerId);
            }
            average = Math.max(1, average/this.playerLives.size());
            this.playerLives.put(player.getUniqueId(), average);
        }
        player.sendMessage("happy hunger games");
        this.updateColors();
        player.sendMessage("and may the odds be ever in your favor");
    }

    /**
     * Stores the number of lives in the file configuration
     * @param config
     */
    public void saveLives(FileConfiguration config) {
        for(UUID playerId : this.playerLives.keySet()) {
            config.set(playerId.toString(), this.playerLives.get(playerId).toString());
        }
    }

    /**
     * Loads the number of lives in the file configuration
     * @param config
     */
    public void loadLives(FileConfiguration config) {
        for(String playerId: config.getKeys(false)) {
            UUID id = UUID.fromString(playerId);
            Integer lives = Integer.valueOf((String) Objects.requireNonNull(config.get(playerId)));
            this.playerLives.put(id, lives);
        }
    }

}
