package de.superhellth.kitpvp.listener;

import de.superhellth.kitpvp.game.Phase;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class GraceListener implements Listener {

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            Kitpvp plugin = Kitpvp.getInstance();
            if (plugin.isInGame(damaged)) {
                if (plugin.getGame(damaged).getCurrentPhase() == Phase.GRACE) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
