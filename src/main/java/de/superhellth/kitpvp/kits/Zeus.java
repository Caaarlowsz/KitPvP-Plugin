package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Zeus extends Kit {

    public static final Material TRIGGER_MAT = Material.BLAZE_ROD;

    // singleton pattern
    private static Zeus instance;
    public static Zeus getInstance() {
        if (Zeus.instance == null) {
            Zeus.instance = new Zeus();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Zeus";
    }

    @Override
    public void setColor() {
        color = ChatColor.GRAY;
    }

    @Override
    public void setView() {
        view = Material.BLAZE_ROD;
    }

    @Override
    public void setLore() {
        lore = Arrays.asList("");
    }

    @Override
    public void setItems() {
        ItemStack trigger = new ItemStack(TRIGGER_MAT, 4);
        ItemMeta meta = trigger.getItemMeta();
        meta.setDisplayName(color + "§kaaa§r " + color + "Lightning §kaaa");
        meta.setLore(Arrays.asList("Summon a lightning strike!"));
        trigger.setItemMeta(meta);
        items.add(trigger);
    }

    @Override
    public void setArmor() {
    }

    @Override
    public void setEffects() {
    }
}
