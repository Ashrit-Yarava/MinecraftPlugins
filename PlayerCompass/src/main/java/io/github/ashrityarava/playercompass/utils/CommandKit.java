package io.github.ashrityarava.playercompass.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class CommandKit implements CommandExecutor {

    Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {

            Player player = (Player) sender;

            if(args.length > 0 && !args[0].equals("reset")) {
                giveCompass(player);
                if(Bukkit.getPlayer(args[0]) != null) {
                    handler.setTarget(player, Objects.requireNonNull(Bukkit.getPlayer(args[0])).getLocation());
                    player.sendMessage(ChatColor.GREEN + "Now tracking " + args[0]);
                } else {
                    player.sendMessage(ChatColor.RED + "Player is offline.");
                }
            } else if(args.length > 0) {
                giveCompass(player);
                handler.reset(player);
            } else {
                player.sendMessage(ChatColor.GRAY + "Usage: /compass <player to track>, /compass reset");
            }

        } else {
            Bukkit.getLogger().info("Only players can run that command.");
        }
        return true;
    }

    private void giveCompass(Player player) {
        if(!player.getInventory().contains(new ItemStack(Material.COMPASS))) {
            ItemStack compass = new ItemStack(Material.COMPASS);
            ItemMeta compassMeta = compass.getItemMeta();
            assert compassMeta != null;
            compassMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            compass.setItemMeta(compassMeta);
            player.getInventory().addItem();
        }
    }

}

