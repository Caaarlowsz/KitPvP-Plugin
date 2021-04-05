package de.superhellth.kitpvp.listener.kitlistener;

import de.superhellth.kitpvp.kits.Soup;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SoupListener extends KitListener {

    public SoupListener(Kitpvp plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (isPlayerFighting(player, Soup.getInstance())) {
            if (player.getInventory().getItemInMainHand().getType() == Material.MUSHROOM_STEW) {
                if (player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
                    return;
                }
                soup(player);
            }
        }
    }

    private void soup(Player player) {
        int max = (int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        int diff = (int) (player.getHealth() + Soup.getInstance().getSoupHeal());
        player.setHealth(Math.min(max, diff));
        player.getInventory().setItemInMainHand(new ItemStack(Material.BOWL, 1));
    }
}
