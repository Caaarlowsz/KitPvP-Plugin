package de.superhellth.kitpvp.game;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class KitSelector extends ItemStack {

    public KitSelector() {
        setType(Material.CHEST);
        setAmount(1);
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Kit Selector");
        meta.setLore(Arrays.asList("Right click to select your kit!"));
        setItemMeta(meta);
    }
}
