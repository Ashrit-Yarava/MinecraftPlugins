package io.github.sriyaankvad.minecraftrandomizer.minecraftrandomizer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.TreeMap;

public class Handler {

    public TreeMap<Material, Material> map;

    public Handler() {
        Material[] materials = Material.values();

        for(Material block : materials) {
            if(block.isBlock()) {
                int pos;
                do {
                    pos = (int)(Math.random()* materials.length);
                } while(!materials[pos].isItem());
                map.put(block, materials[pos]);
            }
        }
    }

    public ItemStack getDrop(Block block) {
        return new ItemStack(map.get(block.getType()),
                (int)(Math.random() * 64) + 1);
    }

}
