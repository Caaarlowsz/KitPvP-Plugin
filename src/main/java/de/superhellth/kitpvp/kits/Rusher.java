package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class Rusher extends Kit {

    // singleton pattern
    private static Rusher instance;
    public static Rusher getInstance() {
        if (Rusher.instance == null) {
            Rusher.instance = new Rusher();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Rusher";
    }

    @Override
    public void setColor() {
        color = ChatColor.GOLD;
    }

    @Override
    public void setView() {
        view = Material.STONE_SWORD;
    }

    @Override
    public void setLore() {
        lore = Arrays.asList("");
    }

    @Override
    public void setItems() {
        quickAdd(Material.STONE_SWORD, 1);
        quickAdd(Material.OAK_PLANKS, 64);
        quickAdd(Material.COOKED_BEEF, 3);
    }

    @Override
    public void setArmor() {
        armor[3] = new ItemStack(Material.LEATHER_HELMET);
        armor[2] = new ItemStack(Material.LEATHER_CHESTPLATE);
        armor[1] = new ItemStack(Material.LEATHER_LEGGINGS);
        armor[0] = new ItemStack(Material.LEATHER_BOOTS);
    }

    @Override
    public void setEffects() {
        effects.add(new PotionEffect(PotionEffectType.SPEED, 4 * 60 * 20, 0, false, false));
    }
}
