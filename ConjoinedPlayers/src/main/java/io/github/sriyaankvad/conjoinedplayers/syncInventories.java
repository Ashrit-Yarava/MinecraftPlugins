package io.github.sriyaankvad.conjoinedplayers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

public class syncInventories {
    PlayerInventory inv;
    ArrayList<Player> players;

    public syncInventories(ArrayList<Player> players) {
        this.players = players;
        inv = players.get(0).getInventory();
    }

    public void checkForSyncInventory() {
        for(Player player : players)
            if(!player.getInventory().equals(inv)) {
                inv = player.getInventory();
                syncInventories();
                return;
            }
    }

//    public void shiftArmor() {
//        for(int i = 1; i < players.size(); i++) {
//            if(hasArmor(players.get(i))) {
//                players.get(0).getInventory().setArmorContents(players.get(i).getInventory().getArmorContents());
//                
//            }
//        }
//    }
//    
//    private boolean hasArmor(Player player) {
//        for(ItemStack armor : player.getInventory().getArmorContents())
//            if(armor != null)
//                return true;
//        return false;
//    }
    
    private void syncInventories() {
        for(Player player : players) {
            player.getInventory().setContents(inv.getContents());
            player.updateInventory();
        }
    }

}
