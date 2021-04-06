package de.superhellth.kitpvp.listener;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.game.Phase;
import de.superhellth.kitpvp.kits.Kit;
import de.superhellth.kitpvp.main.Kitpvp;
import de.superhellth.kitpvp.chat.Chat;
import de.superhellth.kitpvp.util.KitInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class KitSelectionListener implements Listener {

    @EventHandler
    public void onSelectionOpened(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (Kitpvp.getInstance().isInGame(player)) {
            Game game = Kitpvp.getInstance().getGame(player);
            if (game.getCurrentPhase() == Phase.KIT_SELECTION) {
                if (event.hasItem()) {
                    if (event.getItem().getType() == Material.CHEST) {
                        Inventory selectInv = KitInventory.getInstance().getInv();
                        player.openInventory(selectInv);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onKitSelect(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (Objects.equals(player.getOpenInventory().getTopInventory(), KitInventory.getInstance().getInv())) {
            event.setCancelled(true);
            if (Objects.equals(event.getClickedInventory(), KitInventory.getInstance().getInv())) {
                Kitpvp plugin = Kitpvp.getInstance();
                Game game = plugin.getGame(player);
                if (event.getCurrentItem() != null) {
                    Kit selected = plugin.getKit(event.getCurrentItem().getType());
                    game.selectKit(player, selected);
                    Chat.sendMessage(player, "You have selected the " + selected.getColor() + "" + ChatColor.BOLD +
                            selected.getName() + "§r" + Chat.BASE_COLOR + " kit!");
                }
                player.getOpenInventory().close();
            }
        } else if (Objects.equals(event.getClickedInventory(), player.getInventory())) {
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getType() == Material.CHEST) {
                    if (Kitpvp.getInstance().getGame((Player) event.getWhoClicked()).getCurrentPhase() == Phase.KIT_SELECTION) {
                        event.setCancelled(true);
                    }
                } else if (event.getCurrentItem().getType() == Material.AIR) {
                    if (event.getHotbarButton() == 8) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (Kitpvp.getInstance().isInGame(player)) {
            Game game = Kitpvp.getInstance().getGame(player);
            if (game.getCurrentPhase() == Phase.KIT_SELECTION) {
                if (event.getItemDrop().getItemStack().getType() == Material.CHEST) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
