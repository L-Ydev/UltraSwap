package eq.larry.dev.Booster;

import java.util.ArrayList;
import java.util.List;

import eq.larry.dev.UltraCore;
import eq.larry.dev.hand.Head;
import eq.larry.dev.util.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BatAttackHead extends Head.HeadAction {
    public void onDamage(Player player, Player damager) {}

    public void onRun(Player player) {
        Bukkit.broadcastMessage(String.valueOf(UltraCore.prefix) + player.getName() + " active <" + ChatColor.RED + "Chauvres-souris-ninjas !!" + ChatColor.WHITE + ">");
        final List<Entity> entities = new ArrayList<>();
        final List<Item> itemEntities = new ArrayList<>();
        for (Player alive : UltraCore.getInstance().getAlivePlayers()) {
            if (alive != player) {
                List<ItemStack> items = new ArrayList<>();
                byte b;
                int j;
                ItemStack[] arrayOfItemStack;
                for (j = (arrayOfItemStack = alive.getInventory().getContents()).length, b = 0; b < j; ) {
                    ItemStack item = arrayOfItemStack[b];
                    if (item != null && item.getType() != Material.AIR)
                        items.add(item);
                    b++;
                }
                for (int i = 0; i < 6; i++) {
                    Bat bat = (Bat)alive.getWorld().spawn(alive.getLocation().add(MathUtils.random(-2, 2), (1.0F + MathUtils.random(1.0F, 1.5F)), MathUtils.random(-2, 2)), Bat.class);
                    if (!items.isEmpty()) {
                        ItemStack itemStack = items.remove(MathUtils.random(items.size() - 1));
                        alive.getInventory().remove(itemStack);
                        Item item = bat.getWorld().dropItem(bat.getLocation(), itemStack);
                        item.setPickupDelay(2147483647);
                        bat.setPassenger((Entity)item);
                        bat.setHealth(0.5D);
                        bat.setCustomName("*niark*");
                        bat.setCustomNameVisible(true);
                        itemEntities.add(item);
                    }
                    entities.add(bat);
                }
            }
        }
        (new BukkitRunnable() {
            int count = 0;

            public void run() {
                if (this.count < 100) {
                    for (Entity entity : entities)
                        entity.setVelocity(new Vector(0, 0, 0));
                } else {
                    for (Entity entity : entities) {
                        if (!entity.isDead())
                            entity.remove();
                    }
                    cancel();
                }
                for (Item item : itemEntities) {
                    if (item.getVehicle() == null || item.getVehicle().isDead())
                        item.setPickupDelay(0);
                }
                this.count += 5;
            }
        }).runTaskTimer((Plugin) UltraCore.getInstance(), 0L, 5L);
    }
}
