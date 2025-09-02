package com.bindglam.enchant;

import com.bindglam.enchant.enchant.EnchantApplier;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class SandeulEnchantBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler((event) -> {
            EnchantApplier.REQUIRED_MATERIALS.forEach((material) -> event.registry().register(
                    EnchantmentKeys.create(material.key()),
                    (builder) -> builder.description(Component.translatable(material.material()).append(Component.text("가 부족합니다!")).color(NamedTextColor.RED))
                            .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_DURABILITY))
                            .anvilCost(0)
                            .maxLevel(1)
                            .weight(1)
                            .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(0, 0))
                            .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(0, 0))
                            .activeSlots(EquipmentSlotGroup.ANY)
                    )
            );

            event.registry().register(
                    EnchantmentKeys.create(EnchantApplier.UPGRADE_RANDOM_50_ENCHANTMENT),
                    (builder) -> builder.description(Component.text("50% 확률로 랜덤한 마법 강화하기").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD))
                            .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_DURABILITY))
                            .anvilCost(0)
                            .maxLevel(1)
                            .weight(1)
                            .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(0, 0))
                            .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(0, 0))
                            .activeSlots(EquipmentSlotGroup.ANY)
            );

            event.registry().register(
                    EnchantmentKeys.create(EnchantApplier.UPGRADE_RANDOM_100_ENCHANTMENT),
                    (builder) -> builder.description(Component.text("100% 확률로 랜덤한 마법 강화하기").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD))
                            .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_DURABILITY))
                            .anvilCost(0)
                            .maxLevel(1)
                            .weight(1)
                            .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(0, 0))
                            .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(0, 0))
                            .activeSlots(EquipmentSlotGroup.ANY)
            );
        }));
    }
}
