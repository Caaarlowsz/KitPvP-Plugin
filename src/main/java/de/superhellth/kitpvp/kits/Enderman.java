package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;

// Special ability: Doesn't take ender pearl damage
public class Enderman extends Kit {

    public static final int REACH = 16;

    // singleton pattern
    private static Enderman instance;
    public static Enderman getInstance() {
        if (Enderman.instance == null) {
            Enderman.instance = new Enderman();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Enderman";
    }

    @Override
    public void setColor() {
        color = ChatColor.BLACK;
    }

    @Override
    public void setView() {
        view = Material.ENDER_PEARL;
    }

    @Override
    public void setLore() {
        lore = Arrays.asList("Sneak to change position with a mob or player");
    }

    @Override
    public void setItems() {
        quickAdd(Material.ENDER_PEARL, 6);
    }

    @Override
    public void setArmor() {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();
        //boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
        meta.setColor(Color.PURPLE);
        boots.setItemMeta(meta);
        armor[0] = boots;
    }

    @Override
    public void setEffects() {

    }
}
