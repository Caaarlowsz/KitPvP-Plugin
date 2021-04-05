package de.superhellth.kitpvp.listener.kitlistener;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.game.Phase;
import de.superhellth.kitpvp.kits.Lumber;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class LumberListener implements Listener {

    private List<Material> logs;

    public LumberListener() {
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
        if (!(Kitpvp.getInstance().isInGame(player))) {
            return;
        }
        Game game = Kitpvp.getInstance().getGame(player);
        if (game.getCurrentPhase() == Phase.fighting) {
            if (game.getSelectedKits().get(player) == Lumber.getInstance()) {
                if (player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DURABILITY) == 10) {
                    if (logs.contains(block.getType())) {

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
            }
        }
    }

}
