package de.superhellth.kitpvp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReadyEvent extends Event {

    public Player player;

    public ReadyEvent(Player player) {
        this.player = player;
    }

    // basic event stuff
    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
