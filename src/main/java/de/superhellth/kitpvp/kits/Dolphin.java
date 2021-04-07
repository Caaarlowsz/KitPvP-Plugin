package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class Dolphin extends Kit {

    // singleton pattern
    private static Dolphin instance;
    public static Dolphin getInstance() {
        if (Dolphin.instance == null) {
            Dolphin.instance = new Dolphin();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Dolphin";
    }

    @Override
    public void setColor() {
        color = ChatColor.BLUE;
    }

    @Override
    public void setView() {
        view = Material.WATER_BUCKET;
    }

    @Override
    public void setLore() {
        lore = Arrays.asList("Has strength when in water but weakness on land");
    }

    @Override
    public void setItems() {
    }

    @Override
    public void setArmor() {
    }

    @Override
    public void setEffects() {
        effects.add(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 99999 * 20, 1, false, false));
    }
}
