package io.github.sriyaankvad.conjoinedplayers.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class TwoPlayers implements RestrictedListener {

    //Mover and Block Events (Player 1)
    @Override
    public void handleMovement(PlayerMoveEvent e) {
        if (e.getPlayer().equals(players.get(0))) {
            players.get(1).teleport(players.get(0).getLocation());
        } else {
            e.setCancelled(true);
        }
    }

    @Override
    public void handleItemPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player)
            if (e.getEntity().equals(players.get(1)))
                e.setCancelled(true);
    }

    @Override
    public void handleHealth(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player)
            if (e.getEntity().equals(players.get(0)))
                for (int i = 1; i < players.size(); i++)
                    players.get(i).setHealth(players.get(0).getHealth());
            else
                e.setCancelled(true);
    }

    @Override
    public void handleHunger(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player)
            if (e.getEntity().equals(players.get(0)))
                for (int i = 1; i < players.size(); i++)
                    players.get(i).setFoodLevel(players.get(0).getFoodLevel());
            else
                e.setCancelled(true);
    }

    @Override
    public void handleBlockPlace(BlockPlaceEvent e) {
        if (e.getPlayer().equals(players.get(1)))
            e.setCancelled(true);
    }

    @Override
    public void handleBlockDamage(BlockDamageEvent e) {
        if (e.getPlayer().equals(players.get(1)))
            e.setCancelled(true);
    }

    //Fighter and Inventory Events (Player 2)
    @Override
    public void handleInventory(InventoryInteractEvent e) {
        if (e.getWhoClicked() instanceof Player)
            if (e.getWhoClicked().equals(players.get(0)))
                e.setCancelled(true);
    }

    @Override
    public void handleEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (e.getDamager().equals(players.get(0)))
                e.setCancelled(true);
        }
    }

    @Override
    public void handleItemConsumption(PlayerItemConsumeEvent e) {
        if (e.getPlayer().equals(players.get(0)))
            e.setCancelled(true);
    }

    @Override
    public void handleItemSlot(PlayerItemHeldEvent e) {
        if (e.getPlayer().equals(players.get(0)))
            e.setCancelled(true);
    }

}
