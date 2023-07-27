package eq.larry.dev.sche;

import eq.larry.dev.UltraCore;
import eq.larry.dev.hand.Step;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class BeginCountdown extends BukkitRunnable {
    public static boolean started = false;

    public static int timeUntilStart = 90;

    private UltraCore plugin;

    public BeginCountdown(UltraCore plugin) {
        this.plugin = plugin;
        started = true;
        runTaskTimer((Plugin)plugin, 0L, 20L);
    }

    public void run() {
        if (timeUntilStart <= 0) {
            cancel();
            if ((Bukkit.getOnlinePlayers()).length < 2) {
                Bukkit.broadcastMessage(String.valueOf(UltraCore.prefix) + ChatColor.RED + "Il n'y a pas assez de joueurs !");
                timeUntilStart = 90;
                started = false;
            } else {
                Step.setCurrentStep(Step.IN_GAME);
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                createObjective(scoreboard, "health", "health", ChatColor.DARK_RED + "").setDisplaySlot(DisplaySlot.BELOW_NAME);
                        createObjective(scoreboard, "kills", "playerKillCount", "kills").setDisplaySlot(DisplaySlot.PLAYER_LIST);
                Objective pvpSwap = createObjective(scoreboard, "players", "dummy", "PvPSwap " + ChatColor.RED + "00:00");
                pvpSwap.setDisplaySlot(DisplaySlot.SIDEBAR);
                pvpSwap.getScore(ChatColor.DARK_AQUA + "-------------").setScore(99);
                pvpSwap.getScore("Joueurs").setScore((Bukkit.getOnlinePlayers()).length);
                byte b;
                int i;
                Player[] arrayOfPlayer;
                for (i = (arrayOfPlayer = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, b = 0; b < i; ) {
                    Player player = arrayOfPlayer[b];
                    this.plugin.getAlivePlayers().add(player);
                    resetPlayer(player);
                    b++;
                }
                (new GameRunnable(this.plugin, pvpSwap)).doSwap();
            }
            return;
        }
        int remainingMins = timeUntilStart / 60 % 60;
        int remainingSecs = timeUntilStart % 60;
        if (timeUntilStart % 30 == 0 || (remainingMins == 0 && (remainingSecs % 10 == 0 || remainingSecs <= 5))) {
            String message = ChatColor.GOLD + "Ddu jeu dans " + ChatColor.YELLOW + ((remainingMins > 0) ? (String.valueOf(remainingMins) + " minute" + ((remainingMins > 1) ? "s" : "")) : "") + ((remainingSecs > 0) ? (String.valueOf((remainingMins > 0) ? " " : "") + remainingSecs + " seconde" + ((remainingSecs > 1) ? "s" : "")) : "") + ".";
            byte b;
            int i;
            Player[] arrayOfPlayer;
            for (i = (arrayOfPlayer = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, b = 0; b < i; ) {
                Player player = arrayOfPlayer[b];
                player.sendMessage(String.valueOf(UltraCore.prefix) + message);
                if (remainingMins == 0 && remainingSecs <= 10)
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                b++;
            }
        }
        timeUntilStart--;
    }

    private Objective createObjective(Scoreboard scoreboard, String name, String type, String displayName) {
        Objective objective = scoreboard.getObjective(name);
        if (objective != null)
            objective.unregister();
        objective = scoreboard.registerNewObjective(name, type);
        objective.setDisplayName(displayName);
        return objective;
    }

    public static void resetPlayer(Player player) {
        player.setFireTicks(0);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setExhaustion(5.0F);
        player.setFallDistance(0.0F);
        player.setExp(0.0F);
        player.setLevel(0);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.closeInventory();
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
    }
}