package eq.larry.dev;


import java.io.File;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import eq.larry.dev.*;
import eq.larry.dev.event.block.BlockBreak;
import eq.larry.dev.event.block.BlockPlace;
import eq.larry.dev.event.entity.*;
import eq.larry.dev.event.inventory.InventoryClick;
import eq.larry.dev.event.player.*;
import eq.larry.dev.event.server.ServerListPing;
import eq.larry.dev.event.weather.ThunderChange;
import eq.larry.dev.event.weather.WeatherChange;
import eq.larry.dev.event.world.ChunkUnload;
import eq.larry.dev.hand.*;
import eq.larry.dev.sche.GameRunnable;
import eq.larry.dev.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class UltraCore extends JavaPlugin {
    private static UltraCore instance;

    public static UltraCore getInstance() {
        return instance;
    }

    public static String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "PVPSwap" + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + " ";

    public static boolean godMode = true;

    public static boolean duelMode = true;

    private MySQL database;

    private Location lobbyLocation;

    public Location getLobbyLocation() {
        return this.lobbyLocation;
    }

    public Set<Player> getAlivePlayers() {
        return this.alivePlayers;
    }

    private Set<Player> alivePlayers = new HashSet<>();

    private Map<String, List<Location>> spawns;

    private Map<String, List<Location>> unusedSpawns;

    private Map<String, Integer> onMaps = new HashMap<>();

    private List<String> maps;

    private List<Item> items;

    private List<Item> finalItems;

    private String lastMap;

    private List<Location> finalSpawns;

    private List<Location> unusedFinalSpawns;

    public List<Location> getFinalSpawns() {
        return this.finalSpawns;
    }

    public void onLoad() {
        try {
            Bukkit.unloadWorld("world", false);
            File worldContainer = getServer().getWorldContainer();
            File worldFolder = new File(worldContainer, "world");
            File copyFolder = new File(worldContainer, "pvpswap");
            if (copyFolder.exists()) {
                FileUtils.delete(worldFolder);
                FileUtils.copyFolder(copyFolder, worldFolder);
            }
        } catch (Throwable ex) {
            throw ex;
        }
    }

    public void onEnable() {
        instance = this;
        loadConfiguration(Bukkit.getWorlds().get(0));
        for (Map.Entry<String, List<Location>> entry : this.spawns.entrySet())
            loadChunks(entry.getValue());
        loadChunks(this.finalSpawns);
        this.database = new MySQL((Plugin)this, getConfig().getString("mysql.host"), getConfig().getString("mysql.port"), getConfig().getString("mysql.database"), getConfig().getString("mysql.user"), getConfig().getString("mysql.pass"));
        try {
            this.database.openConnection();
            this.database.updateSQL("CREATE TABLE IF NOT EXISTS `players` ( `id` int(11) NOT NULL AUTO_INCREMENT, `name` varchar(30) NOT NULL, `uuid` varbinary(16) NOT NULL, `coins` double NOT NULL, `fk_miner` int(11) DEFAULT '0' NOT NULL, `fk_better_bow` int(11) DEFAULT '0' NOT NULL, `fk_better_sword` int(11) DEFAULT '0' NOT NULL, `fk_better_armor` int(11) DEFAULT '0' NOT NULL, `fk_merlin` int(11) DEFAULT '0' NOT NULL, `created_at` datetime NOT NULL, `updated_at` datetime NOT NULL, PRIMARY KEY (`id`) ) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;");
        } catch (ClassNotFoundException|java.sql.SQLException e) {
            getLogger().severe("Impossible de se connecter la base de donn:");
            e.printStackTrace();
            getLogger().severe("Arrdu serveur...");
            Bukkit.shutdown();
            return;
        }
        Step.setCurrentStep(Step.LOBBY);
        World world = Bukkit.getWorlds().get(0);
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setTime(6000L);
        clearCommands(new String[] { "kill", "me", "tell" });
        register((Class<? extends UltraCore>[])new Class[] {
                BlockBreak.class, BlockPlace.class, CreatureSpawn.class, EntityDamage.class, EntityDamageByPlayer.class, EntityExplode.class, FoodLevelChange.class, PotionSplash.class, InventoryClick.class, AsyncPlayerChat.class,
                PlayerCommandPreprocess.class, PlayerDamage.class, PlayerDamageByPlayer.class, PlayerDeath.class, PlayerDropItem.class, PlayerInteract.class, PlayerItemConsume.class, PlayerJoin.class, PlayerKick.class, PlayerLogin.class,
                PlayerMove.class, PlayerPickupItem.class, PlayerQuit.class, PlayerRespawn.class, ServerListPing.class, ThunderChange.class, WeatherChange.class, ChunkUnload.class });
        Bukkit.getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
    }

    public void onDisable() {
        saveConfiguration();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Vous devez un joueur.");
            return true;
        }
        Player player = (Player)sender;
        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Plugin PvPSwap v1.0.1 | par Rellynn pour NexusMC.");
        } else {
            String sub = args[0];
            if (sub.equalsIgnoreCase("help")) {
                player.sendMessage(ChatColor.GOLD + "Aide du plugin PvPSwap :");
                player.sendMessage("/pvpswap setlobby" + ChatColor.YELLOW + " - dle lobby du jeu");
                player.sendMessage("/pvpswap additem" + ChatColor.YELLOW + " - dun item de swap");
                player.sendMessage("/pvpswap addfinalitem" + ChatColor.YELLOW + " - dun item de swap final");
                player.sendMessage("/pvpswap addswap map" + ChatColor.YELLOW + " - dun spawn de swap");
                player.sendMessage("/pvpswap addfinalswap" + ChatColor.YELLOW + " - dun spawn de swap final");
            } else if (sub.equalsIgnoreCase("setlobby")) {
                this.lobbyLocation = player.getLocation();
                player.sendMessage(ChatColor.GREEN + "Vous avez dle lobby avec réusite");
                        saveConfiguration();
            } else if (sub.equalsIgnoreCase("additem")) {
                Item item = createItemWithHand(player, Arrays.<String>copyOfRange(args, 1, args.length));
                if (item != null) {
                    this.items.add(item);
                    player.sendMessage(ChatColor.GREEN + "L'item a bien ajout!");
                }
            } else if (sub.equalsIgnoreCase("addfinalitem")) {
                Item finalItem = createItemWithHand(player, Arrays.<String>copyOfRange(args, 1, args.length));
                if (finalItem != null) {
                    this.finalItems.add(finalItem);
                    player.sendMessage(ChatColor.GREEN + "L'item a bien ajout!");
                }
            } else if (sub.equalsIgnoreCase("addswap") && args.length == 2) {
                player.sendMessage(ChatColor.GREEN + "Vous avez étais swap " + args[1] + ".");
                List<Location> locations = this.spawns.containsKey(args[1]) ? this.spawns.get(args[1]) : new ArrayList<>();
                locations.add(player.getLocation());
                this.spawns.put(args[1], locations);
                saveConfiguration();
            } else if (sub.equalsIgnoreCase("addfinalswap")) {
                Location location = player.getLocation();
                player.sendMessage(ChatColor.GREEN + "Vous avez dun spawn de swap final avec réusite");
                this.finalSpawns.add(location);
                saveConfiguration();
            } else if (sub.equalsIgnoreCase("head") && args.length == 2) {
                Head head = Head.valueOf(args[1]);
                player.getInventory().addItem(new ItemStack[] { head.getItem() });
            } else {
                sender.sendMessage(ChatColor.RED + "Mauvais arguments ou commande inexistante. Tapez " + ChatColor.DARK_RED + "/pvpswap help" + ChatColor.RED + " pour de l'aide.");
            }
            return true;
        }
        return false;
    }

    private void loadChunks(List<Location> spawns) {
        for (Location location : spawns) {
            location.getChunk().load();
            for (int x = -5; x < 5; x++) {
                for (int z = -5; z < 5; z++)
                    location.clone().add((x * 16), 0.0D, (z * 16)).getChunk().load();
            }
        }
    }

    private Item createItemWithHand(Player player, String[] args) {
        ItemStack inHand = player.getItemInHand();
        if (inHand == null || inHand.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "Vous devez avoir un item dans la main.");
            return null;
        }
        Item item = new Item(1.0F, inHand.getAmount(), inHand.getAmount(), inHand);
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = args).length, b = 0; b < i; ) {
            String arg = arrayOfString[b];
            if (arg.startsWith("rarity:")) {
                item.setRarity(Integer.parseInt(arg.replace("rarity:", "").replace("%", "")) / 100.0F);
            } else if (arg.startsWith("max:")) {
                item.setMaximum(Integer.parseInt(arg.replace("max:", "")));
            } else if (arg.startsWith("min:")) {
                item.setMinimum(Integer.parseInt(arg.replace("min:", "")));
            }
            b++;
        }
        return item;
    }

    public boolean isReady() {
        return (this.spawns.size() * 2 >= Bukkit.getMaxPlayers() && this.finalSpawns.size() >= 2);
    }

    private void loadConfiguration(World world) {
        saveDefaultConfig();
        String defaultLoc = LocationUtils.toString(world.getSpawnLocation());
        this.lobbyLocation = LocationUtils.toLocation(getConfig().getString("lobby", defaultLoc));
        this.spawns = new HashMap<>();
        this.unusedSpawns = new HashMap<>();
        if (getConfig().isConfigurationSection("default.spawns")) {
            ConfigurationSection mapSections = getConfig().getConfigurationSection("default.spawns");
            for (String map : mapSections.getKeys(false)) {
                List<Location> locations = new ArrayList<>();
                for (String stringLoc : mapSections.getStringList(map))
                    locations.add(LocationUtils.toLocation(stringLoc));
                this.spawns.put(map, locations);
                this.unusedSpawns.put(map, new ArrayList<>(locations));
            }
        }
        this.maps = new ArrayList<>(this.spawns.keySet());
        this.finalSpawns = new ArrayList<>();
        this.unusedFinalSpawns = new ArrayList<>();
        if (getConfig().isConfigurationSection("final.spawns")) {
            ConfigurationSection locationsSection = getConfig().getConfigurationSection("final.spawns");
            for (String stringKey : locationsSection.getKeys(false)) {
                Location location = LocationUtils.toLocation(locationsSection.getString(stringKey));
                this.finalSpawns.add(location);
                this.unusedFinalSpawns.add(location);
            }
        }
        this.items = loadItems("default.items");
        this.finalItems = loadItems("final.items");
    }

    private List<Item> loadItems(String key) {
        List<Item> items = new ArrayList<>();
        if (getConfig().isConfigurationSection(key)) {
            ConfigurationSection itemsSection = getConfig().getConfigurationSection(key);
            for (String itemKey : itemsSection.getKeys(false)) {
                ConfigurationSection item = itemsSection.getConfigurationSection(itemKey);
                items.add(new Item((float)item.getDouble("rarity"), item.getInt("minimum"), item.getInt("maximum"), item.getItemStack("itemstack")));
            }
        }
        return items;
    }

    private void saveConfiguration() {
        getConfig().set("lobby", LocationUtils.toString(this.lobbyLocation));
        getConfig().set("default", null);
        getConfig().set("final", null);
        for (Map.Entry<String, List<Location>> entry : this.spawns.entrySet()) {
            List<String> locations = new ArrayList<>();
            for (Location location : entry.getValue())
                locations.add(LocationUtils.toString(location));
            getConfig().set("default.spawns." + (String)entry.getKey(), locations);
        }
        int locationId = 0;
        for (Location spawn : this.finalSpawns) {
            getConfig().set("final.spawns." + locationId, LocationUtils.toString(spawn));
            locationId++;
        }
        saveItems("default.items", this.items);
        saveItems("final.items", this.finalItems);
        saveConfig();
    }

    private void saveItems(String key, List<Item> items) {
        int itemId = 0;
        for (Item item : items) {
            ConfigurationSection section = getConfig().createSection(String.valueOf(key) + "." + itemId);
            section.set("rarity", Float.valueOf(item.getRarity()));
            section.set("minimum", Integer.valueOf(item.getMinimum()));
            section.set("maximum", Integer.valueOf(item.getMaximum()));
            section.set("itemstack", item.getItemStack());
            itemId++;
        }
    }

    private void register(Class... classes) {
        try {
            byte b;
            int i;
            Class[] arrayOfClass;
            for (i = (arrayOfClass = classes).length, b = 0; b < i; ) {
                Class<? extends UltraCoreListener> clazz = arrayOfClass[b];
                Bukkit.getPluginManager().registerEvents(ReflectionHandler.getConstructor(clazz, new Class[] { UltraCore.class }).newInstance(new Object[] { this }), (Plugin)this);
                b++;
            }
        } catch (Throwable $ex) {
            throw ex;
        }
    }

    public int getOnMap(String map) {
        if (!this.onMaps.containsKey(map))
            return 0;
        return ((Integer)this.onMaps.get(map)).intValue();
    }

    private void refillSpawns() {
        this.lastMap = null;
        this.unusedSpawns.clear();
        for (Map.Entry<String, List<Location>> entry : this.spawns.entrySet())
            this.unusedSpawns.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        this.maps = new ArrayList<>(this.spawns.keySet());
    }

    public List<MapLocation> getUnusedSpawns() {
        this.onMaps.clear();
        List<MapLocation> locations = new ArrayList<>();
        int alivePlayers = getAlivePlayers().size();
        int counter = 0;
        while (locations.size() < alivePlayers) {
            if (counter >= this.maps.size())
                counter = 0;
            this.lastMap = this.maps.get(counter);
            List<Location> mapLocations = this.unusedSpawns.get(this.lastMap);
            if (mapLocations == null || mapLocations.isEmpty()) {
                this.maps.remove(counter);
                continue;
            }
            if (Math.floor((alivePlayers / this.spawns.size())) > 1.0D) {
                locations.add(new MapLocation(this.lastMap, mapLocations.remove(MathUtils.random(mapLocations.size() - 1))));
                this.onMaps.put(this.lastMap, Integer.valueOf(this.onMaps.containsKey(this.lastMap) ? (((Integer)this.onMaps.get(this.lastMap)).intValue() + 1) : 0));
            } else {
                for (Location mapLocation : mapLocations) {
                    locations.add(new MapLocation(this.lastMap, mapLocation));
                    this.onMaps.put(this.lastMap, Integer.valueOf(this.onMaps.containsKey(this.lastMap) ? (((Integer)this.onMaps.get(this.lastMap)).intValue() + 1) : 0));
                }
            }
            counter++;
        }
        refillSpawns();
        return locations;
    }

    public List<Item> getItems() {
        if (duelMode && GameRunnable.swapCount == 1) {
            List<Item> list = new ArrayList<>();
            list.add(new Item(1.0F, 1, 1, new ItemStack(Material.IRON_HELMET)));
            list.add(new Item(1.0F, 1, 1, new ItemStack(Material.IRON_CHESTPLATE)));
            list.add(new Item(1.0F, 1, 1, new ItemStack(Material.IRON_LEGGINGS)));
            list.add(new Item(1.0F, 1, 1, new ItemStack(Material.IRON_BOOTS)));
            list.add(new Item(1.0F, 1, 1, new ItemStack(Material.IRON_SWORD)));
            list.add(new Item(1.0F, 6, 15, new ItemStack(Material.COOKED_BEEF)));
            list.add(new Item(0.1F, 4, 4, new ItemStack(Material.EXPERIENCE_BOTTLE)));
            return list;
        }
        List<Item> items = (GameRunnable.swapCount == -1) ? this.finalItems : this.items;
        if (GameRunnable.swapCount == -1) {
            items = new ArrayList<>(items);
            items.addAll(this.items);
        }
        return items;
    }

    public Location getFinalSpawn() {
        return this.unusedFinalSpawns.remove(MathUtils.random(this.unusedFinalSpawns.size() - 1));
    }

    private void clearCommands(String... command) {
        try {
            final CommandMap commandMap = (CommandMap) ReflectionHandler.getField(Bukkit.getServer().getClass(), true, "commandMap").get(Bukkit.getServer());
            final Field knownCommands = ReflectionHandler.getField(commandMap.getClass(), true, "knownCommands");
            final Map<String, Command> commands = (Map<String, Command>)knownCommands.get(commandMap);
            (new BukkitRunnable() {
                public void run() {
                    try {
                        List<String> commandNames = Arrays.asList(command);
                        for (Map.Entry<String, Command> entry : (new HashMap<>(commands)).entrySet()) {
                            if (commandNames.contains(((Command)entry.getValue()).getName()))
                                commands.remove(entry.getKey());
                        }
                        knownCommands.set(commandMap, commands);
                    } catch (Throwable $ex) {
                        throw $ex;
                    }
                }
            }).runTaskLater((Plugin)this, 1L);
        } catch (Throwable $ex) {
            throw $ex;
        }
    }

    public void onPlayerLoose(Player player) {
        if (Step.isStep(Step.LOBBY)) {
            StatsUtils.removeData(player);
        } else if (Step.isStep(Step.IN_GAME)) {
            this.alivePlayers.remove(player);
            Bukkit.getScoreboardManager().getMainScoreboard().getObjective("players").getScore("Joueurs").setScore(this.alivePlayers.size());
            if (this.alivePlayers.size() == 1) {
                final Player winner = this.alivePlayers.iterator().next();
                (new BukkitRunnable() {
                    public void run() {
                        winner.setMetadata("SPECTATOR", (MetadataValue)new FixedMetadataValue((Plugin)UltraCore.this, Boolean.valueOf(true)));
                        Bukkit.broadcastMessage(String.valueOf(UltraCore.prefix) + ChatColor.GOLD + ChatColor.BOLD + "Victoire de " + winner.getName() + " " + ChatColor.YELLOW + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "|" + ChatColor.YELLOW + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.BOLD + " F" + ChatColor.YELLOW + ChatColor.MAGIC + " |" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "|" + ChatColor.YELLOW + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "|");
                    }
                }).runTaskLater((Plugin)this, 1L);
                StatsUtils.addCoins(winner, 15.0F);
                stopGame();
            }
        }
    }

    public void stopGame() {
        Step.setCurrentStep(Step.POST_GAME);
        for (Map.Entry<UUID, PlayerData> entry : (Iterable<Map.Entry<UUID, PlayerData>>) StatsUtils.getData().entrySet()) {
            final String uuid = ((UUID)entry.getKey()).toString().replaceAll("-", "");
            final PlayerData data = entry.getValue();
            (new BukkitRunnable() {
                public void run() {
                    try {
                        ResultSet res = UltraCore.this.database.querySQL("SELECT name FROM players WHERE uuid=UNHEX('" + uuid + "')");
                        if (res.first()) {
                            UltraCore.this.database.updateSQL("UPDATE players SET name='" + data.getName() + "', coins=coins+" + data.getCoins() + ", updated_at=NOW() WHERE uuid=UNHEX('" + uuid + "')");
                        } else {
                            UltraCore.this.database.updateSQL("INSERT INTO players(name, uuid, coins, created_at, updated_at) VALUES('" + data.getName() + "', UNHEX('" + uuid + "'), " + data.getCoins() + ", NOW(), NOW())");
                        }
                    } catch (ClassNotFoundException|java.sql.SQLException e) {
                        e.printStackTrace();
                    }
                }
            }).runTaskAsynchronously((Plugin)this);
        }
        (new BukkitRunnable() {
            public void run() {
                byte b;
                int i;
                Player[] arrayOfPlayer;
                for (i = (arrayOfPlayer = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, b = 0; b < i; ) {
                    Player online = arrayOfPlayer[b];
                    BungeeCordUtils.teleportToLobby((Plugin)UltraCore.this, online);
                    b++;
                }
            }
        }).runTaskLater((Plugin)this, 300L);
        (new BukkitRunnable() {
            public void run() {
                Bukkit.shutdown();
            }
        }).runTaskLater((Plugin)this, 400L);
    }
}
