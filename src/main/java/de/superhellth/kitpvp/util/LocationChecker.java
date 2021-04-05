package de.superhellth.kitpvp.util;

import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocationChecker {

    private List<Material> accepted;

    public LocationChecker() {
        accepted = new ArrayList<>();
        accepted.add(Material.GRASS);
        accepted.add(Material.DIRT);
        accepted.add(Material.STONE);
        accepted.add(Material.MYCELIUM);
        accepted.add(Material.PODZOL);
        accepted.add(Material.SAND);
        accepted.add(Material.SANDSTONE);
        accepted.add(Material.TERRACOTTA);
        accepted.add(Material.ACACIA_LEAVES);
        accepted.add(Material.OAK_LEAVES);
        accepted.add(Material.BIRCH_LEAVES);
        accepted.add(Material.DARK_OAK_LEAVES);
        accepted.add(Material.SPRUCE_LEAVES);
        accepted.add(Material.JUNGLE_LEAVES);
        accepted.add(Material.MELON);
        accepted.add(Material.PUMPKIN);
        accepted.add(Material.NETHERRACK);
        accepted.add(Material.SOUL_SAND);
        accepted.add(Material.SOUL_SOIL);
        accepted.add(Material.BASALT);
        accepted.add(Material.BLACKSTONE);
        accepted.add(Material.END_STONE);
    }

    public Location findSafeSpot(Location center, int size) {
        Random random = new Random();
        Location currentGuess = null;
        while (true) {
            int xGuess = center.getBlockX() + random.nextInt(size / 2) - (size / 2);
            int zGuess = center.getBlockZ() + random.nextInt(size / 2) - (size / 2);
            int yGuess;
            for (int i = 40; i < 128; i++) {
                yGuess = i;
                currentGuess = new Location(center.getWorld(), xGuess, yGuess, zGuess);
                if (isSafe(currentGuess)) {
                    break;
                }
            }
            if (isSafe(currentGuess)) {
                break;
            }
        }
        // comment
        currentGuess.setY(currentGuess.getBlockY() + 1);

        return currentGuess;
    }

    private boolean isSafe(Location location) {
        // check if block is acceptable
        if (accepted.contains(location.getBlock().getType())) {

            // check if there is air
            Location aLoc;
            for (int i = 1; i < 3; i++) {
                aLoc = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + i, location.getBlockZ());
                if (!aLoc.getBlock().isPassable()) {
                    return false;
                }
            }

            // check if there is lava
            for (int y = 0; y < 4; y++) {
                for (int z = -1; z < 2; z++) {
                    for (int x = -1; x < 2; x++) {
                        Location bLoc = new Location(location.getWorld(), location.getBlockX() + x,
                                location.getBlockY() + y, location.getBlockZ() + z);
                        if (bLoc.getBlock().getType() == Material.LAVA) {
                            return false;
                        }
                    }
                }
            }

            // check if has skylight access
            if (location.getWorld().getEnvironment() == World.Environment.NORMAL) {
                return hasSkylight(location);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean hasSkylight(Location location) {
        for (int i = 256; i > location.getBlockY(); i--) {
            Location aLoc = new Location(location.getWorld(), location.getBlockX(), i, location.getBlockZ());
            if (!aLoc.getBlock().isPassable()) {
                return false;
            }
        }

        return true;
    }

}
