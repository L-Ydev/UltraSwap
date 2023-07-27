package eq.larry.dev.Booster;

import java.beans.ConstructorProperties;

import eq.larry.dev.UltraCore;
import eq.larry.dev.hand.Head;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CameleonHead extends Head.HeadAction {
    public void onDamage(final Player player, final Player damager) {
        damager.sendMessage(String.valueOf(UltraCore.prefix) + "Et pouf ! Disparu !");
        Location playerLoc = player.getLocation();
        damager.playSound(playerLoc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0F, 0.8F);
        player.playSound(playerLoc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0F, 0.8F);
        player.sendMessage(String.valueOf(UltraCore.prefix) + "Vos rde camvous rendent temporairement invisible. Profitez en !");
        byte b;
        int i;
        Player[] arrayOfPlayer;
        for (i = (arrayOfPlayer = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, b = 0; b < i; ) {
            Player online = arrayOfPlayer[b];
            if (online != player)
                online.hidePlayer(player);
            b++;
        }
        final CameleonListener listener = new CameleonListener(player, damager, false);
        Bukkit.getPluginManager().registerEvents(listener, (Plugin)UltraCore.getInstance());
        (new BukkitRunnable() {
            public void run() {
                if (!listener.damage)
                    CameleonHead.this.resetCameleonEffect(listener, player, damager);
            }
        }).runTaskLater((Plugin)UltraCore.getInstance(), 60L);
    }

    private void resetCameleonEffect(CameleonListener listener, Player player, Player damager) {
        byte b;
        int i;
        Player[] arrayOfPlayer;
        for (i = (arrayOfPlayer = Bukkit.getOnlinePlayers().toArray(new Player[0])).length, b = 0; b < i; ) {
            Player online = arrayOfPlayer[b];
            if (online != player)
                online.showPlayer(player);
            b++;
        }
        player.sendMessage("*Pof*");
        player.sendMessage(ChatColor.RED + "Fin de l'invisibilit!!");
        damager.sendMessage("*Pof*");
        damager.sendMessage(ChatColor.RED + "Fin de l'invisibilit!!");
        HandlerList.unregisterAll(listener);
    }

    public class CameleonListener implements Listener {
        private Player player;

        private Player damager;

        private boolean damage;

        @ConstructorProperties({"player", "damager", "damage"})
        public CameleonListener(Player player, Player damager, boolean damage) {
            this.player = player;
            this.damager = damager;
            this.damage = damage;
        }

        @EventHandler
        public void onPlayerDamageByPlayer(EntityDamageByEntityEvent evt) {
            if (evt.getEntity() instanceof Player && (evt.getDamager() == this.player || (evt.getDamager() instanceof Projectile && ((Projectile)evt.getDamager()).getShooter() == this.player))) {
                CameleonHead.this.resetCameleonEffect(this, this.player, this.damager);
                this.damage = true;
            }
        }
    }

    public void onRun(Player player) {}
}
