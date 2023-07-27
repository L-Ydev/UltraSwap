package eq.larry.dev.event.inventory;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Step;
import eq.larry.dev.util.SpectatorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClick extends UltraCoreListener {
    public InventoryClick(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if (Step.isStep(Step.LOBBY) || SpectatorUtils.isSpectator(player)) {
            event.setCancelled(true);
            if (event.getInventory().getName().contains("Menu Spectateur") && event.getRawSlot() == event.getSlot()) {
                ItemStack item = event.getCurrentItem();
                if (item != null && item.getType() == Material.SKELETON_SKULL) {
                    player.closeInventory();
                    Player alive = Bukkit.getPlayer(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                    if (!alive.isOnline()) {
                        player.sendMessage(ChatColor.GRAY + "Ce joueur n'est plus en ligne !");
                    } else {
                        player.teleport((Entity)alive);
                    }
                }
            }
        }
    }
}
