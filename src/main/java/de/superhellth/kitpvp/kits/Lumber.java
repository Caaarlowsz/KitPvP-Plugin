package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

// special ability: can mine entire trees
public class Lumber extends Kit {

    // singleton pattern
    private static Lumber instance;
    public static Lumber getInstance() {
        if (Lumber.instance == null) {
            Lumber.instance = new Lumber();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Lumber";
    }

    @Override
    public void setColor() {
        color = ChatColor.DARK_GREEN;
    }

    @Override
    public void setItems() {
        // Axe
        ItemStack axe = new ItemStack(Material.WOODEN_AXE, 1);
        axe.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        axe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 3);
        ItemMeta axeMeta = axe.getItemMeta();
        axeMeta.setDisplayName(color + "§kaaa§r " + color + "Lumberaxe §kaaa");
        axe.setItemMeta(axeMeta);
        items.add(axe);

        // trees
        quickAdd(Material.DIRT, 10);
        quickAdd(Material.BONE_MEAL, 32);
        quickAdd(Material.OAK_SAPLING, 10);
    }

    @Override
    public void setArmor() {
    }

    @Override
    public void setEffects() {
    }
}
