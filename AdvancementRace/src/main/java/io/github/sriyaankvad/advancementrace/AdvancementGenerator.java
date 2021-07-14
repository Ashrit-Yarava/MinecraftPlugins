package io.github.sriyaankvad.advancementrace;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class AdvancementGenerator {
    private ArrayList<Advancement> advancements;

    public AdvancementGenerator() {
        advancements = new ArrayList<>();
        Iterator<Advancement> iter = Bukkit.advancementIterator();

        while(iter.hasNext()) {
            Advancement advancement = iter.next();
            if(!isBanned(advancement)) advancements.add(advancement);
        }
    }

    public Advancement randomAdvancement() {
        return advancements.get((int) (Math.random()*advancements.size()));
    }

    private boolean isBanned(Advancement advancement) {
        try {
            String name = advancement.getKey().getNamespace();
            File fileToRead = new File("src/main/resources/BanList.txt");
            BufferedReader br = new BufferedReader(new FileReader(fileToRead));
            String line = null;

            while ((line = br.readLine()) != null)
                if (line.equalsIgnoreCase(name))
                   return true;

        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
