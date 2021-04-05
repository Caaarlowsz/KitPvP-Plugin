package de.superhellth.kitpvp.kits;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class Jesus extends Kit {

    // singleton pattern
    private static Jesus instance;
    public static Jesus getInstance() {
        if (Jesus.instance == null) {
            Jesus.instance = new Jesus();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Jesus";
    }

    @Override
    public void setColor() {
        color = ChatColor.WHITE;
    }

    @Override
    public void setItems() {
        quickAdd(Material.TOTEM_OF_UNDYING, 1);
    }

    @Override
    public void setArmor() {
        // Boots
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        boots.addEnchantment(Enchantment.DEPTH_STRIDER, 1);
        boots.addEnchantment(Enchantment.DURABILITY, 3);
        LeatherArmorMeta bootMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootMeta.setColor(Color.WHITE);
        bootMeta.setDisplayName(color + "Jesus Boots");
        boots.setItemMeta(bootMeta);
        armor[0] = boots;
    }

    @Override
    public void setEffects() {
        PotionEffect grace = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 99999 * 20, 0);
        effects.add(grace);
        PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING, 90 * 20, 0);
        effects.add(glowing);
    }
}
