package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Farmer extends Kit {

    // singleton pattern
    private static Farmer instance;
    public static Farmer getInstance() {
        if (Farmer.instance == null) {
            Farmer.instance = new Farmer();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Farmer";
    }

    @Override
    public void setColor() {
        color = ChatColor.GREEN;
    }

    @Override
    public void setView() {
        view = Material.WHEAT;
    }

    @Override
    public void setLore() {
        lore = Arrays.asList("");
    }

    @Override
    public void setItems() {
        // Netherite Hoe
        ItemStack hoe = new ItemStack(Material.NETHERITE_HOE, 1);
        hoe.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        hoe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 5);
        hoe.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 5);
        ItemMeta hoeMeta = hoe.getItemMeta();
        hoeMeta.setDisplayName(color + "§kaaa§r " + color + "Farmer's Hoe §kaaa");
        hoe.setItemMeta(hoeMeta);
        items.add(hoe);

        // Bone meal
        quickAdd(Material.BONE_MEAL, 64);
        // Potatoes and Carrots
        quickAdd(Material.POTATO, 16);
        quickAdd(Material.CARROT, 16);
        // Hay bale
        quickAdd(Material.HAY_BLOCK, 8);
        // cats
        quickAdd(Material.CAT_SPAWN_EGG, 2);
        quickAdd(Material.COD, 16);
        // Water Bucket
        quickAdd(Material.WATER_BUCKET, 2);
    }

    @Override
    public void setArmor() {

    }

    @Override
    public void setEffects() {
    }
}
