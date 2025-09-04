package com.bindglam.enchant.managers;

import com.bindglam.enchant.SandeulEnchantPlugin;
import com.bindglam.enchant.compatibility.Compatibility;
import com.bindglam.enchant.compatibility.excellentenchants.ExcellentEnchantsCompatibility;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompatibilityManager implements ManagerBase {
    private final List<Compatibility> compatibilities = List.of(new ExcellentEnchantsCompatibility());

    private final List<Compatibility> enabledCompatibilities = new ArrayList<>();
    private final Map<Enchantment, Integer> enchantments = new HashMap<>();

    @Override
    public void start() {
        compatibilities.forEach((comp) -> {
            if(Bukkit.getPluginManager().isPluginEnabled(comp.requiredPlugin())) {
                SandeulEnchantPlugin.getInstance().getLogger().info(comp.requiredPlugin() + " detected!");

                enabledCompatibilities.add(comp);
            }
        });

        enchantments.putAll(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)
                .stream().collect(HashMap::new, (map, ench) -> map.put(ench, ench.getMaxLevel()), HashMap::putAll));

        enabledCompatibilities.forEach((comp) -> {
            enchantments.putAll(comp.enchantments());
        });
    }

    @Override
    public void end() {
        enabledCompatibilities.clear();

        enchantments.clear();
    }

    public List<Compatibility> getEnabledCompatibilities() {
        return enabledCompatibilities;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }
}
