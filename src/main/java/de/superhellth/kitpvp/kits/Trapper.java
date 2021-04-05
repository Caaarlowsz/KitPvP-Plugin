package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Trapper extends Kit {

    // singleton pattern
    private static Trapper instance;
    public static Trapper getInstance() {
        if (Trapper.instance == null) {
            Trapper.instance = new Trapper();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Trapper";
    }

    @Override
    public void setColor() {
        color = ChatColor.DARK_GRAY;
    }

    @Override
    public void setItems() {
        quickAdd(Material.STICKY_PISTON, 8);
        quickAdd(Material.PISTON, 8);
        quickAdd(Material.REDSTONE, 32);
        quickAdd(Material.REPEATER, 8);
        quickAdd(Material.TRAPPED_CHEST, 3);
        quickAdd(Material.COMPARATOR, 3);
        quickAdd(Material.TNT, 6);
        quickAdd(Material.GRAVEL, 6);
    }

    @Override
    public void setArmor() {
    }

    @Override
    public void setEffects() {
    }
}
