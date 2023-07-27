package eq.larry.dev.event.player;


import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Step;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageByPlayer extends UltraCoreListener {
    public PlayerDamageByPlayer(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event) {
        if (UltraCore.godMode && event.getEntity() instanceof Player && (event.getDamager() instanceof Player || (event.getDamager() instanceof Projectile && ((Projectile)event.getDamager()).getShooter() instanceof Player))) {
            event.setCancelled(true);
            if (Step.isStep(Step.IN_GAME)) {
                Player damager = (event.getDamager() instanceof Player) ? (Player)event.getDamager() : (Player)((Projectile)event.getDamager()).getShooter();
                damager.sendMessage(String.valueOf(UltraCore.prefix) + ChatColor.RED + "Le pvp n'est pas activé");
            }
        }
    }
}
