package eq.larry.dev.hand;

import java.beans.ConstructorProperties;

import eq.larry.dev.UltraCore;
import eq.larry.dev.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public enum Head {
    BAT_ATTACK("Bat-Attack", "Des chauves souris\ns'emparent du stuff\nde vos adversaires", (HeadAction)new BatAttackHead(), HeadType.ON_RUN),
    CAMELEON("Cameleon", "Le camdevient provisoirement\ninvisible en cas d'aggression", (HeadAction)new CameleonHead(), HeadType.ON_DAMAGE),
    BUMP_SHIELD("BumpShield", "Votre aggresseur aura une petite surprise...", (HeadAction)new BumpShieldHead(), HeadType.ON_DAMAGE),
    EXPELLIARMUS_SHIELD("ExpelliarmusShield", "Projete l'item en main\nde votre prochain aggresseur\ncomme dans Harry Potter !", (HeadAction)new ExpelliarmusShieldHead(), HeadType.ON_DAMAGE),
    CHICKEN_ATTACK("Chicken", "Des poulets attaquent les autres joueurs", (HeadAction)new ChickenAttackHead(), HeadType.ON_RUN),
    PARANOIA("Parano", "Et tous vos adversaires\ndeviennent paranos...", (HeadAction)new ParanoiaHead(), HeadType.ON_RUN);

    private String name;

    private String description;

    private HeadAction action;

    private HeadType type;

    private ItemStack itemStack;

    public enum HeadType {
        ON_RUN, ON_DAMAGE;
    }

    public class HeadDamageListener implements Listener {
        private Player player;

        private Head head;

        @ConstructorProperties({"player", "head"})
        public HeadDamageListener(Player player, Head head) {
            this.player = player;
            this.head = head;
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerDamageByPlayer(EntityDamageByEntityEvent evt) {
            if (!evt.isCancelled() && evt.getEntity() instanceof Player && evt.getDamager() instanceof Player &&
                    evt.getEntity() == this.player) {
                evt.setCancelled(true);
                this.head.action.onDamage(this.player, (Player)evt.getDamager());
                EntityDamageEvent.getHandlerList().unregister(this);
            }
        }
    }

    public static abstract class HeadAction {
        public abstract void onDamage(Player param1Player1, Player param1Player2);

        public abstract void onRun(Player param1Player);
    }

    Head(String name, String description, HeadAction action, HeadType type) {
        this.name = name;
        this.description = description;
        this.action = action;
        this.type = type;
    }

    public void giveHeadEffect(Player player) {
        if (this.type == HeadType.ON_RUN) {
            this.action.onRun(player);
        } else if (this.type == HeadType.ON_DAMAGE) {
            Bukkit.getPluginManager().registerEvents(new HeadDamageListener(player, this), (Plugin) UltraCore.getInstance());
        }
    }

    public ItemStack getItem() {
        if (this.itemStack == null) {
            ItemBuilder builder = new ItemBuilder(Material.SKELETON_SKULL, (short)3);
            builder.setTitle(ChatColor.WHITE + ChatColor.ITALIC + this.name + ChatColor.GOLD + " (Clic Droit)");
            builder.addLores(new String[] { ChatColor.DARK_PURPLE + ChatColor.ITALIC + this.name, "" });
            byte b;
            int i;
            String[] arrayOfString;
            for (i = (arrayOfString = this.description.split("\n")).length, b = 0; b < i; ) {
                String line = arrayOfString[b];
                builder.addLores(new String[] { ChatColor.DARK_PURPLE + ChatColor.ITALIC + line });
                b++;
            }
            this.itemStack = builder.build();
        }
        return this.itemStack;
    }
}

