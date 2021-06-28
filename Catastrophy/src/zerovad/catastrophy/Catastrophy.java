package zerovad.catastrophy;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Catastrophy implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void creatureSpawn(BlockBreakEvent event){
        Player player = event.getPlayer();
        Location location = player.getLocation();
        World world = player.getWorld();

        world.spawnEntity(location, EntityType.CAT);
    }
}
