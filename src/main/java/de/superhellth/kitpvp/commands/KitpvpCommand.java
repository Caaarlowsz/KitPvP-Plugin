package de.superhellth.kitpvp.commands;

import com.sun.istack.internal.Nullable;
import de.superhellth.kitpvp.chat.stringSaves.CommandOutput;
import de.superhellth.kitpvp.chat.stringSaves.Error;
import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.game.Phase;
import de.superhellth.kitpvp.main.Kitpvp;
import de.superhellth.kitpvp.chat.Chat;
import de.superhellth.kitpvp.util.ConfigWriter;
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
    @Nullable
    private Game game;

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
        game = plugin.getGame(player);

        // check if command has at least one argument
        hasCorrectArgs(1, Allow.OR_MORE);

        // check argument
        switch (args[0].toLowerCase()) {
            case Commands.HELP:
                if (hasCorrectArgs(1, Allow.ONLY_EQUAL)) {
                    Chat.sendMessage(player, CommandOutput.HELP);
                }
                break;

            case Commands.NEW_GAME:
                if (hasCorrectArgs(1, Allow.ONLY_EQUAL)) {
                    newGame();
                }
                break;

            case Commands.INVITE:
                if (hasCorrectArgs(2, Allow.ONLY_EQUAL)) {
                    invite(args[1]);
                }
                break;

            case Commands.ACCEPT:
                if (hasCorrectArgs(2, Allow.ONLY_EQUAL)) {
                    acceptInvite(args[1]);
                }
                break;

            case Commands.LIST:
                if (hasCorrectArgs(1, Allow.ONLY_EQUAL)) {
                    list();
                }
                break;

            case Commands.START:
                if (hasCorrectArgs(1, Allow.ONLY_EQUAL)) {
                    start();
                }
                break;

            case Commands.LEAVE:
                if (hasCorrectArgs(1, Allow.ONLY_EQUAL)) {
                    leaveGame();
                }
                break;

            case Commands.STOP:
                if (hasCorrectArgs(1, Allow.ONLY_EQUAL)) {
                    stopGame();
                }
                break;

            case Commands.CONFIG:
                if (hasCorrectArgs(1, Allow.OR_MORE)) {
                    config();
                }
                break;

            default:
                Chat.sendMessage(player, Error.UNKNOWN);
                break;
        }

        return true;
    }

    /**
     * Manages all </kp config ...> commands.
     */
    private void config() {
        if (hasCorrectArgs(2, Allow.OR_MORE)) {
            switch (args[1].toLowerCase()) {

                // changing the size of the map
                case Commands.MAP_SIZE:
                    if (hasCorrectArgs(3, Allow.ONLY_EQUAL)) {
                        try {
                            ConfigWriter.updateSize(Integer.parseInt(args[2]));
                        } catch (NumberFormatException e) {
                            Chat.sendMessage(player, Error.NO_NUMBER);
                            return;
                        }
                        Chat.sendMessage(player, CommandOutput.CHANGED_MAP_SIZE);
                    }
                    break;

                // changing the center of the map
                case Commands.MAP_CENTER:
                    if (hasCorrectArgs(2, Allow.ONLY_EQUAL)) {
                        ConfigWriter.updateCenter(player.getLocation());
                        Chat.sendMessage(player, CommandOutput.CHANGED_MAP_CENTER);
                    }
                    break;

                // command not found
                default:
                    Chat.sendMessage(player, Error.UNKNOWN);
                    break;
            }
        }
    }

    /**
     * Creates a new game.
     */
    private void newGame() {
        if (!plugin.isInGame(player)) {
            plugin.reloadConfig();
            plugin.loadConfig();
            Game aGame = new Game(player);
            plugin.getGames().add(aGame);
            game = aGame;
            Chat.sendMessage(player, CommandOutput.CREATED_GAME);
        } else {
            Chat.sendMessage(player, Error.YOU_ARE_IN_GAME);
        }
    }

    /**
     * Invites a player to the game.
     * @param invitedStr Name of the invited player
     */
    private void invite(String invitedStr) {
        Player invited = plugin.getServer().getPlayer(invitedStr);
        if (invited == null) {
            Chat.sendMessage(player, Error.WRONG_PLAYER_NAME);
            return;
        }
        if (plugin.isInGame(invited)) {
            Chat.sendMessage(player, Error.OTHER_PLAYER_IS_IN_GAME);
            return;
        }
        if (game == null) {
            newGame();
        }
        if (game.getHost() != player) {
            Chat.sendMessage(player, Error.NOT_HOST);
            return;
        }
        if (game.getCurrentPhase() != Phase.INVITATION) {
            Chat.sendMessage(player, Error.WRONG_PHASE);
            return;
        }
        if (game.getInvited().contains(invited)) {
            Chat.sendMessage(player, Error.ALREADY_INVITED);
            return;
        }
        game.getInvited().add(invited);
        Chat.sendMessage(player, invited.getDisplayName() + CommandOutput.INVITED_PLAYER);
        Chat.sendMessage(invited, CommandOutput.BEING_INVITED + player.getName()
                + "\n" + CommandOutput.ACCEPT_INVITE + player.getName());
    }

    /**
     * Accept the invitation to a game
     * @param invitingStr Name of the inviting player
     */
    private void acceptInvite(String invitingStr) {
        Player inviting = plugin.getServer().getPlayer(invitingStr);
        Game invitedTo = plugin.getGame(inviting);
        if (!invitedTo.getInvited().contains(player)) {
            Chat.sendMessage(player, Error.NO_INVITATION);
            return;
        }
        if (invitedTo.getCurrentPhase() != Phase.INVITATION) {
            Chat.sendMessage(player, Error.GAME_RUNNING);
            return;
        }
        invitedTo.addPlayer(player);
        Chat.sendMessage(player, CommandOutput.ACCEPTED_INVITE);
        for (Player player : invitedTo.getMembers()) {
            Chat.sendMessage(player, player.getDisplayName() + CommandOutput.PLAYER_JOINED);
        }
        invitedTo.getInvited().remove(player);
    }

    /**
     * Start the game as a host.
     */
    private void start() {
        Game game = plugin.getGame(player);
        if (game == null) {
            Chat.sendMessage(player, Error.NOT_IN_GAME);
            return;
        }
        if (game.getHost() != player) {
            Chat.sendMessage(player, Error.NOT_HOST);
            return;
        }
        if (game.getCurrentPhase() != Phase.INVITATION) {
            Chat.sendMessage(player, Error.WRONG_PHASE);
            return;
        }
        game.startKitSelection();
    }

    /**
     * Provides a list of game members.
     */
    private void list() {
        Game game = plugin.getGame(player);
        if (game == null) {
            Chat.sendMessage(player, Error.NOT_IN_GAME);
            return;
        }
        StringBuilder messageBuilder = new StringBuilder(CommandOutput.LIST);
        for (Player aPlayer : game.getMembers()) {
            if (aPlayer != player) {
                messageBuilder.append(aPlayer.getDisplayName()).append("\n");
            }
        }
        String message = messageBuilder.toString();
        message += "yourself";

        Chat.sendMessage(player, message);
    }

    /**
     * Leave the current game.
     */
    private void leaveGame() {
        if (!plugin.isInGame(player)) {
            Chat.sendMessage(player, Error.NOT_IN_GAME);
        } else {
            Game playedByPlayer = plugin.getGame(player);
            playedByPlayer.removePlayer(player);
            if (playedByPlayer.getMembers().size() <= 0) {
                playedByPlayer.end(false);
                plugin.getGames().remove(playedByPlayer);
            }
            Chat.sendMessage(player, CommandOutput.LEFT);
        }
    }

    /**
     * Stops and deletes the current game.
     */
    private void stopGame() {
        Game game = plugin.getGame(player);
        if (game == null) {
            Chat.sendMessage(player, Error.NOT_IN_GAME);
            return;
        }
        if (game.getHost() != player) {
            Chat.sendMessage(player, Error.NOT_HOST);
            return;
        }
        game.end(false);
        plugin.getGames().remove(game);
        for (Player player : game.getMembers()) {
            Chat.sendMessage(player, CommandOutput.STOP);
        }
    }

    /**
     * Check whether or not the number of arguments is correct
     *
     * @param numArgs Number of arguments the command is supposed to have
     * @param option  Determines whether the number of arguments in the command can be greater or smaller
     * @return
     */
    private boolean hasCorrectArgs(int numArgs, Allow option) {
        int givenArgs = args.length;
        switch (option) {
            case OR_LESS:
                if (givenArgs == numArgs || givenArgs < numArgs) {
                    return true;
                }
                Chat.sendMessage(player, Error.TOO_MANY_ARGS);
                break;

            case ONLY_EQUAL:
                if (givenArgs == numArgs) {
                    return true;
                }
                Chat.sendMessage(player, Error.NOT_RIGHT_ARGS);
                break;

            case OR_MORE:
                if (givenArgs == numArgs || givenArgs > numArgs) {
                    return true;
                }
                Chat.sendMessage(player, Error.TOO_FEW_ARGS);
                break;
        }
        return false;
    }
}
