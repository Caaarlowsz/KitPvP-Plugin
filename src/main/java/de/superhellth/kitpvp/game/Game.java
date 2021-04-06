package de.superhellth.kitpvp.game;

import de.superhellth.kitpvp.kits.Kit;
import de.superhellth.kitpvp.main.Kitpvp;
import de.superhellth.kitpvp.util.Chat;
import de.superhellth.kitpvp.util.LocationChecker;
import de.superhellth.kitpvp.util.TimeManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class Game {

    private Phase currentPhase;

    // list of game members
    private Player host;
    private final List<Player> members;
    private final List<Player> invited;
    private final Map<Player, Kit> selectedKits;
    private final Map<Player, Boolean> alive;
    private final List<Block> placedBlocks;

    public Game(Player host) {
        this.host = host;
        members = new ArrayList<>();
        invited = new ArrayList<>();
        selectedKits = new HashMap<>();
        alive = new HashMap<>();
        placedBlocks = Collections.synchronizedList(new ArrayList<>());
        members.add(host);
        currentPhase = Phase.INVITATION;
    }

    /**
     * Starts the kit selection phase. Only method needed to start the game
     */
    public void startKitSelection() {
        // remove effects, heal player and give kit selector
        ItemStack kitSelector = new ItemStack(Material.CHEST, 1);
        ItemMeta selectorMeta = kitSelector.getItemMeta();
        selectorMeta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Kit Selector");
        selectorMeta.setLore(Arrays.asList("Right click to select your kit!"));
        kitSelector.setItemMeta(selectorMeta);

        for (Player player : members) {
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }
            alive.put(player, true);
            player.setHealth(20);
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.getInventory().clear();
            player.getInventory().setItem(8, kitSelector);
            player.setGameMode(GameMode.ADVENTURE);
        }

        // in seconds
        int kitSelectionTime = 60;
        currentPhase = Phase.KIT_SELECTION;
        broadcast(ChatColor.BOLD + "Kit selection phase has begun!\n§r" + Chat.BASE_COLOR +
                "You have " + TimeManager.getSecondsAsString(kitSelectionTime) + " to select your kit.");
        broadcastDelayedMessage("The game starts in " + TimeManager.getSecondsAsString(kitSelectionTime / 2),
                kitSelectionTime / 2);
        broadcastDelayedMessage("The game starts in " + TimeManager.getSecondsAsString(kitSelectionTime / 4),
                kitSelectionTime / 4 * 3 );
        for (int c = 0; c < 10; c++) {
            broadcastDelayedMessage(ChatColor.BOLD + "" + (10 - c) + "",  kitSelectionTime - 10 + c);
        }

        // start grace period
        Bukkit.getScheduler().scheduleSyncDelayedTask(Kitpvp.getInstance(), new Runnable() {
            @Override
            public void run() {
                start();
            }
        }, kitSelectionTime * 20);
    }

    // start actual game
    public void start() {
        // time set day
        if (Kitpvp.getInstance().getMapCenter().getWorld() == Bukkit.getWorld("world")) {
            Bukkit.getWorld("world").setTime(12 * 1000);
        }

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
     * Broadcast a message to all game members
     *
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

    // Basic player actions
    // add player
    public void addPlayer(Player player) {
        members.add(player);
    }

    // remove player
    public void removePlayer(Player player) {
        members.remove(player);
        if (host == player && members.size() >= 1) {
            host = members.get(0);
        }
        if (selectedKits.keySet().contains(player)) {
            selectedKits.remove(player);
        }
        alive.remove(player);
    }

    // select kit
    public void selectKit(Player player, Kit kit) {
        selectedKits.put(player, kit);
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

    // manage bw kit
    public List<Block> getPlacedBlocks() {
        return placedBlocks;
    }
    public void addPlacedBlock(Block block) {
        placedBlocks.add(block);
    }


    // Further methods
    // check if player is in this game
    public boolean playedBy(Player player) {
        return members.contains(player);
    }

    // send a message to all game members
    public void broadcast(String message) {
        for (Player player : members) {
            Chat.sendMessage(player, message);
        }
    }

    // give players items and effects
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

    // checks if kits were chosen correctly
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

    // sets up world border and teleports players
    private void setupWorldAndPlayerPosition() {
        Kitpvp plugin = Kitpvp.getInstance();

        // reset potion effects
        for (Player player : members) {
            player.setGameMode(GameMode.SURVIVAL);
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }
            player.setHealth(20);
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.getInventory().clear();
        }

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

    // ends the game
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

        // reset borders
        for (World world : Kitpvp.getInstance().getServer().getWorlds()) {
            WorldBorder border = world.getWorldBorder();
            border.setCenter(0, 0);
            border.setSize(6000000);
        }
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
