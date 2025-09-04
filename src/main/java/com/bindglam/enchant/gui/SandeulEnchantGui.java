package com.bindglam.enchant.gui;

import com.bindglam.enchant.SandeulEnchantPlugin;
import com.bindglam.enchant.enchant.EnchantApplier;
import com.bindglam.enchant.enchant.RequiredMaterial;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.view.EnchantmentView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class SandeulEnchantGui implements Listener {
    private static final Map<UUID, SandeulEnchantGui> VIEWS = new HashMap<>();

    private final Player player;
    private final EnchantmentView view;
    private final ScheduledTask tickTask;

    private final EnchantApplier applier;

    private ItemStack prevItemStack = null;

    private SandeulEnchantGui(Player player) {
        this.player = player;
        this.view = MenuType.ENCHANTMENT.create(player);
        this.view.open();

        VIEWS.put(player.getUniqueId(), this);


        this.applier = new EnchantApplier(view);


        this.tickTask = Bukkit.getAsyncScheduler().runAtFixedRate(SandeulEnchantPlugin.getInstance(), this::tick, 0L, 50L, TimeUnit.MILLISECONDS);

        Bukkit.getPluginManager().registerEvents(this, SandeulEnchantPlugin.getInstance());
    }



    private void tick(ScheduledTask task) {
        if(!Objects.equals(prevItemStack, view.getTopInventory().getItem())) {
            prevItemStack = view.getTopInventory().getItem();

            applier.update();
        }

        view.setOffers(new EnchantmentOffer[]{ applier.getOffer(0), applier.getOffer(1), applier.getOffer(2) });
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!isMe(event.getView())) return;
        if(event.getRawSlot() != 1) return;

        event.setCancelled(true);

        ItemStack cursor = view.getCursor();
        view.setCursor(event.getCurrentItem());
        view.getTopInventory().setSecondary(cursor);
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        if(!isMe(event.getView())) return;

        event.setCancelled(true);

        if(EnchantApplier.REQUIRED_MATERIALS.stream().map(RequiredMaterial::key)
                .anyMatch(Predicate.isEqual(event.getEnchantmentHint().key()))) return;

        ItemStack itemStack = event.getItem();
        ItemStack ingredient = view.getTopInventory().getSecondary();
        if(ingredient == null) return;

        ingredient.setAmount(ingredient.getAmount() - 10);

        if(event.whichButton() == 0) {
            itemStack.addUnsafeEnchantment(event.getEnchantmentHint(), event.getLevelHint());
        } else if(event.whichButton() == 1) {
            if(Math.random() <= 0.5)
                applier.upgradeRandom();
        } else if(event.whichButton() == 2) {
            applier.upgradeRandom();
        }

        player.setLevel(Math.max(player.getLevel() - event.getExpLevelCost(), 0));
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if(!isMe(event.getView())) return;

        tickTask.cancel();

        HandlerList.unregisterAll(this);

        VIEWS.remove(player.getUniqueId());
    }



    private boolean isMe(@NotNull InventoryView otherView) {
        SandeulEnchantGui other = VIEWS.get(otherView.getPlayer().getUniqueId());

        if(other == null) return false;

        return other.getView() == otherView;
    }

    public EnchantmentView getView() {
        return view;
    }

    public static SandeulEnchantGui open(Player player) {
        return new SandeulEnchantGui(player);
    }
}
