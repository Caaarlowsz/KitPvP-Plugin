package de.superhellth.kitpvp.game;

import de.superhellth.kitpvp.chat.stringSaves.Progress;
import de.superhellth.kitpvp.kits.Kit;
import de.superhellth.kitpvp.main.Kitpvp;
import de.superhellth.kitpvp.chat.Chat;
import de.superhellth.kitpvp.util.LocationChecker;
import de.superhellth.kitpvp.util.TimeManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * A kitpvp game. Takes care of player management
 * and game progress
 */
public class Game {

    private Phase currentPhase;

    // list of game members
    private final Kitpvp plugin = Kitpvp.getInstance();
    private Player host;
    private final List<Player> members = new ArrayList<>();
    private final List<Player> invited = new ArrayList<>();
    private final Map<Player, Kit> selectedKits = new HashMap<>();
    private final Map<Player, Boolean> alive = new HashMap<>();

    // for the bw kit
    private final Set<Block> placedBlocks = Collections.synchronizedSet(new HashSet<>());

    public Game(Player host) {
        this.host = host;
        members.add(host);
        currentPhase = Phase.INVITATION;
    }

    public void addPlayer(Player player) {
        members.add(player);
    }

    public void removePlayer(Player player) {
        members.remove(player);
        if (host == player && members.size() >= 1) {
            host = members.get(0);
        }
        if (selectedKits.keySet().contains(player)) {
            selectedKits.remove(player);
        }
        alive.remove(player);
        player.setGameMode(GameMode.SURVIVAL);
    }

    public void selectKit(Player player, Kit kit) {
        selectedKits.put(player, kit);
    }

    /**
     * Starts the kit selection phase. Only method needed to start the game.
     */
    public void startKitSelection() {
        // remove effects, heal player and give kit selector
        resetAll(GameMode.ADVENTURE);
        giveAll(8, new KitSelector());

        // in seconds
        int kitSelectionTime = 60;
        currentPhase = Phase.KIT_SELECTION;
        prepare1stCountdown(kitSelectionTime);

        // start grace period
        Bukkit.getScheduler().scheduleSyncDelayedTask(Kitpvp.getInstance(), this::startGrace, kitSelectionTime * 20);
    }

    // start actual game
    public void startGrace() {
        // time set day
        Bukkit.getWorld("world").setTime(1000);

        // checks if everyone selected a kit, if not give player random kit
        checkKits();

        // tell every player his kit
        for (Player player : members) {
            Chat.sendMessage(player, "You are now playing as " + selectedKits.get(player).getColor() + ""
                    + ChatColor.BOLD + selectedKits.get(player).getName() + "§r" + Chat.BASE_COLOR + "!");
        }

        // set grace period
        currentPhase = Phase.GRACE;
        // setup world border
        setupWorldAndPlayerPosition();
        deployKits();

        //int timeToCD = 4 * 60 + 49;
        int timeToCD = 2 * 60;
        prepare2ndCountdown(timeToCD);
    }

    /**
     * Ends the game. Chooses winner if wanted, teleports everyone to the center in gm spectator
     * @param hasWinner Game can be ended with winner(regular game end) or without(/kp stop)
     */
    public void end(boolean hasWinner) {
        if (hasWinner) {
            broadcast("The game is over! The winner is "
                    + getWinner().getDisplayName() + "! Congratulations!");

            // teleport everyone to the  center
            getWinner().setGameMode(GameMode.SPECTATOR);
        }
        Location center = Kitpvp.getInstance().getMapCenter();
        for (Player player : members) {
            player.teleport(center);
        }
        members.clear();

        // reset borders
        for (World world : Kitpvp.getInstance().getServer().getWorlds()) {
            WorldBorder border = world.getWorldBorder();
            border.setCenter(0, 0);
            border.setSize(6000000);
        }

    }

    /**
     * Checks if everyone selected a kit. Players who haven't will get a random one
     */
    private void checkKits() {
        for (Player player : members) {
            Random random = new Random();
            if (!selectedKits.containsKey(player)) {
                Chat.sendMessage(player, "You haven't chosen a kit and will get a random one!");
                List<Kit> kitList = Kitpvp.getInstance().getKits();
                selectedKits.put(player, kitList.get(random.nextInt(kitList.size())));
            }
        }
    }

    /**
     * Gives players their items, armor and effects
     */
    private void deployKits() {
        for (Player player : members) {
            Kit selectedKit = selectedKits.get(player);
            for (ItemStack item : selectedKit.getItems()) {
                player.getInventory().addItem(item);
            }
            for (PotionEffect effect : selectedKit.getEffects()) {
                player.addPotionEffect(effect);
            }
            player.getInventory().setArmorContents(selectedKit.getArmor());
        }
    }

    /**
     * Sets the world border, resets player stats and teleports players
     */
    private void setupWorldAndPlayerPosition() {
        resetAll(GameMode.SURVIVAL);

        // set world border
        for (World world : plugin.getServer().getWorlds()) {
            WorldBorder border = world.getWorldBorder();
            border.setCenter(plugin.getMapCenter());
            border.setSize(plugin.getMapSize());
        }

        WorldBorder mainBorder = plugin.getMapCenter().getWorld().getWorldBorder();

        // teleport players
        LocationChecker checker = new LocationChecker();
        for (Player player : members) {
            player.teleport(checker.findSafeSpot(mainBorder.getCenter(), (int) mainBorder.getSize()));
        }
    }

    /**
     * Gives the item to all players.
     * @param slot Item in there will be replaced by item
     * @param item Item to add
     */
    private void giveAll(int slot, ItemStack item) {
        for (Player player : members) {
            player.getInventory().setItem(slot, item);
        }
    }

    /**
     * Heals players, removes their effects, fills their hunger bars and sets their gamemode to gamemode
     * @param gameMode Gamemode to set all players to
     */
    private void resetAll(GameMode gameMode) {
        for (Player player : members) {
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }
            alive.put(player, true);
            player.setHealth(20);
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.getInventory().clear();
            player.setGameMode(gameMode);
        }
    }

    /**
     * Creates a countdown for the end of the kit selection phase
     * @param kitSelectionTime time of the kit selection phase
     */
    private void prepare1stCountdown(int kitSelectionTime) {
        broadcast(Progress.START_KS_1 + TimeManager.getSecondsAsString(kitSelectionTime) + Progress.START_KS_2);
        broadcastDelayedMessage("The game starts in " + TimeManager.getSecondsAsString(kitSelectionTime / 2),
                kitSelectionTime / 2);
        broadcastDelayedMessage("The game starts in " + TimeManager.getSecondsAsString(kitSelectionTime / 4),
                kitSelectionTime / 4 * 3 );
        for (int c = 0; c < 10; c++) {
            broadcastDelayedMessage(ChatColor.BOLD + "" + (10 - c) + "",  kitSelectionTime - 10 + c);
        }
    }

    /**
     * Manages player notification for the end of the grace period
     * @param timeToCD time until timer starts (10s countdown)
     */
    private void prepare2ndCountdown(int timeToCD) {
        broadcast("The grace period has begun. It lasts for " + TimeManager.getSecondsAsString(timeToCD) + "!");
        broadcastDelayedMessage(ChatColor.BOLD + "The grace period will end in...", timeToCD);
        for (int c = 0; c < 10; c++) {
            broadcastDelayedMessage(ChatColor.BOLD + "" + (10 - c) + "", timeToCD + 1 + c);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Kitpvp.getInstance(), new Runnable() {
            @Override
            public void run() {
                currentPhase = Phase.FIGHTING;
                broadcast(ChatColor.BOLD + "You are now in the fighting phase, the last to survive wins!");
            }
        }, timeToCD * 20 + 10 * 20);
    }

    /**
     * Broadcast a message to all game members.
     * @param message
     * @param delay in seconds
     */
    private void broadcastDelayedMessage(String message, int delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Kitpvp.getInstance(), new Runnable() {
            @Override
            public void run() {
                broadcast(message);
            }
        }, 20L * delay);
    }

    // send a message to all game members
    public void broadcast(String message) {
        for (Player player : members) {
            Chat.sendMessage(player, message);
        }
    }

    // Getter
    // get all members
    public List<Player> getMembers() {
        return members;
    }

    // get invited
    public List<Player> getInvited() {
        return invited;
    }

    // get host
    public Player getHost() {
        return host;
    }

    // get current phase
    public Phase getCurrentPhase() {
        return currentPhase;
    }

    // get kit selection
    public Map<Player, Kit> getSelectedKits() {
        return selectedKits;
    }

    // get alive map
    public Map<Player, Boolean> getAlive() {
        return alive;
    }

    // check if player is in this game
    public boolean playedBy(Player player) {
        return members.contains(player);
    }

    // manage bw kit
    public Set<Block> getPlacedBlocks() {
        return placedBlocks;
    }
    public void addPlacedBlock(Block block) {
        placedBlocks.add(block);
    }

    private Player getWinner() {
        for (Player player : members) {
            if (alive.get(player)) {
                return player;
            }
        }

        return null;
    }
}
