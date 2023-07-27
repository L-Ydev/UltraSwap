package eq.larry.dev.event.player;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreItems;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Step;
import eq.larry.dev.sche.BeginCountdown;
import eq.larry.dev.util.SpectatorUtils;
import eq.larry.dev.util.StatsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class PlayerJoin extends UltraCoreListener {
    public PlayerJoin(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear();
        StatsUtils.loadData(player);
        if (!Step.canJoin() && player.hasPermission("games.join")) {
            event.setJoinMessage(null);
            SpectatorUtils.setSpectator((Plugin)this.plugin, player);
        } else if (Step.isStep(Step.LOBBY)) {
            event.setJoinMessage(String.valueOf(UltraCore.prefix) + ChatColor.WHITE + player.getName() + ChatColor.GRAY + " a rejoint la partie " + ChatColor.GREEN + "(" + (Bukkit.getOnlinePlayers()).length + "/" + Bukkit.getMaxPlayers() + ")");
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().setItem(8, UltraCoreItems.HUB_ITEM);
            player.teleport(this.plugin.getLobbyLocation());
            if (!BeginCountdown.started && !this.plugin.isReady()) {
                BeginCountdown.started = true;
            } else if (!BeginCountdown.started && (Bukkit.getOnlinePlayers()).length >= Bukkit.getMaxPlayers() / 2) {

            }
        }
    }
}
