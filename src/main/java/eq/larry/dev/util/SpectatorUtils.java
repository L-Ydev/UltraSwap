package eq.larry.dev.util;

import eq.larry.dev.UltraCoreItems;
import eq.larry.dev.hand.Step;
import eq.larry.dev.sche.GameRunnable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpectatorUtils {
    public static boolean isSpectator(Player player) {
        return !(!player.hasMetadata("SPECTATOR") && (!Step.isStep(Step.IN_GAME) || GameRunnable.swapCount != 0));
    }

    public static void setSpectator(Plugin plugin, Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2147483647, 1));
        player.setMetadata("SPECTATOR", (MetadataValue)new FixedMetadataValue(plugin, Boolean.valueOf(true)));
        player.setAllowFlight(true);
        player.getInventory().setItem(0, UltraCoreItems.COMPASS_ITEM);
        player.getInventory().setItem(8, UltraCoreItems.HUB_ITEM);
        byte b;
        int i;
        Player[] arrayOfPlayer;
        for (i = (arrayOfPlayer = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, b = 0; b < i; ) {
            Player online = arrayOfPlayer[b];
            if (player != online) {
                player.showPlayer(online);
                if (!Step.isStep(Step.IN_GAME) || !isSpectator(online))
                    online.hidePlayer(player);
            }
            b++;
        }
    }
}
