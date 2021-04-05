package de.superhellth.kitpvp.listener.kitlistener;

import de.superhellth.kitpvp.kits.Zeus;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZeusListener extends KitListener {

    public ZeusListener(Kitpvp plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (isPlayerFighting(player, Zeus.getInstance())) {
            if (player.getInventory().getItemInMainHand().getType() == Zeus.TRIGGER_MAT) {
                if (player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                    summonLightning(player);
                }
            }
        }
    }

    /**
     * Summon 3 lightning strikes at the players location.
     *
     * @param player uses this players location
     */
    private void summonLightning(Player player) {
        Location playerLocation = player.getLocation();
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 3, 100, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 10, 0, true));
        for (int i = 0; i < 3; i++) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
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
