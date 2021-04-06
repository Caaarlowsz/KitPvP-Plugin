package de.superhellth.kitpvp.util;

import de.superhellth.kitpvp.kits.Kit;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitInventory {

    private static Inventory inventory;

    // singleton patter
    private static KitInventory instance;

    public static KitInventory getInstance() {
        if (instance == null) {
            instance = new KitInventory();
        }
        return instance;
    }

    public KitInventory() {
        inventory = Bukkit.createInventory(null, 18, "Select your Kit!");
        for (Kit kit : Kitpvp.getInstance().getKits()) {
            ItemStack kitItem = new ItemStack(kit.getView(), 1);
            ItemMeta meta = kitItem.getItemMeta();
            meta.setDisplayName(kit.getColor() + "" + ChatColor.BOLD + kit.getName());
            List<String> desc = getFullDesc(kit);
            meta.setLore(desc);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            kitItem.setItemMeta(meta);
            inventory.addItem(kitItem);
        }
    }

    public Inventory getInv() {
        return inventory;
    }

    private List<String> getFullDesc(Kit kit) {
        List<String> desc = new ArrayList<>();
        if (!kit.getLore().get(0).equals("")) {
            desc.add("Special ability:");
            desc.addAll(kit.getLore());
        }
        if (kit.getItems().size() > 0) {
            if (desc.size() != 0) {
                desc.add("");
            }
            desc.add("Items:");
            for (ItemStack item : kit.getItems()) {
                if (!item.getItemMeta().getDisplayName().equals("")) {
                    desc.add(" - " + item.getAmount() + " " + item.getItemMeta().getDisplayName());
                } else {
                    desc.add(" - " + item.getAmount() + " " + getNormalizedName(item.getType().toString()));
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            ItemStack currentArmor = kit.getArmor()[i];
            if (currentArmor != null) {
                if (!desc.contains("Armor:")) {
                    if (desc.get(desc.size() - 1).contains("-")) {
                        desc.add("");
                    }
                    desc.add("Armor:");
                }
                if (!currentArmor.getItemMeta().getDisplayName().equals("")) {
                    desc.add(" - " + currentArmor.getItemMeta().getDisplayName());
                } else {
                    desc.add(" - " + getNormalizedName(currentArmor.getType().toString()));
                }
            }
        }

        if (kit.getEffects().size() > 0) {
            if (desc.get(desc.size() - 1).contains("-")) {
                desc.add("");
            }
            desc.add("Effects:");
            for (PotionEffect effect : kit.getEffects()) {
                String duration = effect.getDuration() > 9999 * 20 ? "permanent" : TimeManager.getSecondsAsString(effect.getDuration() / 20);
                String effName = effect.getType().getName();
                effName = getNormalizedName(effName);
                desc.add(" - " + effName + ": " + duration);
            }
        }
        return desc;
    }

    private String getNormalizedName(String typeName) {
        typeName = typeName.replaceAll("_", " ");
        typeName = typeName.toLowerCase();
        return typeName;
    }

}
