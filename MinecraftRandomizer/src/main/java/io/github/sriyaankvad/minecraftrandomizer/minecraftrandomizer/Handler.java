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
            Material drop = materials[(int)(Math.random()* materials.length)];

            if(drop.isItem() && block.isBlock()) map.put(block, drop);
        }
    }

    public ItemStack getDrop(Block block) {
        return new ItemStack(map.get(block.getType()),
                (int)(Math.random() * 64) + 1);
    }

}
