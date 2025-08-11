package com.bindglam.enchant.utils;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public final class InventoryUtil {
    private InventoryUtil() {
    }

    public static void filterInventory(Inventory inventory, List<Material> blacklist) {
        for(int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);

            if(itemStack == null) continue;

            if(blacklist.contains(itemStack.getType())) {
                inventory.setItem(i, null);
            }
        }
    }

    public static void filterList(List<ItemStack> inventory, List<Material> blacklist) {
        for(int i = inventory.size() - 1; i >= 0; i--) {
            ItemStack itemStack = inventory.get(i);

            if(itemStack == null) continue;

            if(blacklist.contains(itemStack.getType())) {
                inventory.remove(i);
            }
        }
    }
}
