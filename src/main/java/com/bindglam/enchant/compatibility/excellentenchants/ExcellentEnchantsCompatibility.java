package com.bindglam.enchant.compatibility.excellentenchants;

import com.bindglam.enchant.compatibility.Compatibility;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentenchants.api.EnchantRegistry;

import java.util.HashMap;
import java.util.Map;

public class ExcellentEnchantsCompatibility implements Compatibility {
    @Override
    public @NotNull String requiredPlugin() {
        return "ExcellentEnchants";
    }

    @Override
    public @NotNull Map<Enchantment, Integer> enchantments() {
        return EnchantRegistry.getRegistered().stream().collect(HashMap::new,
                (map, ench) -> map.put(ench.getBukkitEnchantment(), ench.getDefinition().getMaxLevel()), HashMap::putAll);
    }
}
