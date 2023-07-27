package eq.larry.dev.event.block;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Step;
import eq.larry.dev.util.SpectatorUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace extends UltraCoreListener {
        public BlockPlace(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (Step.isStep(Step.LOBBY) || SpectatorUtils.isSpectator(event.getPlayer()))
            event.setCancelled(true);
    }
}
