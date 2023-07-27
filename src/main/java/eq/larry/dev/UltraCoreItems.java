package eq.larry.dev;

import eq.larry.dev.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface UltraCoreItems {
    public static final ItemStack HUB_ITEM = (new ItemBuilder(Material.RED_CARPET)).setTitle(ChatColor.GOLD + "Retourner au Hub").addLores(new String[] { ChatColor.GRAY + "Ou faites " + ChatColor.YELLOW + "/hub" }).build();

    public static final ItemStack COMPASS_ITEM = (new ItemBuilder(Material.COMPASS)).setTitle(ChatColor.AQUA + "Menu Spectateur").build();
}