package com.bindglam.enchant;

import com.bindglam.enchant.listeners.BlockListener;
import com.bindglam.enchant.listeners.InventoryListener;
import com.bindglam.enchant.listeners.ItemListener;
import org.bukkit.plugin.java.JavaPlugin;

public class SandeulEnchantPlugin extends JavaPlugin {
    private static SandeulEnchantPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
    }

    @Override
    public void onDisable() {
    }

    public static SandeulEnchantPlugin getInstance() {
        return instance;
    }
}
