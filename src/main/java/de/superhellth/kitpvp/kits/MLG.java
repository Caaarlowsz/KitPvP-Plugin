package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class MLG extends Kit {

    // singleton pattern
    private static MLG instance;
    public static MLG getInstance() {
        if (MLG.instance == null) {
            MLG.instance = new MLG();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "MLG";
    }

    @Override
    public void setColor() {
        color = ChatColor.DARK_RED;
    }

    @Override
    public void setItems() {
        quickAdd(Material.OAK_PLANKS, 16);
        quickAdd(Material.HAY_BLOCK, 3);
        quickAdd(Material.OAK_BOAT, 1);
        quickAdd(Material.OAK_BOAT, 1);
        quickAdd(Material.RED_BED, 1);
        quickAdd(Material.LADDER, 16);
        quickAdd(Material.TNT, 16);
        quickAdd(Material.WATER_BUCKET, 1);
    }

    @Override
    public void setArmor() {
    }

    @Override
    public void setEffects() {
    }
}
