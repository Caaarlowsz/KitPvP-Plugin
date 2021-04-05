package de.superhellth.kitpvp.listener.kitlistener;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.game.Phase;
import de.superhellth.kitpvp.kits.Kit;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class KitListener implements Listener {

    protected final Kitpvp plugin;

    public KitListener(Kitpvp plugin) {
        this.plugin = plugin;
    }

    /**
     * Check whether or not player is in a game, if the game is
     * in the fighting phase and if the player has the correct kit
     * @param player
     * @param kit
     * @return
     */
    protected boolean isPlayerFighting(Player player, Kit kit) {
            if (!plugin.isInGame(player)) {
                return false;
            }

            Game game = plugin.getGame(player);
            if (game.getCurrentPhase() == Phase.FIGHTING) {
                return game.getSelectedKits().get(player) == kit;
            }

            return false;
    }

}

