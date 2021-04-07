package de.superhellth.kitpvp.listener.kitlistener;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.kits.Dolphin;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DolphinListener extends KitListener {
    public DolphinListener(Kitpvp plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (isPlayerFighting(player, Dolphin.getInstance())) {
            if (player.getLocation().getBlock().getType() == Material.WATER) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999 * 20,
                        1, false, false));
                player.removePotionEffect(PotionEffectType.WEAKNESS);
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 99999 * 20,
                        1, false, false));
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
        }
    }
}
