package eq.larry.dev.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import eq.larry.dev.hand.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StatsUtils {
    public static Map<UUID, PlayerData> getData() {
        return data;
    }

    private static Map<UUID, PlayerData> data = new HashMap<>();

    public static void loadData(Player player) {
        data.put(player.getUniqueId(), new PlayerData(player.getUniqueId(), player.getName(), 0.0D));
    }

    public static void addCoins(Player player, float coins) {
        getData(player).addCoins(coins);
    }

    public static void showStatsMessage(Player player) {
        PlayerData data = getData(player);
        player.sendMessage(ChatColor.GOLD + "-----------------------------------------------------");
        player.sendMessage(ChatColor.GOLD + "Fin de partie sur " + ChatColor.GREEN + "PvPSwap");
        player.sendMessage(ChatColor.GRAY + "Gain total de " + ChatColor.YELLOW + "FunCoins " + ChatColor.GRAY + "sur la partie : " + ChatColor.YELLOW + data.getCoins());
        player.sendMessage(ChatColor.GOLD + "-----------------------------------------------------");
    }

    public static PlayerData getData(Player player) {
        PlayerData data = StatsUtils.data.get(player.getUniqueId());
        if (data == null) {
            player.kickPlayer(ChatColor.RED + "Erreur");
            return null;
        }
        return data;
    }

    public static void removeData(Player player) {
        data.remove(player.getUniqueId());
    }
}

