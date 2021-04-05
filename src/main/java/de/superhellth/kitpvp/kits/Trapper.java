package de.superhellth.kitpvp.kits;

import org.bukkit.ChatColor;

public class Trapper extends Kit {

    // singleton pattern
    private static Trapper instance;
    public static Trapper getInstance() {
        if (Trapper.instance == null) {
            Trapper.instance = new Trapper();
        }

        return instance;
    }

    @Override
    public void setName() {
        name = "Trapper";
    }

    @Override
    public void setColor() {
        color = ChatColor.DARK_GRAY;
    }

    @Override
    public void setItems() {

    }

    @Override
    public void setArmor() {

    }

    @Override
    public void setEffects() {

    }
}
