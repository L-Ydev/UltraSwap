package eq.larry.dev.Booster;

import eq.larry.dev.UltraCore;
import eq.larry.dev.hand.Head;
import eq.larry.dev.util.MathUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ExpelliarmusShieldHead extends Head.HeadAction {
    public void onDamage(Player player, Player damager) {
        player.sendMessage(String.valueOf(UltraCore.prefix) + "Votre bouclier vous a protéger de  l'attaque !");
        damager.sendMessage(ChatColor.RED + "Expelliarmus ! ");
        ItemStack itemStack = damager.getItemInHand();
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            damager.setItemInHand(null);
            Item item = damager.getWorld().dropItem(damager.getLocation(), itemStack);
            item.setVelocity(new Vector(MathUtils.random(0.6F), MathUtils.random(0.4F), MathUtils.random(0.6F)));
        }
    }

    public void onRun(Player player) {}
}
