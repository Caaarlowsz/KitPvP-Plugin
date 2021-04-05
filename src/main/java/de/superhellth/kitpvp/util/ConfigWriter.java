package de.superhellth.kitpvp.util;

import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.Location;

public class ConfigWriter {

    public static void updateCenter(Location newCenter) {
        Kitpvp.getInstance().getConfig().set(Kitpvp.MAP_CENTER, newCenter.serialize());
        Kitpvp.getInstance().saveConfig();
    }

    public static void updateSize(int newSize) {
        Kitpvp.getInstance().getConfig().set(Kitpvp.MAP_SIZE, newSize);
        Kitpvp.getInstance().saveConfig();
    }

}
