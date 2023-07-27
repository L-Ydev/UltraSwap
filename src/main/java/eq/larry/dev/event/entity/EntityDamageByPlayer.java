package eq.larry.dev.event.entity;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Step;
import eq.larry.dev.util.SpectatorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByPlayer extends UltraCoreListener {
    public EntityDamageByPlayer(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event) {
        if (!Step.isStep(Step.IN_GAME) || (event.getDamager() instanceof Player && SpectatorUtils.isSpectator((Player)event.getDamager())))
            event.setCancelled(true);
    }
}
