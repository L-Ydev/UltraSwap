package eq.larry.dev.Booster;

import eq.larry.dev.UltraCore;
import eq.larry.dev.hand.Head;
import eq.larry.dev.util.MathUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class BumpShieldHead extends Head.HeadAction {
    public void onDamage(Player player, Player damager) {
        player.sendMessage(String.valueOf(UltraCore.prefix) + "Votre bouclier vous a protde l'attaque !");
        damager.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1));
        damager.setVelocity(new Vector(MathUtils.random(0.8F), MathUtils.random(0.8F) + 0.6F, MathUtils.random(0.8F)));
    }

    public void onRun(Player player) {}
}
