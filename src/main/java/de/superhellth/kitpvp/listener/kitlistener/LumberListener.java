package de.superhellth.kitpvp.listener.kitlistener;

import de.superhellth.kitpvp.kits.Lumber;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class LumberListener extends KitListener {

    private List<Material> logs;

    public LumberListener(Kitpvp plugin) {
        super(plugin);
        logs = new ArrayList<>();
        logs.add(Material.OAK_LOG);
        logs.add(Material.BIRCH_LOG);
        logs.add(Material.DARK_OAK_LOG);
        logs.add(Material.SPRUCE_LOG);
        logs.add(Material.ACACIA_LOG);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLoc = block.getLocation();
        if (isPlayerFighting(player, Lumber.getInstance())) {
            if (player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DURABILITY) == 10) {
                if (logs.contains(block.getType())) {
                    chopTree(blockLoc);
                }
            }
        }
    }

    // chop tree
    private void chopTree(Location blockLoc) {
        // scan tree for more oak logs
        for (int y = -31; y < 31; y++) {
            for (int x = -2; x < 2; x++) {
                for (int z = -2; z < 2; z++) {
                    Location newLocation = new Location(blockLoc.getWorld(), blockLoc.getBlockX() + x,
                            blockLoc.getBlockY() + y, blockLoc.getBlockZ() + z);
                    Block newBlock = newLocation.getBlock();
                    if (logs.contains(newBlock.getType())) {
                        newBlock.breakNaturally();
                    }
                }
            }
        }
    }
}
