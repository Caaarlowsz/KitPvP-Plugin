package de.superhellth.kitpvp.listener.kitlistener;

import de.superhellth.kitpvp.kits.Enderman;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

public class EndermanListener extends KitListener {

    public EndermanListener(Kitpvp plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (isPlayerFighting(player, Enderman.getInstance())) {
            if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.DAMAGE_RESISTANCE, 10, 10, false, false));
            }
        }
    }

    @EventHandler
    public void onPlayerSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (isPlayerFighting(player, Enderman.getInstance())) {
            if (event.isSneaking()) {
                Player target = getTarget(player);
                if (target == null) {
                    return;
                }
                Location loc1 = player.getLocation();
                Location loc2 = target.getLocation();
                player.teleport(loc2);
                target.teleport(loc1);
            }
        }
    }

    private Player getTarget(Player player) {
        List<Entity> nearbyE = player.getNearbyEntities(Enderman.REACH, Enderman.REACH, Enderman.REACH);
        List<Player> players = new ArrayList<>();

        for (Entity e : nearbyE) {
            if (e instanceof Player) {
                players.add((Player) e);
            }
        }

        BlockIterator bItr = new BlockIterator(player, Enderman.REACH);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {
            block = bItr.next();
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            // check for entities near this block in the line of sight
            for (Player p : players) {
                loc = p.getLocation();
                ex = loc.getX();
                ey = loc.getY();
                ez = loc.getZ();
                if ((bx - .75 <= ex && ex <= bx + 1.75) && (bz - .75 <= ez && ez <= bz + 1.75) && (by - 1 <= ey && ey <= by + 2.5)) {
                    // entity is close enough, set target and stop
                    return p;
                }
            }
        }
        return null;
    }

}
