package de.superhellth.kitpvp.listener.kitlistener;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.kits.BW;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BWListener extends KitListener {

    public BWListener(Kitpvp plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Game game = plugin.getGame(player);
        if (isPlayerFighting(player, BW.getInstance())) {
            if (!(game.getPlacedBlocks().contains(block))) {
                event.setCancelled(true);
            }
        }
    }

}
