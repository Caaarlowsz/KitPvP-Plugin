package de.superhellth.kitpvp.listener.kitlistener;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.kits.BW;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BWListener extends KitListener {

    public BWListener(Kitpvp plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Game game = plugin.getGame(player);
        if (isPlayerFighting(player, BW.getInstance())) {
            if (!(game.getPlacedBlocks().contains(block))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (isPlayerFighting(player, BW.getInstance())) {
            if (event.getItem() != null) {
                ItemStack item = event.getItem();
                if (item.getType() == Material.BRICK_SLAB && item.getItemMeta().hasLore()) {
                    Game game = plugin.getGame(player);
                    Location playerLoc = player.getLocation();
                    Location currentLoc = new Location(playerLoc.getWorld(), playerLoc.getBlockX(), playerLoc.getBlockY() - 1,
                            playerLoc.getBlockZ());
                    int yaw = Math.round(player.getLocation().getYaw() / 90) * 90;
                    int dx = 0;
                    int dz = 0;
                    switch (yaw) {
                        case 90:
                        case -270:
                            dx = -1;
                            break;

                        case 180:
                        case -180:
                            dz = -1;
                            break;

                        case 270:
                        case -90:
                            dx = 1;
                            break;

                        case 0:
                        case -360:
                        case 360:
                            dz = 1;
                            break;
                    }
                    for (int i = 1; i < 6; i++) {
                        Location blockLoc = new Location(currentLoc.getWorld(), currentLoc.getBlockX() + dx * i,
                                currentLoc.getBlockY(), currentLoc.getBlockZ() + dz * i);
                        setBlock(player, blockLoc, game, i * 2);
                    }
                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                    event.setCancelled(true);
                }
            }
        }
    }

    private void setBlock(Player player, Location location, Game game, long delay) {
        if (location.getBlock().getType() == Material.AIR) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                location.getBlock().setType(Material.SANDSTONE);
                game.getPlacedBlocks().add(location.getBlock());
                player.playSound(location, Sound.BLOCK_SCAFFOLDING_PLACE, 0.5f, 1f);
            }, delay);
        }
    }

}
