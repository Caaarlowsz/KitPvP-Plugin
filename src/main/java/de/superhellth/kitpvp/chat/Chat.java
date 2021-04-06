package de.superhellth.kitpvp.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Chat {

    // Base color and prefix for all chat messages
    public final static ChatColor BASE_COLOR = ChatColor.DARK_GRAY;
    public final static String PREFIX = ChatColor.BLUE + "" +  ChatColor.BOLD + "KitPvP§r " + BASE_COLOR + "";

    // Error messages

    // Command outputs


    /**
     * Sends a color message with prefix to the player
     * @param player Player to send message to
     * @param message Message the player receives
     */
    public static void sendMessage(Player player, String message) {
        player.sendMessage(PREFIX + message);
    }
}
