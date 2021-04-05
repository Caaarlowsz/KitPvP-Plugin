package de.superhellth.kitpvp.listener.kitlistener;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.game.Phase;
import de.superhellth.kitpvp.kits.Kit;
import de.superhellth.kitpvp.kits.Zeus;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class ZeusListener implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (Kitpvp.getInstance().isInGame(player)) {
            Game game = Kitpvp.getInstance().getGame(player);
            if (game.getCurrentPhase() == Phase.fighting) {
                if (game.getSelectedKits().get(player) == Zeus.getInstance()) {
                    if (player.getInventory().getItemInMainHand().getType() == Zeus.TRIGGER_MAT) {
                        if (player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                            Location playerLocation = player.getLocation();
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 3, 100, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 10, 0, true));
                            for (int i = 0; i < 3; i++) {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Kitpvp.getInstance(), new Runnable() {
                                    @Override
                                    public void run() {
                                        playerLocation.getWorld().strikeLightning(playerLocation);
                                    }
                                }, 10L * i);
                            }
                            int initAmount = player.getInventory().getItemInMainHand().getAmount();
                            player.getInventory().getItemInMainHand().setAmount(--initAmount);
                        }
                    }
                }
            }
        }
    }

}
