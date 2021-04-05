package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Miner extends Kit {

    // singleton pattern
    private static Miner instance;
    public static Miner getInstance() {
        if (Miner.instance == null) {
            Miner.instance = new Miner();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Miner";
    }

    @Override
    public void setColor() {
        color = ChatColor.DARK_BLUE;
    }

    @Override
    public void setItems() {
        // Pickaxe
        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pickaxe.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        pickaxe.addEnchantment(Enchantment.DIG_SPEED, 1);
        pickaxe.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
        ItemMeta pickMeta = pickaxe.getItemMeta();
        pickMeta.setDisplayName(color + "§kaaa§r " + color + "Miner's Pickaxe §kaaa");
        pickaxe.setItemMeta(pickMeta);
        items.add(pickaxe);

        // Shovel
        ItemStack shovel = new ItemStack(Material.DIAMOND_SHOVEL, 1);
        shovel.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        shovel.addEnchantment(Enchantment.DIG_SPEED, 2);
        ItemMeta shovelMeta = pickaxe.getItemMeta();
        shovelMeta.setDisplayName(color + "§kaaa§r " + color + "Miner's Shovel §kaaa");
        shovel.setItemMeta(shovelMeta);
        items.add(shovel);
    }

    @Override
    public void setArmor() {
    }

    @Override
    public void setEffects() {
        PotionEffect nightVision = new PotionEffect(PotionEffectType.NIGHT_VISION, 999999 * 20, 0);
        effects.add(nightVision);
        PotionEffect haste = new PotionEffect(PotionEffectType.FAST_DIGGING, 999999 * 20, 0);
        effects.add(haste);
    }
}
