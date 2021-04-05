package de.superhellth.kitpvp.game;

import de.superhellth.kitpvp.kits.Kit;
import de.superhellth.kitpvp.main.Kitpvp;
import de.superhellth.kitpvp.ui.Chat;
import de.superhellth.kitpvp.util.LocationChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Game {

    private Phase currentPhase;

    // list of game members
    private Player host;
    private List<Player> members;
    private List<Player> invited;
    private Map<Player, Kit> selectedKits;
    private List<Player> readyPlayers;
    private List<Block> placedBlocks;

    public Game(Player host) {
        this.host = host;
        members = new ArrayList<>();
        invited = new ArrayList<>();
        selectedKits = new HashMap<>();
        readyPlayers = new ArrayList<>();
        placedBlocks = Collections.synchronizedList(new ArrayList<>());
        members.add(host);
        currentPhase = Phase.invitation;
    }

    // start kit selection phase
    public void startKitSelection() {
        // remove effect, give resistance and heal player
        for (Player player : members) {
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }
            player.setHealth(20);
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.addPotionEffect(
                    new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 70 * 60 * 20, 255));
        }

        currentPhase = Phase.kitselection;
        broadcast(ChatColor.BOLD + "Kit selection phase has begun!\n§r" + Chat.BASE_COLOR +
                "You can now select your kit with /kitpvp select <kit>\n" +
                "Type /kitpvp kits to see which kits are available!\n" +
                "Type /kitpvp ready when you have chosen your kit, or want to play with a random kit.");
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

        currentPhase = Phase.grace;
        // setup world border
        setupWorldAndPlayerPosition();
        deployKits();
        currentPhase = Phase.fighting;
        broadcast(ChatColor.BOLD + "You are now in the fighting phase, the last to survive wins!");
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
        if (readyPlayers.contains(player)) {
            readyPlayers.remove(player);
        }
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
            Chat.sendMessage(player, "Your items...");
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
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }
            player.setHealth(20);
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20, 100));
            player.getInventory().clear();
        }

        // set world border
        WorldBorder border = plugin.getMapCenter().getWorld().getWorldBorder();
        border.setCenter(plugin.getMapCenter());
        border.setSize(plugin.getMapSize());

        // teleport players
        Random random = new Random();
        LocationChecker checker = new LocationChecker();
        for (Player player : members) {
            player.teleport(checker.findSafeSpot(border.getCenter(), (int) border.getSize()));
        }
    }

    // check if all players are ready
    public void checkPlayer(Player player) {
        Chat.sendMessage(player, "You are marked as ready!");
        readyPlayers.add(player);
        boolean allReady = true;
        for (Player aPlayer : members) {
            if (!readyPlayers.contains(aPlayer)) {
                allReady = false;
            }
        }

        if (allReady) {
            start();
        }
    }
}
