package com.bindglam.enchant.compatibility;

import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface Compatibility {
    @NotNull String requiredPlugin();

    @NotNull Map<Enchantment, Integer> enchantments();
}
