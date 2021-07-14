package io.github.sriyaankvad.minecraftrandomizer.minecraftrandomizer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    Handler handler = new Handler();

    @EventHandler(priority = EventPriority.HIGH)
    public void ItemDrop(BlockBreakEvent event){
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();
        World world = player.getWorld();

        event.setDropItems(false);
        world.dropItem(location, handler.getDrop(event.getBlock()));
    }
}
