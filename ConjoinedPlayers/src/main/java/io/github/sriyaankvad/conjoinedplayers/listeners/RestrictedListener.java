package io.github.sriyaankvad.conjoinedplayers.listeners;

import io.github.sriyaankvad.conjoinedplayers.CommandKit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
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

import java.util.ArrayList;

public interface RestrictedListener extends Listener {

    CommandKit kit = new CommandKit();
    ArrayList<Player> players = kit.players;

    void handleMovement(PlayerMoveEvent e);

    void handleItemPickup(EntityPickupItemEvent e);

    void handleHealth(EntityDamageEvent e);

    void handleHunger(FoodLevelChangeEvent e);

    void handleBlockPlace(BlockPlaceEvent e);

    void handleInventory(InventoryInteractEvent e);

    void handleBlockDamage(BlockDamageEvent e);

    void handleEntityDamage(EntityDamageByEntityEvent e);

    void handleItemConsumption(PlayerItemConsumeEvent e);

    void handleItemSlot(PlayerItemHeldEvent e);

}
