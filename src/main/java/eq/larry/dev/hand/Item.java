package eq.larry.dev.hand;

import java.beans.ConstructorProperties;
import org.bukkit.inventory.ItemStack;

public class Item {
    private float rarity;

    private int minimum;

    private int maximum;

    private ItemStack itemStack;

    public float getRarity() {
        return this.rarity;
    }

    public int getMinimum() {
        return this.minimum;
    }

    public int getMaximum() {
        return this.maximum;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setRarity(float rarity) {
        this.rarity = rarity;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Item))
            return false;
        Item other = (Item)o;
        if (!other.canEqual(this))
            return false;
        if (Float.compare(getRarity(), other.getRarity()) != 0)
            return false;
        if (getMinimum() != other.getMinimum())
            return false;
        if (getMaximum() != other.getMaximum())
            return false;
        Object this$itemStack = getItemStack(), other$itemStack = other.getItemStack();
        return !((this$itemStack == null) ? (other$itemStack != null) : !this$itemStack.equals(other$itemStack));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Item;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + Float.floatToIntBits(getRarity());
        result = result * 59 + getMinimum();
        result = result * 59 + getMaximum();
        Object $itemStack = getItemStack();
        return result * 59 + (($itemStack == null) ? 0 : $itemStack.hashCode());
    }

    public String toString() {
        return "Item(rarity=" + getRarity() + ", minimum=" + getMinimum() + ", maximum=" + getMaximum() + ", itemStack=" + getItemStack() + ")";
    }

    @ConstructorProperties({"rarity", "minimum", "maximum", "itemStack"})
    public Item(float rarity, int minimum, int maximum, ItemStack itemStack) {
        this.rarity = rarity;
        this.minimum = minimum;
        this.maximum = maximum;
        this.itemStack = itemStack;
    }
}
