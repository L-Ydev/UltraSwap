package eq.larry.dev.event.player;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Step;
import eq.larry.dev.util.SpectatorUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerItemConsume extends UltraCoreListener {
    public PlayerItemConsume(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent evt) {
        if (Step.isStep(Step.LOBBY) || SpectatorUtils.isSpectator(evt.getPlayer()))
            evt.setCancelled(true);
    }
}
