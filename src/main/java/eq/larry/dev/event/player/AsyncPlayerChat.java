package eq.larry.dev.event.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Step;
import eq.larry.dev.sche.GameRunnable;
import eq.larry.dev.util.SpectatorUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat extends UltraCoreListener {
    public static List<UUID> specs = new ArrayList<>();

    public AsyncPlayerChat(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (Step.isStep(Step.IN_GAME) && GameRunnable.swapCount > 0 && SpectatorUtils.isSpectator(player)) {
            if (specs.contains(player.getUniqueId())) {
                event.setCancelled(true);
                player.sendMessage(String.valueOf(UltraCore.prefix) + ChatColor.RED + "Vous ne pouvez pas parler. " + ChatColor.WHITE + "Attendez un kill ! =)");
                return;
            }
            specs.add(player.getUniqueId());
        }
        event.setFormat(ChatColor.GRAY + "[" + ((!Step.isStep(Step.LOBBY) && SpectatorUtils.isSpectator(player)) ? (ChatColor.DARK_GRAY + "Spectateur") : (ChatColor.WHITE + "Joueur")) + ChatColor.GRAY + "] " + ChatColor.WHITE + player.getName() + ": " + event.getMessage());
    }
}
