package com.bindglam.enchant;

import com.bindglam.enchant.listeners.BlockListener;
import com.bindglam.enchant.listeners.InventoryListener;
import com.bindglam.enchant.listeners.ItemListener;
import com.bindglam.enchant.managers.CompatibilityManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SandeulEnchantPlugin extends JavaPlugin {
    private static SandeulEnchantPlugin instance;

    private final CompatibilityManager compatibilityManager = new CompatibilityManager();

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ItemListener(), this);

        compatibilityManager.start();
    }

    @Override
    public void onDisable() {
        compatibilityManager.end();
    }

    public CompatibilityManager getCompatibilityManager() {
        return compatibilityManager;
    }

    public static SandeulEnchantPlugin getInstance() {
        return instance;
    }
}
