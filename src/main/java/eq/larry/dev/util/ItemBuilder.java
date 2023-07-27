package eq.larry.dev.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
    private String title;

    private int amount;

    private short damage;

    private Material material;

    private List<String> lores = new ArrayList<>();

    private Map<Enchantment, Integer> enchantments = new HashMap<>();

    public ItemBuilder(ItemStack item) {
        this(item.getType(), item.getAmount(), item.getDurability());
    }

    public ItemBuilder(Material material) {
        this(material, 1, (short)0);
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, (short)0);
    }

    public ItemBuilder(Material material, int amount, short damage) {
        this.material = material;
        this.amount = amount;
        this.damage = damage;
    }

    public ItemBuilder(Material material, short durability) {
        this(material, 1, durability);
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, Integer.valueOf(level));
        return this;
    }

    public ItemBuilder addLores(String... lores) {
        this.lores.addAll(Arrays.asList(lores));
        return this;
    }

    public ItemStack build() {
        if (this.material == null)
            throw new NullPointerException("Material cannot be null!");
        ItemStack item = new ItemStack(this.material, this.amount, this.damage);
        if (!this.enchantments.isEmpty())
            item.addUnsafeEnchantments(this.enchantments);
        ItemMeta meta = item.getItemMeta();
        if (this.title != null)
            meta.setDisplayName(this.title);
        if (!this.lores.isEmpty())
            meta.setLore(this.lores);
        item.setItemMeta(meta);
        return item;
    }

    public ItemBuilder setTitle(String title) {
        this.title = title;
        return this;
    }
}

