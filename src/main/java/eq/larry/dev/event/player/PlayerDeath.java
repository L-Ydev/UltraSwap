package eq.larry.dev.event.player;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Head;
import eq.larry.dev.hand.Step;
import eq.larry.dev.util.MathUtils;
import eq.larry.dev.util.SpectatorUtils;
import eq.larry.dev.util.StatsUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class PlayerDeath extends UltraCoreListener {
    public PlayerDeath(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (Step.isStep(Step.LOBBY) || SpectatorUtils.isSpectator(event.getEntity())) {
            event.setDeathMessage(null);
            event.getDrops().clear();
            event.setDroppedExp(0);
        } else {
            event.setDeathMessage(String.valueOf(UltraCore.prefix) + event.getDeathMessage());
            Player player = event.getEntity();
            Player killer = player.getKiller();
            if (killer != null) {
                AsyncPlayerChat.specs.clear();
                StatsUtils.addCoins(killer, 2.0F);
            }
            if (!UltraCore.duelMode) {
                Head head = Head.values()[MathUtils.random((Head.values()).length - 1)];
                player.getWorld().dropItemNaturally(player.getLocation(), head.getItem());
            }
            player.setMetadata("DEATH", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, player.getLocation()));
            this.plugin.onPlayerLoose(player);
        }
    }
}