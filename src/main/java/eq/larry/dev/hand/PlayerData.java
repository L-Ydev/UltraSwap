package eq.larry.dev.hand;


import java.beans.ConstructorProperties;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerData {
    private UUID uuid;

    private String name;

    private double coins;

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public double getCoins() {
        return this.coins;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PlayerData))
            return false;
        PlayerData other = (PlayerData)o;
        if (!other.canEqual(this))
            return false;
        Object this$uuid = getUuid(), other$uuid = other.getUuid();
        if ((this$uuid == null) ? (other$uuid != null) : !this$uuid.equals(other$uuid))
            return false;
        Object this$name = getName(), other$name = other.getName();
        return ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) ? false : (!(Double.compare(getCoins(), other.getCoins()) != 0));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PlayerData;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $uuid = getUuid();
        result = result * 59 + (($uuid == null) ? 0 : $uuid.hashCode());
        Object $name = getName();
        result = result * 59 + (($name == null) ? 0 : $name.hashCode());
        long $coins = Double.doubleToLongBits(getCoins());
        return result * 59 + (int)($coins ^ $coins >>> 32L);
    }

    public String toString() {
        return "PlayerData(uuid=" + getUuid() + ", name=" + getName() + ", coins=" + getCoins() + ")";
    }

    @ConstructorProperties({"uuid", "name", "coins"})
    public PlayerData(UUID uuid, String name, double coins) {
        this.uuid = uuid;
        this.name = name;
        this.coins = coins;
    }

    public void addCoins(double coins) {
        Player player = Bukkit.getPlayer(this.name);
        if (player != null && player.isOnline()) {
            this.coins += player.hasPermission("funcoins.mvpplus") ? (coins * 4.0D) : (player.hasPermission("funcoins.mvp") ? (coins * 3.0D) : (player.hasPermission("funcoins.vip") ? (coins * 2.0D) : coins));
            Bukkit.getPlayer(this.name).sendMessage(ChatColor.GRAY + "Gain de FunCoins + " + ChatColor.GOLD + String.valueOf(coins).replace(".", ","));
        }
    }
}
