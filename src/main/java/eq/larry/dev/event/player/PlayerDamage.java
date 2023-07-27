package eq.larry.dev.event.player;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.util.SpectatorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage extends UltraCoreListener {
    public PlayerDamage(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && SpectatorUtils.isSpectator((Player)event.getEntity()))
            event.setCancelled(true);
    }
}
