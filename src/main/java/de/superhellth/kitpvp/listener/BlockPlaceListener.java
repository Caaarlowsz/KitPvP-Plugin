package de.superhellth.kitpvp.listener;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.game.Phase;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (!(Kitpvp.getInstance().isInGame(player))) {
            return;
        }
        Game game = Kitpvp.getInstance().getGame(player);
        if (game.getCurrentPhase() == Phase.FIGHTING || game.getCurrentPhase() == Phase.GRACE) {
            game.addPlacedBlock(block);
        }
    }

}
