package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public abstract class Kit {

    // kit attributes
    protected String name;
    protected ChatColor color;
    protected List<ItemStack> items;
    protected ItemStack[] armor;
    protected List<PotionEffect> effects;
    protected Material view;
    protected List<String> lore;

    protected Kit() {
        setName();
        setColor();
        setView();
        setLore();
        items = new ArrayList<>();
        setItems();
        armor = new ItemStack[4];
        setArmor();
        effects = new ArrayList<>();
        setEffects();
    }

    // Getter
    public String getName() {
        return name;
    }
    public ChatColor getColor() {
        return color;
    }
    public List<String> getLore() {
        return lore;
    }
    public List<ItemStack> getItems() {
        return items;
    }
    public ItemStack[] getArmor() {
        return armor;
    }
    public List<PotionEffect> getEffects() {
        return effects;
    }
    public Material getView() {
        return view;
    }

    // Setter
    public abstract void setName();
    public abstract void setColor();
    public abstract void setView();
    public abstract void setLore();
    public abstract void setItems();
    public abstract void setArmor();
    public abstract void setEffects();

    // Helper
    protected void quickAdd(Material mat, int amount) {
        ItemStack stack = new ItemStack(mat, amount);
        items.add(stack);
    }

}
