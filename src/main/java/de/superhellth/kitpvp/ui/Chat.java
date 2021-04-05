package de.superhellth.kitpvp.ui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Chat {

    public final static ChatColor BASE_COLOR = ChatColor.DARK_GRAY;
    public final static String PREFIX = ChatColor.BLUE + "" +  ChatColor.BOLD + "KitPvP§r " + BASE_COLOR + "";

    public static void sendMessage(Player player, String message) {
        player.sendMessage(PREFIX + message);
    }

}
