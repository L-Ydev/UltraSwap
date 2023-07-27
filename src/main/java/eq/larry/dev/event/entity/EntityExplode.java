package eq.larry.dev.event.entity;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Step;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode extends UltraCoreListener {
    public EntityExplode(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (Step.isStep(Step.LOBBY))
            event.setCancelled(true);
    }
}
