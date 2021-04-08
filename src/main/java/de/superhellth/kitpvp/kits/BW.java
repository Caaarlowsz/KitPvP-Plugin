package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Arrays;

public class BW extends Kit {

    // singleton pattern
    private static BW instance;
    public static BW getInstance() {
        if (BW.instance == null) {
            BW.instance = new BW();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Bedwars";
    }

    @Override
    public void setColor() {
        color = ChatColor.DARK_AQUA;
    }

    @Override
    public void setView() {
        view = Material.RED_BED;
    }

    @Override
    public void setLore() {
        lore = Arrays.asList("Can't mine naturally generated Blocks");
    }

    @Override
    public void setItems() {
        // Sword
        ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        sword.addEnchantment(Enchantment.DURABILITY, 1);
        sword.addEnchantment(Enchantment.KNOCKBACK, 1);
        sword.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.setDisplayName(color + "Iron Sword");
        sword.setItemMeta(swordMeta);
        items.add(sword);

        // Bow
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.getItemMeta().setDisplayName(color + "Bow III");
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
        bow.addEnchantment(Enchantment.DURABILITY, 3);
        items.add(bow);

        // Pickaxe
        ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE);
        pickaxe.addEnchantment(Enchantment.DIG_SPEED, 1);
        pickaxe.addEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta pickMeta = pickaxe.getItemMeta();
        pickMeta.setDisplayName(color + "Iron Pickaxe");
        items.add(pickaxe);

        // Rest I
        quickAdd(Material.COOKED_PORKCHOP, 32);
        quickAdd(Material.SANDSTONE, 64);
        quickAdd(Material.SANDSTONE, 64);

        // Potion
        ItemStack pot = new ItemStack(Material.POTION, 1);
        PotionMeta potMeta = (PotionMeta) pot.getItemMeta();
        potMeta.setBasePotionData(new PotionData(PotionType.STRENGTH, false, true));
        pot.setItemMeta(potMeta);
        items.add(pot);

        // Rest II
        quickAdd(Material.ENDER_PEARL, 1);
        quickAdd(Material.COBWEB, 8);
        quickAdd(Material.SANDSTONE, 64);
        quickAdd(Material.SANDSTONE, 64);
        quickAdd(Material.SANDSTONE, 64);
        quickAdd(Material.ARROW, 64);
        ItemStack bridges = new ItemStack(Material.BRICK_SLAB, 10);
        ItemMeta bridgeMeta = bridges.getItemMeta();
        bridgeMeta.setLore(Arrays.asList("Gomme bw bridges"));
        bridges.setItemMeta(bridgeMeta);
        items.add(bridges);
    }

    @Override
    public void setArmor() {
        // Helmet
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        helmet.addEnchantment(Enchantment.DURABILITY, 1);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(Color.AQUA);
        helmet.setItemMeta(helmetMeta);
        armor[3] = helmet;

        // Chestplate
        ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        chest.addEnchantment(Enchantment.DURABILITY, 1);
        chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        armor[2] = chest;

        // Leggings
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leggings.addEnchantment(Enchantment.DURABILITY, 1);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        leggingsMeta.setColor(Color.AQUA);
        leggings.setItemMeta(leggingsMeta);
        armor[1] = leggings;

        // Boots
        ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_FALL, 3);
        armor[0] = boots;
    }

    @Override
    public void setEffects() {

    }
}
