package eq.larry.dev.event.player;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Step;
import eq.larry.dev.sche.BeginCountdown;
import eq.larry.dev.util.SpectatorUtils;
import eq.larry.dev.util.StatsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerKick extends UltraCoreListener {
    public PlayerKick(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage(null);
        BeginCountdown.resetPlayer(event.getPlayer());
        if (Step.isStep(Step.IN_GAME) && !SpectatorUtils.isSpectator(event.getPlayer()))
            Bukkit.broadcastMessage(String.valueOf(UltraCore.prefix) + event.getPlayer().getName() + ChatColor.GRAY + " est mort en se d);
        if (!Step.isStep(Step.LOBBY))
            StatsUtils.showStatsMessage(event.getPlayer());
        this.plugin.onPlayerLoose(event.getPlayer());
    }
}
