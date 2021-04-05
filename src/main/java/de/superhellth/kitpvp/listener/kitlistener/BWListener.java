package de.superhellth.kitpvp.listener.kitlistener;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.game.Phase;
import de.superhellth.kitpvp.kits.BW;
import de.superhellth.kitpvp.kits.Lumber;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BWListener implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (!(Kitpvp.getInstance().isInGame(player))) {
            return;
        }
        Game game = Kitpvp.getInstance().getGame(player);
        if (game.getCurrentPhase() == Phase.fighting) {
            if (game.getSelectedKits().get(player) == BW.getInstance()) {
                if (!(game.getPlacedBlocks().contains(block))) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
