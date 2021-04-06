package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Tank extends Kit {

    // singleton pattern
    private static Tank instance;
    public static Tank getInstance() {
        if (Tank.instance == null) {
            Tank.instance = new Tank();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Tank";
    }

    @Override
    public void setColor() {
        color = ChatColor.AQUA;
    }

    @Override
    public void setView() {
        view = Material.DIAMOND_CHESTPLATE;
    }

    @Override
    public void setLore() {
        lore = Arrays.asList("");
    }

    @Override
    public void setItems() {
    }

    @Override
    public void setArmor() {
    }

    @Override
    public void setEffects() {
        armor[2] = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
    }
}
