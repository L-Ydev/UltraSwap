package eq.larry.dev.event.player;

import java.util.ArrayList;
import java.util.List;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreItems;
import eq.larry.dev.UltraCoreListener;
import eq.larry.dev.hand.Head;
import eq.larry.dev.hand.Item;
import eq.larry.dev.hand.Step;
import eq.larry.dev.sche.GameRunnable;
import eq.larry.dev.util.BungeeCordUtils;
import eq.larry.dev.util.MathUtils;
import eq.larry.dev.util.SpectatorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class PlayerInteract extends UltraCoreListener {
    private List<Location> chestsOpened;

    public PlayerInteract(UltraCore plugin) {
        super(plugin);
        this.chestsOpened = new ArrayList<>();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem() && event.getItem().isSimilar(UltraCoreItems.HUB_ITEM)) {
            event.setCancelled(true);
            BungeeCordUtils.teleportToLobby((Plugin)this.plugin, event.getPlayer());
        } else if (!Step.isStep(Step.LOBBY) && SpectatorUtils.isSpectator(event.getPlayer())) {
            event.setCancelled(true);
            if (event.hasItem() && event.getItem().isSimilar(UltraCoreItems.COMPASS_ITEM)) {
                Inventory inv = Bukkit.createInventory((InventoryHolder)event.getPlayer(), 27, "Menu Spectateur");
                for (Player alive : this.plugin.getAlivePlayers()) {
                    ItemStack skull = new ItemStack(Material.SKULL_BANNER_PATTERN, 1, (short)3);
                    SkullMeta meta = (SkullMeta)skull.getItemMeta();
                    meta.setOwner(alive.getName());
                    meta.setDisplayName(ChatColor.WHITE + alive.getName());
                    skull.setItemMeta((ItemMeta)meta);
                    inv.addItem(new ItemStack[] { skull });
                }
                event.getPlayer().openInventory(inv);
            }
        } else if (event.getAction().name().contains("RIGHT") && Step.isStep(Step.IN_GAME)) {
            if (event.hasItem() && event.getMaterial() == Material.SKELETON_SKULL) {
                byte b;
                int i;
                Head[] arrayOfHead;
                for (i = (arrayOfHead = Head.values()).length, b = 0; b < i; ) {
                    Head head = arrayOfHead[b];
                    if (head.getItem().isSimilar(event.getItem())) {
                        event.setCancelled(true);
                        removeItemInHand(event);
                        head.giveHeadEffect(event.getPlayer());
                        event.getPlayer().sendMessage("Tactive");
                        break;
                    }
                    b++;
                }
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block block = event.getClickedBlock();
                if (block.getState() instanceof Chest) {
                    if (!this.chestsOpened.contains(block.getLocation())) {
                        Chest chest = (Chest)block.getState();
                        boolean firstDuelSwap = (UltraCore.duelMode && GameRunnable.swapCount == 1);
                        if (firstDuelSwap &&
                                event.getPlayer().hasMetadata("CHEST_OPEN")) {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(String.valueOf(UltraCore.prefix) + ChatColor.RED + "Attendez le prochain swap pour ouvrir ce coffre.");
                            return;
                        }
                        List<Item> items = new ArrayList<>(this.plugin.getItems());
                        int random = 0;
                        if (firstDuelSwap) {
                            random = items.size();
                            event.getPlayer().setMetadata("CHEST_OPEN", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, Integer.valueOf(1)));
                        } else {
                            random = UltraCore.duelMode ? 12 : MathUtils.random(3, 6);
                            if (random > items.size())
                                random = items.size();
                        }
                        for (int i = 0; i < random; i++) {
                            Item randomItem = null;
                            while (randomItem == null && !items.isEmpty()) {
                                randomItem = items.remove(MathUtils.random(items.size() - 1));
                                if (!MathUtils.randomBoolean(randomItem.getRarity()))
                                    randomItem = null;
                            }
                            if (items.isEmpty())
                                break;
                            int slot = -1;
                            while (slot == -1) {
                                slot = MathUtils.random(chest.getInventory().getSize() - 1);
                                ItemStack at = chest.getInventory().getItem(slot);
                                if (at != null && at.getType() != Material.AIR)
                                    slot = -1;
                            }
                            ItemStack itemStack = randomItem.getItemStack().clone();
                            itemStack.setAmount(MathUtils.random(randomItem.getMinimum(), randomItem.getMaximum()));
                            chest.getInventory().setItem(slot, itemStack);
                        }
                        chest.update();
                        this.chestsOpened.add(block.getLocation());
                    }
                } else if (event.hasItem() && event.getMaterial().isBlock()) {
                    for (Entity entity : event.getPlayer().getNearbyEntities(1.0D, 1.0D, 1.0D)) {
                        if (entity instanceof Player && SpectatorUtils.isSpectator((Player)entity)) {
                            Block newBlock = block.getRelative(event.getBlockFace());
                            newBlock.setType(event.getItem().getType());
                            newBlock.setData(event.getItem().getData().getData());
                            removeItemInHand(event);
                            event.getPlayer().updateInventory();
                        }
                    }
                }
            }
        }
    }

    private void removeItemInHand(PlayerInteractEvent event) {
        if (event.getItem().getAmount() > 1) {
            event.getItem().setAmount(event.getItem().getAmount() - 1);
        } else {
            event.getPlayer().setItemInHand(null);
        }
    }
}
