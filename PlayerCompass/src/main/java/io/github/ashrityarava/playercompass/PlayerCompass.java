package io.github.ashrityarava.playercompass;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public final class PlayerCompass extends JavaPlugin {

    Map<UUID, UUID> trackers;

    @SuppressWarnings("SortedCollectionWithNonComparableKeys")
    @Override
    public void onEnable() {

        trackers = new TreeMap<>();
        // Register Plugins.
        this.getCommand("compass").setExecutor(new CommandKit());
        Bukkit.getServer().getPluginManager().registerEvents(new CompassListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void log(String str) {
        // Simple script for better logging to bukkit console.
        Bukkit.getLogger().info(str);
    }

    public class CommandKit implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(args.length == 1) {
                    if(args[0].equals("reset")) {
                        // Set compass target to world spawn.
                        Location worldSpawn = player.getWorld().getSpawnLocation();
                        player.setCompassTarget(worldSpawn);
                    } else if(args[0].equals("give")) {
                        // Give unique item Hunter's Compass, to player.
                        // It has vanishing curse.
                        ItemStack compassItem = new ItemStack(Material.COMPASS);
                        ItemMeta meta = compassItem.getItemMeta();
                        assert meta != null;
                        meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
                        @NonNull TextComponent name = Component.text("Hunter's Compass").color(TextColor.color(new RGBLike() {
                            @Override
                            public @IntRange(from = 0L, to = 255L) int red() {
                                return 255;
                            }

                            @Override
                            public @IntRange(from = 0L, to = 255L) int green() {
                                return 215;
                            }

                            @Override
                            public @IntRange(from = 0L, to = 255L) int blue() {
                                return 0;
                            }
                        }));
                        meta.displayName(name);
                        compassItem.setItemMeta(meta);
                        player.getInventory().addItem(compassItem);
                        player.sendMessage(ChatColor.GRAY + "New Compass Has Been Given.");
                    } else {
                        // Add new player to trackers list if the player exists.
                        Player trackedPlayer = Bukkit.getPlayer(args[0]);
                        if(trackedPlayer != null)
                            trackers.put(player.getUniqueId(), trackedPlayer.getUniqueId());
                        else {
                            log(ChatColor.RED + args[0] + " was not found.");
                        }
                    }
                } else {
                    printHelp(player);
                }
            } else {
                log("Only players can execute this command.");
                // Only players should be able to track other players.
            }
            return true;
        }

        private void printHelp(Player player) {
            // Quick Help Text.
            String text = "\n/compass <player name> - Sets compass target to the selected player.\n" +
                    "/compass reset - Sets compass to point to the world spawn.\n" +
                    "/compass give - Gives player a new compass.\n";
            player.sendMessage(ChatColor.GRAY  + text);
        }
    }

    public class CompassListener implements Listener {

        @EventHandler(priority = EventPriority.HIGH)
        public void onPlayerUse(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            Action action = event.getAction();
            ItemStack item = event.getItem();

            // IF (Player Right Clicks) && (Player is holding compass), reset the compass to the latest location of tracked player.
            if((action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR))) {
                if(item == null) {
                    if (trackers.containsKey(player.getUniqueId())) {
                        player.setCompassTarget(Bukkit.getPlayer(trackers.get(player.getUniqueId())).getLocation());
                        player.sendMessage(ChatColor.DARK_GREEN + "Now tracking " + Bukkit.getPlayer(trackers.get(player.getUniqueId())).getName());
                    } else {
                        Location worldSpawn = player.getWorld().getSpawnLocation();
                        player.setCompassTarget(worldSpawn);
                        player.sendMessage(ChatColor.RED + "No one to track.");
                    }
                    return;
                }
                if (item.getType().equals(Material.COMPASS)) {
                    if (trackers.containsKey(player.getUniqueId())) {
                        player.setCompassTarget(Bukkit.getPlayer(trackers.get(player.getUniqueId())).getLocation());
                        player.sendMessage(ChatColor.DARK_GREEN + "Now tracking " + Bukkit.getPlayer(trackers.get(player.getUniqueId())).getName());
                    } else {
                        Location worldSpawn = player.getWorld().getSpawnLocation();
                        player.setCompassTarget(worldSpawn);
                        player.sendMessage(ChatColor.GREEN + "No one to track.");
                    }
                }
            }
        }

    }

    public static void main(String[] args) {

    }

}
