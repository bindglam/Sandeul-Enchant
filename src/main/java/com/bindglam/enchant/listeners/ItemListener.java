package com.bindglam.enchant.listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMend(PlayerItemMendEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item.containsEnchantment(Enchantment.MENDING)) {
            event.setCancelled(true);
        }
    }
}
