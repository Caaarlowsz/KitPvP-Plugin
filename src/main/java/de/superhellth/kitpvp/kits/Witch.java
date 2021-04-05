package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Witch extends Kit {

    // singleton pattern
    private static Witch instance;
    public static Witch getInstance() {
        if (Witch.instance == null) {
            Witch.instance = new Witch();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Witch";
    }

    @Override
    public void setColor() {
        color = ChatColor.LIGHT_PURPLE;
    }

    @Override
    public void setItems() {
        quickAdd(Material.CAULDRON, 1);
        quickAdd(Material.WATER_BUCKET, 1);
        quickAdd(Material.WATER_BUCKET, 1);
        quickAdd(Material.BREWING_STAND, 3);
        quickAdd(Material.NETHER_WART, 3);
        quickAdd(Material.BLAZE_POWDER, 3);
        quickAdd(Material.GUNPOWDER, 3);
        quickAdd(Material.GLASS_BOTTLE, 12);
        quickAdd(Material.SPIDER_EYE, 2);
        quickAdd(Material.FERMENTED_SPIDER_EYE, 1);
        quickAdd(Material.SUGAR, 1);
        quickAdd(Material.REDSTONE, 1);
        quickAdd(Material.GLOWSTONE, 1);
    }

    @Override
    public void setArmor() {
    }

    @Override
    public void setEffects() {
    }
}
