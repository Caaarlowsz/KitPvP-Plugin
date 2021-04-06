package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class Pyro extends Kit {

    // singleton pattern
    private static Pyro instance;
    public static Pyro getInstance() {
        if (Pyro.instance == null) {
            Pyro.instance = new Pyro();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Pyro";
    }

    @Override
    public void setColor() {
        color = ChatColor.RED;
    }

    @Override
    public void setView() {
        view = Material.LAVA_BUCKET;
    }

    @Override
    public void setLore() {
        lore = Arrays.asList("");
    }

    @Override
    public void setItems() {
        quickAdd(Material.FLINT_AND_STEEL, 1);
        quickAdd(Material.LAVA_BUCKET, 1);
        quickAdd(Material.LAVA_BUCKET, 1);
        quickAdd(Material.LAVA_BUCKET, 1);
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
        meta.addStoredEnchant(Enchantment.FIRE_ASPECT, 1, false);
        book.setItemMeta(meta);
        items.add(book);
    }

    @Override
    public void setArmor() {
    }

    @Override
    public void setEffects() {
        effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999 * 20, 0, false, false));
    }
}
