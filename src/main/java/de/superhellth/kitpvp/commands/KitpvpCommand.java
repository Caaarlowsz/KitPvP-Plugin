package de.superhellth.kitpvp.commands;

import de.superhellth.kitpvp.events.ReadyEvent;
import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.game.Phase;
import de.superhellth.kitpvp.kits.Kit;
import de.superhellth.kitpvp.main.Kitpvp;
import de.superhellth.kitpvp.ui.Chat;
import de.superhellth.kitpvp.util.ConfigWriter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitpvpCommand implements CommandExecutor {

    private final Kitpvp plugin;

    // constructor
    public KitpvpCommand(Kitpvp plugin) {
        this.plugin = plugin;
        // register command
        plugin.getCommand("kitpvp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // player who ran command
        Player player = (Player) sender;

        if (args[0] == null) {
            args[0] = "error";
        }

        // check argument
        switch (args[0].toLowerCase()) {
            case "help":
                Chat.sendMessage(player, "Commands: \n" +
                        "/Kitpvp newgame \n" +
                        "/Kitpvp invite <player> \n" +
                        "/Kitpvp leave \n" +
                        "/Kitpvp stop \n" +
                        "/Kitpvp start");
                break;

            case "newgame":
                addGame(player);
                break;

            case "invite":
                try {
                    invite(player, args[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Chat.sendMessage(player, "Try /kitpvp invite <player>");
                }
                break;

            case "accept":
                try {
                    acceptInvite(player, args[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Chat.sendMessage(player, "Try /kitpvp accept <player>");
                }
                break;

            case "list":
                list(player);
                break;

            case "start":
                start(player);
                break;

            case "kits":
                listKits(player);
                break;

            case "select":
                try {
                    selectKit(player, args[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Chat.sendMessage(player, "Try /kitpvp select <kit>");
                }
                break;

            case "ready":
                ready(player);
                break;

            case "leave":
                leaveGame(player);
                break;

            case "stop":
                stopGame(player);
                break;

            case "config":
                config(player, args);
                break;

            default:
                Chat.sendMessage(player, "Unknown command!");
                break;

        }

        return true;
    }

    // manage config commands
    private void config(Player player, String[] args) {
        try {
            args[1] = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            Chat.sendMessage(player, ChatColor.BOLD + "Possible arguments:§r\n"
                        + Chat.BASE_COLOR + " mapsize\n mapcenter");
            return;
        }

        switch (args[1].toLowerCase()) {
            case "mapsize":
                try {
                    ConfigWriter.updateSize(Integer.parseInt(args[2]));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    Chat.sendMessage(player, "Type a number!");
                    return;
                }
                Chat.sendMessage(player, "Successfully changed map size!");
                break;

            case "mapcenter":
                ConfigWriter.updateCenter(player.getLocation());
                Chat.sendMessage(player, "Successfully changed map center!");
                break;

            default:
                Chat.sendMessage(player, "Unknown command");
                break;
        }
    }

    // creates a new game
    // player is the host of this game
    private void addGame(Player player) {
        if (!plugin.isInGame(player)) {
            Kitpvp.getInstance().reloadConfig();
            Kitpvp.getInstance().loadConfig();
            plugin.getGames().add(new Game(player));
            Chat.sendMessage(player, "You have successfully created a new game!" +
                    "\nUse /kitpvp invite <player> to invite a friend!");
        } else {
            Chat.sendMessage(player, "You are already in a game!");
        }
    }

    // invites player to a game
    private void invite(Player player, String invitedStr) {
        if (invitedStr == null) {
            Chat.sendMessage(player, "/kitpvp invite <player>");
            return;
        }
        Player invited = plugin.getServer().getPlayer(invitedStr);
        if (invited == null) {
            Chat.sendMessage(player, "Couldn't find player!");
            return;
        }
        if (plugin.isInGame(invited)) {
            Chat.sendMessage(player, "This player is currently in a game, wait until he is finished!");
            return;
        }
        if (!(plugin.isInGame(player))) {
            start(player);
        }
        if (plugin.getGame(player).getHost() != player) {
            Chat.sendMessage(player, "You are not the host of this game. Only the host can invite new players.");
            return;
        }
        if (plugin.getGame(player).getCurrentPhase() != Phase.invitation) {
            Chat.sendMessage(player, "You can only invite players during invitation phase!");
            return;
        }
        if (plugin.getGame(player).getInvited().contains(invited)) {
            Chat.sendMessage(player, "You have already invited this player to your game. Wait for his response.");
            return;
        }
        plugin.getGame(player).getInvited().add(invited);
        Chat.sendMessage(player, "You have invited " + invited.getDisplayName() + " to your game. \n" +
                "Let's wait for his response!");
        Chat.sendMessage(invited, "You have been invited to a KitPvP game by " + player.getName()
                + "\nUse /kitpvp accept " + player.getDisplayName() + " to accept the invitation!");
    }

    // accept a game invite
    private void acceptInvite(Player invited, String invitingStr) {
        Player inviting = plugin.getServer().getPlayer(invitingStr);
        Game invitedTo = plugin.getGame(inviting);
        if (!invitedTo.getInvited().contains(invited)) {
            Chat.sendMessage(invited, "You haven't received an invitation from this player.");
            return;
        }
        if (invitedTo.getCurrentPhase() != Phase.invitation) {
            Chat.sendMessage(invited, "The game is already running, you cant join now!");
            return;
        }
        invitedTo.addPlayer(invited);
        Chat.sendMessage(invited, "You have successfully joined superhellth's game!");
        for (Player player : invitedTo.getMembers()) {
            Chat.sendMessage(player, invited.getDisplayName() + " has joined the game!\n Say Hi!");
        }
    }

    // start game
    private void start(Player player) {
        Game game = plugin.getGame(player);
        if (game == null) {
            Chat.sendMessage(player, "You currently not in a game!");
            return;
        }
        if (game.getHost() != player) {
            Chat.sendMessage(player, "You are not the host of this game!");
            return;
        }
        if (game.getCurrentPhase() != Phase.invitation) {
            Chat.sendMessage(player, "You aren't in the invitation phase anymore!");
            return;
        }
        game.startKitSelection();
    }

    // provides a list of all available kits
    private void listKits(Player player) {
        StringBuilder message = new StringBuilder("Available Kits:\n");
        for (Kit kit : plugin.getKits()) {
            message.append(kit.getColor()).append("").append(ChatColor.BOLD).append(kit.getName()).append("\n");
        }

        Chat.sendMessage(player, message.toString());
    }

    // select kit
    private void selectKit(Player player, String kitName) {
        Game game = plugin.getGame(player);
        if (game == null) {
            Chat.sendMessage(player, "You are currently not in a game!");
            return;
        }
        if (game.getCurrentPhase() != Phase.kitselection) {
            Chat.sendMessage(player, ChatColor.BOLD + "You are not in the kit selection phase!");
            return;
        }
        Kit selected = plugin.getKit(kitName);
        if (selected == null) {
            Chat.sendMessage(player, "This is not a valid kit name! Type /kitpvp kits for a list of available kits.");
            return;
        }
        game.selectKit(player, selected);
        Chat.sendMessage(player, "You have successfully selected the " + selected.getColor() + "" + ChatColor.BOLD
                + selected.getName() + "§r" + Chat.BASE_COLOR + " kit!");
    }

    // ready
    private void ready(Player player) {
        if (plugin.getGame(player) == null || plugin.getGame(player).getCurrentPhase() != Phase.kitselection) {
            Chat.sendMessage(player, "You can't /kitpvp ready right now!");
            return;
        }
        ReadyEvent event = new ReadyEvent(player);
        Bukkit.getPluginManager().callEvent(event);
    }

    // provides a list of game members
    private void list(Player player) {
        Game game = plugin.getGame(player);
        if (game == null) {
            Chat.sendMessage(player, "You are currently in no game.");
            return;
        }
        String message = "You are currently playing with:\n";
        for (Player aPlayer : game.getMembers()) {
            if (aPlayer != player) {
                message += (aPlayer.getDisplayName() + "\n");
            }
        }
        message += "... and yourself! :D";

        Chat.sendMessage(player, message);
    }

    // leave the current game
    private void leaveGame(Player player) {
        if (!plugin.isInGame(player)) {
            Chat.sendMessage(player, "You are currently not in a game!");
        } else {
            Game playedByPlayer = plugin.getGame(player);
            playedByPlayer.removePlayer(player);
            if (playedByPlayer.getMembers().size() <= 0) {
                plugin.getGames().remove(playedByPlayer);
            }
            Chat.sendMessage(player, "You left your game!");
        }
    }

    // stops and deletes the current game
    private void stopGame(Player player) {
        Game game = plugin.getGame(player);
        if (game == null) {
            Chat.sendMessage(player, "You are currently not in a game!");
            return;
        }
        if (game.getHost() != player) {
            Chat.sendMessage(player, "Only the host can stop the game!");
            return;
        }
        plugin.getGames().remove(game);
        Chat.sendMessage(player, "The game has been stopped!");
    }
}
