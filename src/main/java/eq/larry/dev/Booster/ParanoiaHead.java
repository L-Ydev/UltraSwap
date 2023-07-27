package eq.larry.dev.Booster;

import eq.larry.dev.UltraCore;
import eq.larry.dev.hand.Head;
import eq.larry.dev.util.MathUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ParanoiaHead extends Head.HeadAction {
    public void onDamage(Player player, Player damager) {}

    public void onRun(Player player) {
        player.sendMessage(String.valueOf(UltraCore.prefix) + "Vos adversaires ne savent plus ou donner de la tête ");
                player.sendMessage(ChatColor.GOLD + "Les autres joueurs deviennent paranos..");
        for (Player alive : UltraCore.getInstance().getAlivePlayers()) {
            if (alive != player) {
                Sound[] sounds = { Sound.BLOCK_CHEST_OPEN, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, Sound.BLOCK_LADDER_STEP, Sound.ENTITY_BAT_AMBIENT, Sound.ENTITY_ARROW_SHOOT };
                final Sound sound = sounds[MathUtils.random(sounds.length - 1)];
                alive.playSound(alive.getLocation(), sound, 1.0F, 1.0F);
                if (sound == Sound.BLOCK_CHEST_OPEN) {
                    (new BukkitRunnable() {
                        public void run() {
                            alive.playSound(alive.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0F, 1.0F);
                        }
                    }).runTaskLater((Plugin)UltraCore.getInstance(), MathUtils.random(20, 40));
                    continue;
                }
                if (sound == Sound.BLOCK_LADDER_STEP) {
                    (new BukkitRunnable() {
                        public void run() {
                            alive.playSound(alive.getLocation(), sound, 1.0F, 1.0F);
                            if (MathUtils.randomBoolean()) {
                                cancel();
                                return;
                            }
                        }
                    }).runTaskLater((Plugin)UltraCore.getInstance(), 2L);
                    continue;
                }
                if (sound == Sound.ENTITY_WITCH_DRINK) {
                    (new BukkitRunnable() {
                        int count = 0;

                        public void run() {
                            if (this.count == 2) {
                                cancel();
                                return;
                            }
                            alive.playSound(alive.getLocation(), sound, 1.0F, 1.0F);
                            this.count++;
                        }
                    }).runTaskTimer((Plugin)UltraCore.getInstance(), 2L, 2L);
                    continue;
                }
                if (sound == Sound.ENTITY_BAT_HURT)
                    (new BukkitRunnable() {
                        boolean death = false;

                        public void run() {
                            if (this.death) {
                                alive.playSound(alive.getLocation(), Sound.ENTITY_BAT_HURT, 1.0F, 1.0F);
                                cancel();
                                return;
                            }
                            alive.playSound(alive.getLocation(), Sound.ENTITY_BAT_HURT, 1.0F, 1.0F);
                            this.death = MathUtils.randomBoolean();
                        }
                    }).runTaskTimer((Plugin)UltraCore.getInstance(), 0L, 10L);
            }
        }
    }
}