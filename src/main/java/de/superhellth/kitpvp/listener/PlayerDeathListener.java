package de.superhellth.kitpvp.listener;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private Kitpvp plugin;

    public PlayerDeathListener(Kitpvp plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Game game = plugin.getGame(player);

        if (game == null) {
            return;
        }
        game.broadcast(player.getDisplayName() + " died and is now out!");
        game.getMembers().remove(player);
        player.setGameMode(GameMode.SPECTATOR);

        // check if second to last player died
        if (game.getMembers().size() == 1) {
            game.broadcast("The game is over! The winner is "
                    + game.getMembers().get(0).getDisplayName() + "! Congratulations!");
        }
    }

}
