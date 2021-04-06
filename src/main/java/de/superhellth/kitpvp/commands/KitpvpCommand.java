package de.superhellth.kitpvp.commands;

import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.game.Phase;
import de.superhellth.kitpvp.kits.Kit;
import de.superhellth.kitpvp.main.Kitpvp;
import de.superhellth.kitpvp.util.Chat;
import de.superhellth.kitpvp.util.ConfigWriter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitpvpCommand implements CommandExecutor {

    // The plugin instance
    private final Kitpvp plugin;

    // The given arguments
    private String[] args;
    private Player player;

    // constructor
    public KitpvpCommand(Kitpvp plugin) {
        this.plugin = plugin;
        // register command
        plugin.getCommand("kitpvp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.player = (Player) sender;
        this.args = args;

        // check if command has at least one argument
        hasCorrectArgs(1, Allow.OR_MORE);

        // check argument
        switch (args[0].toLowerCase()) {
            case Commands.HELP:
                if (hasCorrectArgs(1, Allow.ONLY_EQUAL)) {
                    Chat.sendMessage(player, Chat.HELP);
                }
                break;

            case Commands.NEW_GAME:
                if (hasCorrectArgs(1, Allow.ONLY_EQUAL)) {
                    newGame(player);
                }
                break;

            case Commands.INVITE:
                if (hasCorrectArgs(2, Allow.ONLY_EQUAL)) {
                    invite(player, args[1]);
                }
                break;

            case Commands.ACCEPT:
                if (hasCorrectArgs(2, Allow.ONLY_EQUAL)) {
                    acceptInvite(player, args[1]);
                }
                break;

            case Commands.LIST:
                if (hasCorrectArgs(1, Allow.ONLY_EQUAL)) {
                    list(player);
                }
                break;

            case Commands.START:
                if (hasCorrectArgs(1, Allow.ONLY_EQUAL)) {
                    start(player);
                }
                break;

            case Commands.LEAVE:
                if (hasCorrectArgs(1, Allow.ONLY_EQUAL)) {
                    leaveGame(player);
                }
                break;

            case Commands.STOP:
                if (hasCorrectArgs(1, Allow.ONLY_EQUAL)) {
                    stopGame(player);
                }
                break;

            case Commands.CONFIG:
                if (hasCorrectArgs(1, Allow.OR_MORE)) {
                    config();
                }
                break;

            default:
                Chat.sendMessage(player, Chat.UNKNOWN);
                break;
        }

        return true;
    }

    // manage config commands
    private void config() {
        if (hasCorrectArgs(2, Allow.OR_MORE)) {
            switch (args[1].toLowerCase()) {
                case Commands.MAP_SIZE:
                    try {
                        ConfigWriter.updateSize(Integer.parseInt(args[2]));
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        Chat.sendMessage(player, "Type a number!");
                        return;
                    }
                    Chat.sendMessage(player, "Successfully changed map size!");
                    break;

                case Commands.MAP_CENTER:
                    if (hasCorrectArgs(2, Allow.ONLY_EQUAL)) {
                        ConfigWriter.updateCenter(player.getLocation());
                        Chat.sendMessage(player, Chat.CHANGED_MAP_CENTER);
                    }
                    break;

                default:
                    Chat.sendMessage(player, "Unknown command");
                    break;
            }
        }
    }

    // creates a new game
    // player is the host of this game
    private void newGame(Player player) {
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
        if (plugin.getGame(player).getCurrentPhase() != Phase.INVITATION) {
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
        if (invitedTo.getCurrentPhase() != Phase.INVITATION) {
            Chat.sendMessage(invited, "The game is already running, you cant join now!");
            return;
        }
        invitedTo.addPlayer(invited);
        Chat.sendMessage(invited, "You have successfully joined superhellth's game!");
        for (Player player : invitedTo.getMembers()) {
            Chat.sendMessage(player, invited.getDisplayName() + " has joined the game!\n Say Hi!");
        }
        invitedTo.getInvited().remove(invited);
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
        if (game.getCurrentPhase() != Phase.INVITATION) {
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
                playedByPlayer.end(false);
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
        game.end(false);
        plugin.getGames().remove(game);
        Chat.sendMessage(player, "The game has been stopped!");
    }

    /**
     * Check whether or not the number of arguments is correct
     * @param numArgs Number of arguments the command is supposed to have
     * @param option Determines whether the number of arguments in the command can be greater or smaller
     * @return
     */
    private boolean hasCorrectArgs(int numArgs, Allow option) {
        int givenArgs = args.length;
        switch (option) {
            case OR_LESS:
                if (givenArgs == numArgs || givenArgs < numArgs) {
                    return true;
                }
                Chat.sendMessage(player, Chat.TOO_MANY_ARGS);
                break;

            case ONLY_EQUAL:
                if (givenArgs == numArgs) {
                    return true;
                }
                Chat.sendMessage(player, Chat.NOT_RIGHT_ARGS);
                break;

            case OR_MORE:
                if (givenArgs == numArgs || givenArgs > numArgs) {
                    return true;
                }
                Chat.sendMessage(player, Chat.TOO_FEW_ARGS);
                break;
        }
        return false;
    }
}
