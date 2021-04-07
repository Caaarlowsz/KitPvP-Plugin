package de.superhellth.kitpvp.main;

import de.superhellth.kitpvp.chat.Chat;
import de.superhellth.kitpvp.commands.KitpvpCommand;
import de.superhellth.kitpvp.game.Game;
import de.superhellth.kitpvp.kits.*;
import de.superhellth.kitpvp.listener.BlockPlaceListener;
import de.superhellth.kitpvp.listener.GraceListener;
import de.superhellth.kitpvp.listener.KitSelectionListener;
import de.superhellth.kitpvp.listener.PlayerDeathListener;
import de.superhellth.kitpvp.listener.kitlistener.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Kitpvp extends JavaPlugin {

    private Location initCenter;
    private final int initSize = 300;

    public static final String MAP_SETUP = "world-setup";
    public static final String MAP_CENTER = MAP_SETUP + "." + "center";
    public static final String MAP_SIZE = MAP_SETUP + "." + "size";
    private int mapSize;
    private Location mapCenter;

    private List<Game> games;
    private Map<Material, Kit> kitMap;

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
        initCenter = new Location(getServer().getWorlds().get(0), 0, 100, 0);

        // load config
        loadConfig();

        // create games list
        games = new ArrayList<>();
        kitMap = new LinkedHashMap<>();
        registerKits();

        // Register command
        new KitpvpCommand(this);

        // Register listener
        registerListeners(new PlayerDeathListener(this),
                new BlockPlaceListener(),
                new GraceListener(),
                new KitSelectionListener());

        // Kit Listener
        registerListeners(new BWListener(this),
                new EndermanListener(this),
                new LumberListener(this),
                new ZeusListener(this),
                new WitchListener(this),
                new SoupListener(this),
                new DolphinListener(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();

        // Stop all games
        for (Game game : games) {
            game.end(false);
        }
    }

    /**
     * Load all values stored in the config file to the according variable.
     */
    public void loadConfig() {
        try {
            mapCenter = Location.deserialize(getConfig().getConfigurationSection(MAP_CENTER).getValues(false));
            mapSize = getConfig().getInt(MAP_SIZE);
        } catch (NullPointerException e) {
            getConfig().set(MAP_CENTER, initCenter.serialize());
            getConfig().set(MAP_SIZE, initSize);
            mapCenter = initCenter;
            mapSize = initSize;
        }
    }

    /**
     * Log a message to the console. Has a plugin-specific prefix.
     *
     * @param text
     */
    public void log(String text) {
        Bukkit.getConsoleSender().sendMessage(Chat.PREFIX + text);
    }

    // getter
    // returns list of all running games
    public List<Game> getGames() {
        return games;
    }

    public List<Kit> getKits() {
        return new ArrayList<>(kitMap.values());
    }

    public Kit getKit(Material view) {
        return kitMap.getOrDefault(view, null);
    }

    // finds game player currently is in
    public Game getGame(Player player) {
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
        kits.add(Soup.getInstance());
        kits.add(Trapper.getInstance());
        kits.add(Pyro.getInstance());
        kits.add(Tank.getInstance());
        kits.add(Dolphin.getInstance());

        for (Kit kit : kits) {
            kitMap.put(kit.getView(), kit);
        }
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener: listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
