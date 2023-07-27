package eq.larry.dev.Booster;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eq.larry.dev.UltraCore;
import eq.larry.dev.hand.Head;
import eq.larry.dev.util.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ChickenAttackHead extends Head.HeadAction {
    public void onDamage(Player player, Player damager) {}

    public void onRun(Player player) {
        Bukkit.broadcastMessage(String.valueOf(UltraCore.prefix) + player.getName() + " active <" + ChatColor.RED + "Pluie de poulets !!" + ChatColor.WHITE + ">");
        final Map<Player, List<Entity>> entities = new HashMap<>();
        for (Player alive : UltraCore.getInstance().getAlivePlayers()) {
            if (alive != player) {
                List<Entity> chickens = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    Chicken chicken = (Chicken)alive.getWorld().spawn(alive.getLocation().add(MathUtils.random(), (1.0F + MathUtils.random()), MathUtils.random()), Chicken.class);
                    chicken.setMaxHealth(2048.0D);
                    chicken.setHealth(2048.0D);
                    chicken.setVelocity(new Vector(0, 0, 0));
                    chickens.add(chicken);
                }
                entities.put(alive, chickens);
            }
        }
        (new BukkitRunnable() {
            int count = 0;

            public void run() {
                for (Map.Entry<Player, List<Entity>> entry : (Iterable<Map.Entry<Player, List<Entity>>>)entities.entrySet()) {
                    Player alive = entry.getKey();
                    for (Entity entity : entry.getValue()) {
                        if (!entity.isDead()) {
                            if (this.count >= 21) {
                                entity.remove();
                                continue;
                            }
                            entity.setVelocity(new Vector(0, 0, 0));
                            entity.teleport(alive.getLocation().add(MathUtils.random(), (1.0F + MathUtils.random()), MathUtils.random()));
                        }
                    }
                    if (this.count == 10 || this.count == 20) {
                        alive.damage(2.0D, ((List<Entity>)entry.getValue()).get(0));
                        continue;
                    }
                    if (this.count < 21 && this.count % 3 == 0) {
                        alive.playSound(alive.getLocation(), Sound.ENTITY_CHICKEN_HURT, 1.0F, 1.0F);
                        continue;
                    }
                    if (this.count >= 21)
                        cancel();
                }
                this.count++;
            }
        }).runTaskTimer((Plugin) UltraCore.getInstance(), 0L, 1L);
    }
}
