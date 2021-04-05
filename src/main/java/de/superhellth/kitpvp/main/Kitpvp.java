package de.superhellth.kitpvp.main;

import de.superhellth.kitpvp.commands.KitpvpCommand;
import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.kits.*;
import de.superhellth.kitpvp.listener.BlockPlaceListener;
import de.superhellth.kitpvp.listener.PlayerDeathListener;
import de.superhellth.kitpvp.listener.ReadyListener;
import de.superhellth.kitpvp.listener.kitlistener.*;
import de.superhellth.kitpvp.ui.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Kitpvp extends JavaPlugin {

    public static final String MAP_SETUP = "world-setup";
    public static final String MAP_CENTER = MAP_SETUP + "." + "center";
    public static final String MAP_SIZE = MAP_SETUP + "." + "size";
    private int mapSize;
    private Location mapCenter;

    private List<Game> games;
    private Map<String, Kit> kitMap;

    // singleton pattern
    private static Kitpvp instance;
    public static Kitpvp getInstance() {
        if (Kitpvp.instance == null) {
            instance = new Kitpvp();
        }
        return instance;
    }

    @Override
    public void onEnable() {
        // log start message
        log("Plugin started");
        instance = this;

        // load config
        loadConfig();

        // create games list
        games = new ArrayList<>();
        kitMap = new HashMap<>();
        registerKits();

        // Register command
        new KitpvpCommand(this);

        // Register listener
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new ReadyListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);

        // Kit Listener
        getServer().getPluginManager().registerEvents(new EndermanListener(), this);
        getServer().getPluginManager().registerEvents(new LumberListener(), this);
        getServer().getPluginManager().registerEvents(new BWListener(), this);
        getServer().getPluginManager().registerEvents(new ZeusListener(), this);
        getServer().getPluginManager().registerEvents(new WitchListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            getConfig().save(getConfig().getCurrentPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        // set map center
        mapCenter = Location.deserialize(getConfig().getConfigurationSection(MAP_SETUP + "." + MAP_CENTER).getValues(false));
        mapSize = getConfig().getInt(MAP_SETUP + "." + MAP_SIZE);
    }

    // log a message
    public void log(String text) {
        Bukkit.getConsoleSender().sendMessage(Chat.PREFIX + text);
    }

    // getter
    // returns list of all games
    public List<Game> getGames() {
        return games;
    }

    // returns list of all kits
    public List<Kit> getKits() {
        return new ArrayList<>(kitMap.values());
    }

    // get kit
    // doesnt matter if letter are written capital or not
    public Kit getKit(String name) {
        name = name.toLowerCase();
        String firstLetter = name.substring(0, 1).toUpperCase();
        String rest = name.substring(1);
        name = firstLetter + rest;
        return kitMap.getOrDefault(name, null);
    }

    // finds game player currently is in
    public Game getGame(Player player) {
        Game game = null;
        for (Game aGame : games) {
            if (aGame.playedBy(player)) {
                return aGame;
            }
        }

        return null;
    }

    public int getMapSize() {
        return mapSize;
    }

    public Location getMapCenter() {
        return mapCenter;
    }

    // checks if player is in a game
    public boolean isInGame(Player player) {
        for (Game game : games) {
            if (game.playedBy(player)) {
                return true;
            }
        }

        return false;
    }

    // register all kits
    private void registerKits() {
        List<Kit> kits = new ArrayList<>();
        kits.add(Farmer.getInstance());
        kits.add(Miner.getInstance());
        kits.add(MLG.getInstance());
        kits.add(Enchanter.getInstance());
        kits.add(Enderman.getInstance());
        kits.add(Lumber.getInstance());
        kits.add(Rusher.getInstance());
        kits.add(Witch.getInstance());
        kits.add(BW.getInstance());
        kits.add(Jesus.getInstance());
        kits.add(Zeus.getInstance());

        for (Kit kit : kits) {
            kitMap.put(kit.getName(), kit);
        }
    }
}
