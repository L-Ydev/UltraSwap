package eq.larry.dev.sche;

import java.util.List;
import java.util.Set;

import eq.larry.dev.UltraCore;
import eq.larry.dev.hand.MapLocation;
import eq.larry.dev.hand.Step;
import eq.larry.dev.util.MathUtils;
import eq.larry.dev.util.StatsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;

public class GameRunnable extends BukkitRunnable {
    private UltraCore plugin;

    public static int swapCount = 0;

    private int nextSwapAt = 0;

    private int totalSeconds = 0;

    private int minutes = 0;

    private int seconds = 0;

    private Objective pvpSwap;

    public GameRunnable(UltraCore plugin, Objective pvpSwap) {
        this.plugin = plugin;
        this.pvpSwap = pvpSwap;
        runTaskTimer((Plugin)UltraCore.getInstance(), 0L, 20L);
    }

    public void doSwap() {
        this.minutes = this.seconds = this.totalSeconds = 0;
        Set<Player> alivePlayers = this.plugin.getAlivePlayers();
        final boolean finalSwap = (swapCount >= 2 && alivePlayers.size() <= this.plugin.getFinalSpawns().size());
        byte b;
        int i;
        Player[] arrayOfPlayer;
        for (i = (arrayOfPlayer = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, b = 0; b < i; ) {
            Player online = arrayOfPlayer[b];
            if (finalSwap) {
                online.sendMessage(String.valueOf(UltraCore.prefix) + ChatColor.GOLD + "Quel bruit horrible ! Mais vous pla forme... Ceci le dernier swap, que le meilleur gagne !");
            } else if (UltraCore.duelMode && swapCount == 0) {
                for (int j = 0; j < 10; j++)
                    online.sendMessage("");
                online.sendMessage(ChatColor.RED + "Tirage pour la map Duels.");
            }
            if (alivePlayers.contains(online)) {
                online.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));
                if (!finalSwap) {
                    online.playSound(online.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 1.0F, 1.0F);
                } else {
                    online.playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0F, 1.0F);
                    StatsUtils.addCoins(online, 10.0F);
                }
            }
            b++;
        }
        this.nextSwapAt = finalSwap ? -1 : (((swapCount == 0) ? (UltraCore.duelMode ? 11 : 120) : MathUtils.random(UltraCore.duelMode ? 60 : 120, UltraCore.duelMode ? 120 : 180)) - 3);
        (new BukkitRunnable() {
            public void run() {
                for (Player player : GameRunnable.this.plugin.getAlivePlayers())
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
                (new BukkitRunnable() {
                    public void run() {
                        List<MapLocation> mapLocations = (GameRunnable.null.access(GameRunnable.null.this)).plugin.getUnusedSpawns();
                        Set<Player> alivePlayers = (GameRunnable.null.access(GameRunnable.null.this)).plugin.getAlivePlayers();
                        for (Player alive : alivePlayers) {
                            if (finalSwap) {
                                alive.setMaxHealth(30.0D);
                                alive.setHealth(30.0D);
                            } else if (GameRunnable.swapCount == 0) {
                                alive.sendMessage(String.valueOf(UltraCore.prefix) + ChatColor.RED + "Pvp désactiver " + ChatColor.GRAY + "Prochain swap dans " + ChatColor.GOLD + "2m00s" + ChatColor.GRAY + ", Ouvrez les " + ChatColor.GOLD + "coffres " + ChatColor.GRAY + "!");
                                alive.sendMessage(String.valueOf(UltraCore.prefix) + ChatColor.GRAY + "Activez votre " + ChatColor.RED + "son " + ChatColor.GRAY + "! A chaque swap, un " + ChatColor.RED + "coup de canon " + ChatColor.GRAY + "indique la prd'un " + ChatColor.RED + "adversaire" + ChatColor.GRAY + ".");
                            } else if (GameRunnable.swapCount == 1) {
                                UltraCore.godMode = false;
                                alive.sendMessage(String.valueOf(UltraCore.prefix) + ChatColor.RED + "Pvp activer ! " + ChatColor.GOLD + "Prvotre !");
                                alive.sendMessage(String.valueOf(UltraCore.prefix) + ChatColor.GOLD + "Vous serez rswaptoutes les " + ChatColor.BLUE + "2m00s" + ChatColor.GOLD + " " + ChatColor.BLUE + "3m00s" + ChatColor.GOLD + ".");
                            }
                            alive.setFallDistance(0.0F);
                            if (finalSwap) {
                                alive.teleport((GameRunnable.null.access(GameRunnable.null.this)).plugin.getFinalSpawn());
                            } else {
                                MapLocation mapLocation = mapLocations.remove(MathUtils.random(mapLocations.size() - 1));
                                String map = mapLocation.getMap();
                                if (alive.hasMetadata("MAP"))
                                    alive.removeMetadata("MAP", (Plugin)(GameRunnable.null.access(GameRunnable.null.this)).plugin);
                                alive.setMetadata("MAP", (MetadataValue)new FixedMetadataValue((Plugin)(GameRunnable.null.access(GameRunnable.null.this)).plugin, map));
                                (new BukkitRunnable(finalSwap, alivePlayers, map) {
                                    int count;

                                    int loopCount;

                                    public void run() {
                                        if (this.loopCount == this.count) {
                                            cancel();
                                            return;
                                        }
                                        alive.playSound(alive.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 0.5F);
                                        this.loopCount++;
                                    }
                                }).runTaskTimer((Plugin)(GameRunnable.null.access(GameRunnable.null.this)).plugin, 20L, 20L);
                                alive.teleport(mapLocation.getLocation());
                            }
                            alive.playSound(alive.getLocation(), Sound.ENTITY_ENDERMAN_AMBIENT, 1.0F, 1.0F);
                        }
                        GameRunnable.swapCount = finalSwap ? -1 : (GameRunnable.swapCount + 1);
                    }
                }).runTaskLater((Plugin)GameRunnable.this.plugin, 20L);
            }
        }).runTaskLater((Plugin)this.plugin, 40L);
    }

    public void run() {
        if (!Step.isStep(Step.IN_GAME)) {
            cancel();
            return;
        }
        this.pvpSwap.setDisplayName("PvPSwap " + ChatColor.RED + "0" + this.minutes + ":" + ((this.seconds < 10) ? "0" : "") + this.seconds);
        if (this.totalSeconds == this.nextSwapAt)
            doSwap();
        this.totalSeconds++;
        this.seconds++;
        if (this.seconds == 60) {
            this.minutes++;
            this.seconds = 0;
        }
    }
