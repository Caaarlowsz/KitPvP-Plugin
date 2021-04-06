package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;

public class Soup extends Kit {

    private final int soupAmount = 18;
    private final int soupHeal = 5;

    // singleton pattern
    private static Soup instance;
    public static Soup getInstance() {
        if (Soup.instance == null) {
            Soup.instance = new Soup();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Soup";
    }

    @Override
    public void setColor() {
        color = ChatColor.YELLOW;
    }

    @Override
    public void setView() {
        view = Material.MUSHROOM_STEW;
    }

    @Override
    public void setLore() {
        lore = Arrays.asList("Consuming a soup grants " + soupHeal + " HP");
    }

    @Override
    public void setItems() {
        quickAdd(Material.BOWL, soupAmount);
        quickAdd(Material.BROWN_MUSHROOM, soupAmount);
        quickAdd(Material.RED_MUSHROOM, soupAmount);
    }

    @Override
    public void setArmor() {
    }

    @Override
    public void setEffects() {
    }

    public int getSoupHeal() {
        return soupHeal;
    }
}
