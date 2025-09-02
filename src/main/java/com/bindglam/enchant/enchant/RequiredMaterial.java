package com.bindglam.enchant.enchant;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public record RequiredMaterial(Key key, Material material, int amount) {
    public boolean canBeApplied(ItemStack itemStack) {
        return itemStack.getType() == material() && itemStack.getAmount() == amount();
    }
}
