package com.bindglam.enchant.listeners;

import com.bindglam.enchant.SandeulEnchantPlugin;
import com.bindglam.enchant.gui.SandeulEnchantGui;
import com.bindglam.enchant.utils.InventoryUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BlockListener implements Listener {
    private static final Function<ItemStack, @Nullable ItemStack> ENCHANTED_BOOK_FILTER = (itemStack) -> {
        ItemMeta meta = itemStack.getItemMeta();

        meta.removeEnchant(Enchantment.MENDING);

        if(itemStack.getType() == Material.ENCHANTED_BOOK && itemStack.getItemMeta() instanceof EnchantmentStorageMeta enchantmentStorageMeta) {
            enchantmentStorageMeta.removeStoredEnchant(Enchantment.MENDING);

            if (enchantmentStorageMeta.getStoredEnchants().isEmpty())
                return null;
        }

        itemStack.setItemMeta(meta);

        return itemStack;
    };

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if(!event.getAction().isRightClick()) return;

        if(block != null) {
            if(block.getState() instanceof Container container) {
                if(!player.isOp()) {
                    Bukkit.getAsyncScheduler().runNow(SandeulEnchantPlugin.getInstance(), (task) ->
                            InventoryUtil.filterInventory(container.getInventory(), ENCHANTED_BOOK_FILTER));
                }
            }

            if(block.getType() == Material.ENCHANTING_TABLE) {
                event.setCancelled(true);

                SandeulEnchantGui.open(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(BlockDropItemEvent event) {
        event.setCancelled(true);

        Block block = event.getBlock();
        List<ItemStack> drops = new ArrayList<>(event.getItems().stream().map(Item::getItemStack).toList());

        Bukkit.getAsyncScheduler().runNow(SandeulEnchantPlugin.getInstance(), (task) -> {
            InventoryUtil.filterList(drops, ENCHANTED_BOOK_FILTER);

            Bukkit.getScheduler().runTask(SandeulEnchantPlugin.getInstance(), () ->
                    drops.forEach((itemStack) -> block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.5, 0.5), itemStack)));
        });
    }
}
