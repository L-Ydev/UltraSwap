package eq.larry.dev.event.world;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnload extends UltraCoreListener {
    public ChunkUnload(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        event.getHandlers();
    }
}
