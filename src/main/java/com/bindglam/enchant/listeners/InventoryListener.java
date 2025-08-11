package com.bindglam.enchant.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class InventoryListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraft(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        Recipe recipe = event.getRecipe();

        if(recipe.getResult().getType() == Material.ENCHANTING_TABLE) {
            event.setCancelled(true);

            player.sendMessage(Component.translatable(recipe.getResult()).append(Component.text("은(는) 이용할 수 없습니다.")).color(NamedTextColor.RED));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        ItemStack ingredient = event.getView().getItem(1);

        if(ingredient == null) return;

        if(ingredient.getType() == Material.ENCHANTED_BOOK) {
            event.setResult(null);
        }
    }
}
