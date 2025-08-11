package com.bindglam.enchant.listeners;

import com.bindglam.enchant.SandeulEnchantPlugin;
import com.bindglam.enchant.utils.InventoryUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlockListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if(!event.getAction().isRightClick()) return;

        if(block != null) {
            if(block.getType() == Material.ENCHANTING_TABLE) {
                event.setCancelled(true);

                player.sendMessage(Component.translatable(block).append(Component.text("은(는) 이용할 수 없습니다.")).color(NamedTextColor.RED));
            }

            if(block.getState() instanceof Container container) {
                if(!player.isOp()) {
                    Bukkit.getAsyncScheduler().runNow(SandeulEnchantPlugin.getInstance(), (task) -> InventoryUtil.filterInventory(container.getInventory(), List.of(Material.ENCHANTED_BOOK)));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(BlockDropItemEvent event) {
        event.setCancelled(true);

        Block block = event.getBlock();
        List<ItemStack> drops = new ArrayList<>(event.getItems().stream().map(Item::getItemStack).toList());

        Bukkit.getAsyncScheduler().runNow(SandeulEnchantPlugin.getInstance(), (task) -> {
            InventoryUtil.filterList(drops, List.of(Material.ENCHANTED_BOOK));

            Bukkit.getScheduler().runTask(SandeulEnchantPlugin.getInstance(), () ->
                    drops.forEach((itemStack) -> block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.5, 0.5), itemStack)));
        });
    }
}
