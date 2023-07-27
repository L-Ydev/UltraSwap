package eq.larry.dev.event.entity;


import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawn extends UltraCoreListener {
    public CreatureSpawn(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM)
            event.setCancelled(true);
    }
}
