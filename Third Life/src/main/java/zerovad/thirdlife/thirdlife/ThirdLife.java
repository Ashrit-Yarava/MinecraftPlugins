package zerovad.thirdlife.thirdlife;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * The main class of the plugin
 * Deals with the command kit, listeners, and recipe changes
 */
public final class ThirdLife extends JavaPlugin {

    FileConfiguration config;
    Lives lives = new Lives();

    @Override
    public void onEnable() {
        this.config = this.getConfig();

        getLogger().info("Third Life: A Plugin for Minecraft 1.19 by Sriyaank Vadlamani, based on Grian's 3rd Life Youtube Series");

        // load the previous live counts
        this.lives.loadLives(this.config);

        // Set up the executor for the plugin.
        CommandKit kit = new CommandKit();
        Objects.requireNonNull(this.getCommand("lives")).setExecutor(kit);

        // start the listeners
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new ItemCraft(), this);

        // change TNT recipe
        ItemStack tntStack = new ItemStack(Material.TNT, 1);
        NamespacedKey key = new NamespacedKey(this, "tnt");
        ShapedRecipe tnt = new ShapedRecipe(key, tntStack);

        tnt.shape("*%*","%B%","*%*");

        tnt.setIngredient('*', Material.PAPER);
        tnt.setIngredient('%', Material.SAND);
        tnt.setIngredient('B', Material.GUNPOWDER);

        getServer().addRecipe(tnt);

    }

    @Override
    public void onDisable() {
        lives.saveLives(config);
        Bukkit.getLogger().info("Thank you for using this plugin");
        saveConfig();
    }

    /**
     * Decreases a life from a player when they die
     */
    private class DeathListener implements Listener {
        @EventHandler
        public void lifeUpdate(PlayerDeathEvent event) {
            Player player = event.getEntity();
            lives.decreaseLives(player);
        }

    }

    /**
     * Assigns a life to someone when joining for the first time
     * Gives a player a color when they join everytime
     */
    private class PlayerJoin implements Listener {
        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            lives.addPlayer(player);
        }

    }

    /**
     * Disables the enchanting table and bookshelf recipes from being crafted
     * Meant to do two things
     * * Prevent high level enchantments so pvp actually works
     * * Create chaos by only having one enchanter on the server
     */
    private class ItemCraft implements Listener {
        @EventHandler
        public void disableRecipes(PrepareItemCraftEvent event) {
            Material itemType = Objects.requireNonNull(event.getRecipe()).getResult().getType();
            if (itemType == Material.ENCHANTING_TABLE || itemType == Material.BOOKSHELF) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }

    /**
     * The command kit to handle everything regarding the /lives command
     */
    private class CommandKit implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
            if(!(sender instanceof Player)) {
                Bukkit.getLogger().warning("Only players can execute this command!");
                return true;
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                printUsage(player);
            } else {
                if (args[0].equals("end")) {
                    player.sendMessage(lives.endSession());
                } else if(args[0].equals("display")) {
                    player.sendMessage(lives.printLives());
                } else if(args[0].equals("add") && args.length > 1) {
                    if(Bukkit.getPlayer(args[1]) != null) {
                        if(lives.increaseLife(Objects.requireNonNull(Bukkit.getPlayer(args[1]))))
                            player.sendMessage(Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName() + "'s life was increased");
                        else
                            player.sendMessage(Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName() + " already has 3 lives");
                    } else {
                        player.sendMessage("This player does not exist");
                        printUsage(player);
                    }
                } else if(args[0].equals("remove") && args.length > 1) {
                    if(Bukkit.getPlayer(args[1]) != null) {
                        if(lives.decreaseLives(Objects.requireNonNull(Bukkit.getPlayer(args[1]))))
                            player.sendMessage(Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName() + "'s life was increased");
                        else
                            player.sendMessage(Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName() + " already has no life");
                    } else {
                        player.sendMessage("This player does not exist");
                        printUsage(player);
                    }
                } else
                    printUsage(player);
            }
            return true;
        }

        private void printUsage(Player player) {
            player.sendMessage("Usage:\n" +
                    "/lives end - End the Session, making the lives of all offline players no more than the average of all online players.\n" +
                    "/lives display - Shows the lives of all players.\n" +
                    "/lives add (playername) - Adds a life to player with less than 3 lives. Only use in emergencies.\n" +
                    "/lives remove (playername) - Removes a life to player with at least one life. Only use in emergencies.\n");
        }
    }
}
