package eq.larry.dev.event.entity;


import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PotionSplashEvent;

public class PotionSplash extends UltraCoreListener {
    public PotionSplash(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        if (UltraCore.godMode && event.getPotion().getShooter() instanceof Player) {
            Player damager = (Player)event.getPotion().getShooter();
            boolean player = false;
            for (LivingEntity entity : event.getAffectedEntities()) {
                if (entity instanceof Player) {
                    if (!player && entity != damager)
                        player = true;
                    event.setIntensity(entity, 0.0D);
                }
            }
            if (player)
                damager.sendMessage(String.valueOf(UltraCore.prefix) + ChatColor.RED + "Le pvp n'est pas activé");
        }
    }
}