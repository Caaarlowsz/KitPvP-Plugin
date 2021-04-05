package de.superhellth.kitpvp.listener.kitlistener;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.game.Phase;
import de.superhellth.kitpvp.kits.Witch;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class WitchListener extends KitListener {

    List<PotionEffectType> ignore;

    public WitchListener(Kitpvp plugin) {
        super(plugin);
        ignore = new ArrayList<>();
        ignore.add(PotionEffectType.POISON);
        ignore.add(PotionEffectType.HARM);
        ignore.add(PotionEffectType.SLOW);
        ignore.add(PotionEffectType.WEAKNESS);
    }

    @EventHandler
    public void onEntityPotionEffectEvent(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (checkCondition(player, Witch.getInstance())) {
            if (event.getAction() == EntityPotionEffectEvent.Action.ADDED) {
                if (ignore.contains(event.getModifiedType())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (checkCondition(player, Witch.getInstance())) {
            if (event.getCause() == EntityDamageEvent.DamageCause.MAGIC) {
                event.setCancelled(true);
            }
        }
    }
}
