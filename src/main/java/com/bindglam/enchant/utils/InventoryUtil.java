package com.bindglam.enchant.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class InventoryUtil {
    private InventoryUtil() {
    }

    public static void filterInventory(Inventory inventory, Function<ItemStack, @Nullable ItemStack> function) {
        for(int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);

            if(itemStack == null) continue;

            inventory.setItem(i, function.apply(itemStack));
        }
    }

    public static void filterInventory(Inventory inventory, Predicate<ItemStack> predicate) {
        filterInventory(inventory, (Function<ItemStack, ItemStack>) (itemStack) -> {
            if(predicate.test(itemStack)) {
                return null;
            }
            return itemStack;
        });
    }

    public static void filterInventory(Inventory inventory, List<Material> blacklist) {
        filterInventory(inventory, (Predicate<ItemStack>) (itemStack) -> blacklist.contains(itemStack.getType()));
    }

    public static void filterList(List<ItemStack> list, Function<ItemStack, @Nullable ItemStack> function) {
        for(int i = list.size() - 1; i >= 0; i--) {
            ItemStack itemStack = list.get(i);

            if(itemStack == null) continue;

            ItemStack newItemStack = function.apply(list.get(i));

            if(newItemStack != null)
                list.set(i, newItemStack);
            else
                list.remove(i);
        }
    }

    public static void filterList(List<ItemStack> list, Predicate<ItemStack> predicate) {
        filterList(list, (Function<ItemStack, ItemStack>) (itemStack) -> {
            if(predicate.test(itemStack))
                return null;
            else
                return itemStack;
        });
    }

    public static void filterList(List<ItemStack> list, List<Material> blacklist) {
        filterList(list, (Predicate<ItemStack>) (itemStack) -> blacklist.contains(itemStack.getType()));
    }
}
