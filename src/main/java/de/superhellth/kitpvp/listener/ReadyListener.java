package de.superhellth.kitpvp.listener;

import de.superhellth.kitpvp.events.ReadyEvent;
import de.superhellth.kitpvp.main.Kitpvp;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ReadyListener implements Listener {

    private Kitpvp plugin;

    public ReadyListener(Kitpvp plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onReadyEvent(ReadyEvent event) {
        plugin.getGame(event.player).checkPlayer(event.player);
    }

}
