package eq.larry.dev.event.player;


import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Step;
import eq.larry.dev.util.SpectatorUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItem extends UltraCoreListener {
    public PlayerPickupItem(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (Step.isStep(Step.LOBBY) || SpectatorUtils.isSpectator(event.getPlayer()))
            event.setCancelled(true);
    }
}
