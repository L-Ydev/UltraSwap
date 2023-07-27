package eq.larry.dev.event.player;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Step;
import eq.larry.dev.util.SpectatorUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerRespawn extends UltraCoreListener {
    public PlayerRespawn(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!Step.isStep(Step.LOBBY)) {
            event.setRespawnLocation(this.plugin.getLobbyLocation());
            final Player player = event.getPlayer();
            (new BukkitRunnable() {
                public void run() {
                    SpectatorUtils.setSpectator((Plugin)PlayerRespawn.this.plugin, player);
                    if (player.hasMetadata("DEATH")) {
                        player.teleport((Location)((MetadataValue)player.getMetadata("DEATH").get(0)).value());
                        player.setFlying(true);
                        player.removeMetadata("DEATH", (Plugin)PlayerRespawn.this.plugin);
                    }
                }
            }).runTaskLater((Plugin)this.plugin, 1L);
        }
    }
}
