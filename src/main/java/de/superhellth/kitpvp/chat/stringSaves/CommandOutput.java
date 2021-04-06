package de.superhellth.kitpvp.chat.stringSaves;

public class CommandOutput {

    public final static String HELP = "Commands: \n" +
            "/kitpvp newgame \n" +
            "/kitpvp invite <player> \n" +
            "/kitpvp start \n" +
            "/kitpvp leave \n" +
            "/kitpvp stop";
    public final static String CHANGED_MAP_CENTER = "Successfully changed map center!";
    public final static String CHANGED_MAP_SIZE = "Successfully changed map size!";
    public final static String CREATED_GAME = "You have successfully created a new game!" +
            "\nUse /kitpvp invite <player> to invite a friend!";
    public final static String INVITED_PLAYER = " has been invited! Wait for his response";
    public final static String BEING_INVITED = "You have been invited by ";
    public final static String ACCEPT_INVITE = "Type /kp accept ";
    public final static String ACCEPTED_INVITE = "You have successfully joined the game!";
    public final static String PLAYER_JOINED = " has joined your game!";
    public final static String LIST = "You are playing with...";
    public final static String LEFT = "You left the game!";
    public final static String STOP = "Your game has been stopped!";

}
