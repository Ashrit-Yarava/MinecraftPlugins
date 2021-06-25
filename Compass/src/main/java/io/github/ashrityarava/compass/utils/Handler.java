package io.github.ashrityarava.compass.utils;

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
        Location original = player.getWorld().getSpawnLocation();
        trackersList.put(player.getUniqueId(), original);
    }

    public void track(Player sender) {
        sender.setCompassTarget(trackersList.get(sender));
    }

}
