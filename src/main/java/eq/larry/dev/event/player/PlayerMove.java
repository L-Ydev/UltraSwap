package eq.larry.dev.event.player;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Step;
import eq.larry.dev.sche.GameRunnable;
import eq.larry.dev.util.SpectatorUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove extends UltraCoreListener {
    public PlayerMove(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ())
            if (Step.isStep(Step.IN_GAME) && GameRunnable.swapCount == 0 && from.getBlockY() == to.getBlockY()) {
                event.setTo(event.getFrom());
            } else if (to.getBlockY() <= 0 && (Step.isStep(Step.LOBBY) || SpectatorUtils.isSpectator(event.getPlayer()))) {
                event.getPlayer().sendMessage(String.valueOf(UltraCore.prefix) + "Vous ne pouvez pas sortir de la WaitingRoom");
                event.getPlayer().teleport(this.plugin.getLobbyLocation());
            }
    }
}