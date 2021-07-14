package io.github.ashrityarava.playercompass;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Trackers {

    private TreeMap<UUID, Location> locations;
    private TreeMap<UUID, UUID> players;

    public Trackers() {
        locations = new TreeMap<UUID, Location>();
        players = new TreeMap<UUID, UUID>();
    }

    public void addPlayer(Player player) {
        // Add the new player
        players.put(player.getUniqueId(), null);

        // Get World Spawn Location and input that into compass.
        if(!locations.containsKey(player.getUniqueId())) {
            Location worldSpawn = Bukkit.getWorlds().get(0).getSpawnLocation();
            locations.put(player.getUniqueId(), worldSpawn);
        }
    }

    public void changeTarget(Player player, Player target) {
        // Add the player and their chosen target.
        players.put(player.getUniqueId(), target.getUniqueId());
    }

    public void updateCompass() {
        assert players.size() == locations.size();

        // Logic of Operation:
        // 1. Iterate through the list of players currently online.
        // 2. If the player is online but has not set a player as a target, set the world spawn as the target.
        // 3. If the player has selected a target, make sure that the target is also online.
        // 4. If the target is not online, then set their previous location as the compass target.
        // 5. If the target is not in the same world, use the last known location of the target.
        // 6. If both players are in the same world, update the location of the target as the new location for the player.
        // 7. Set the player's compass target to the position.

        for(UUID playerId: players.keySet()) {
            Player player = Bukkit.getPlayer(playerId);
            if(player != null) {
                if (players.get(playerId) == null) {
                    player.setCompassTarget(locations.get(playerId));
                } else {
                    Player target = Bukkit.getPlayer(players.get(playerId));
                    if(target == null) {
                        players.put(playerId, null);
                    } else {
                        if(target.getWorld().hashCode() == player.getWorld().hashCode()) {
                            locations.put(playerId, target.getLocation());
                        }
                        player.setCompassTarget(locations.get(playerId));
                    }
                }
            }
        }

    }

    public static void givePlayerCompass(Player player) {
        ItemStack compassItem = new ItemStack(Material.COMPASS);
        ItemMeta meta = compassItem.getItemMeta();
        assert meta != null;
        meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        meta.setDisplayName(ChatColor.GOLD + "Hunter's Compass");
        meta.setLore(Collections.singletonList("When In Doubt, Be Still.\nThe Compass Of Your Soul\nWill Tell You The Way To Go."));
        compassItem.setItemMeta(meta);
        player.getInventory().addItem(compassItem);
        player.sendMessage(ChatColor.GRAY + "New Compass received.");
    }

    public void saveLocations(FileConfiguration config) {

        for(UUID playerId: locations.keySet()) {
            StringLocation loc = new StringLocation(locations.get(playerId));
            config.set(playerId.toString(), loc.getString());
        }

    }

    public void loadLocations(FileConfiguration config) {

        for(String playerId: config.getKeys(false)) {
            UUID uuid = UUID.fromString(playerId);
            Location loc = new StringLocation((String) config.get(playerId)).getLocation();
            locations.put(uuid, loc);
        }

    }

    private static class StringLocation {

        double x, y, z;
        float yaw, pitch;
        String world;

        // Format ==> "world x y z yaw pitch"

        public StringLocation(String loc) {
            String[] parts = loc.split(" ");
            world = parts[0];
            x = Double.parseDouble(parts[1]);
            y = Double.parseDouble(parts[2]);
            z = Double.parseDouble(parts[3]);
            yaw = Float.parseFloat(parts[4]);
            pitch = Float.parseFloat(parts[5]);
        }

        public StringLocation(Location loc) {
            world = Objects.requireNonNull(loc.getWorld()).getName();
            x = loc.getX();
            y = loc.getY();
            z = loc.getZ();
            yaw = loc.getYaw();
            pitch = loc.getPitch();
        }

        public Location getLocation() {
            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }

        public String getString() {
            return world + " " + x + " " + y + " " + z + " " + yaw + " " + pitch;
        }

    }

}
