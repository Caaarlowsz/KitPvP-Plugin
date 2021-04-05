package de.superhellth.kitpvp.listener.kitlistener;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.game.Phase;
import de.superhellth.kitpvp.kits.Kit;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class KitListener implements Listener {

    private final Kitpvp plugin;

    public KitListener(Kitpvp plugin) {
        this.plugin = plugin;
    }

    protected boolean checkCondition(Player player, Kit kit) {
            if (!plugin.isInGame(player)) {
                return false;
            }

            Game game = plugin.getGame(player);
            if (game.getCurrentPhase() == Phase.fighting) {
                return game.getSelectedKits().get(player) == kit;
            }

            return false;
    }

}

