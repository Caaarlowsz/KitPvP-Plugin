package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;

public class Enchanter extends Kit {

    // singleton pattern
    private static Enchanter instance;
    public static Enchanter getInstance() {
        if (Enchanter.instance == null) {
            Enchanter.instance = new Enchanter();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Enchanter";
    }

    @Override
    public void setColor() {
        color = ChatColor.DARK_PURPLE;
    }

    @Override
    public void setView() {
        view = Material.ENCHANTING_TABLE;
    }

    @Override
    public void setLore() {
        lore = Arrays.asList("");
    }

    @Override
    public void setItems() {
        quickAdd(Material.ENCHANTING_TABLE, 1);
        quickAdd(Material.EXPERIENCE_BOTTLE, 64);
        quickAdd(Material.LAPIS_LAZULI, 64);
        quickAdd(Material.BOOKSHELF, 15);
    }

    @Override
    public void setArmor() {
    }

    @Override
    public void setEffects() {
    }
}
