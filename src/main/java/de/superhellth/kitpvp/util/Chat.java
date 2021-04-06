package de.superhellth.kitpvp.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Chat {

    // Base color and prefix for all chat messages
    public final static ChatColor BASE_COLOR = ChatColor.DARK_GRAY;
    public final static String PREFIX = ChatColor.BLUE + "" +  ChatColor.BOLD + "KitPvP§r " + BASE_COLOR + "";

    // Error messages
    public final static String TOO_FEW_ARGS = "Too few arguments!";
    public final static String TOO_MANY_ARGS = "Too many arguments!";
    public final static String NOT_RIGHT_ARGS = "Wrong amount of arguments!";
    public final static String UNKNOWN = "Unknown command!";

    // Command outputs
    public final static String HELP = "Commands: \n" +
            "/kitpvp newgame \n" +
            "/kitpvp invite <player> \n" +
            "/kitpvp start \n" +
            "/kitpvp leave \n" +
            "/kitpvp stop";
    public final static String CHANGED_MAP_CENTER = "Successfully changed map center!";
    public final static String CHANGED_MAP_SIZE = "Successfully changed map size!";

    /**
     * Sends a color message with prefix to the player
     * @param player Player to send message to
     * @param message Message the player receives
     */
    public static void sendMessage(Player player, String message) {
        player.sendMessage(PREFIX + message);
    }
}
