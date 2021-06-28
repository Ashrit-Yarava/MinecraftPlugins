package io.github.ashrityarava.playercompass.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.TreeMap;
import java.util.UUID;

public class Handler {

    TreeMap<UUID, Location> trackersList;

    public Handler() {
        trackersList = new TreeMap<>();
    }

    public void setTarget(Player player, Location loc) {
        trackersList.put(player.getUniqueId(), loc);
    }

    public void reset(Player player) {
        Location original = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
        trackersList.put(player.getUniqueId(), original);
    }

    public void track(Player sender) {
        if(trackersList.containsKey(sender.getUniqueId())) {
            sender.setCompassTarget(trackersList.get(sender.getUniqueId()));
        } else {
            this.reset(sender);
        }
    }

}
