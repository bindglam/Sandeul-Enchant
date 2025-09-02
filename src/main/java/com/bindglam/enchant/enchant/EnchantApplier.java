package com.bindglam.enchant.enchant;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.view.EnchantmentView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class EnchantApplier {
    public static final List<RequiredMaterial> REQUIRED_MATERIALS = List.of(
            new RequiredMaterial(Key.key("sandeul-enchant:require-diamond"), Material.DIAMOND, 10),
            new RequiredMaterial(Key.key("sandeul-enchant:require-emerald"), Material.EMERALD, 10),
            new RequiredMaterial(Key.key("sandeul-enchant:require-netherite_ingot"), Material.NETHERITE_INGOT, 10)
    );
    public static final Key UPGRADE_RANDOM_50_ENCHANTMENT = Key.key("sandeul-enchant:upgrade-random-50");
    public static final Key UPGRADE_RANDOM_100_ENCHANTMENT = Key.key("sandeul-enchant:upgrade-random-100");
    public static final List<Key> EXCLUDED_ENCHANTMENTS = new ArrayList<>(List.of(UPGRADE_RANDOM_50_ENCHANTMENT, UPGRADE_RANDOM_100_ENCHANTMENT));

    private static final Registry<@NotNull Enchantment> ENCHANTMENT_REGISTRY = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);

    static {
        EXCLUDED_ENCHANTMENTS.addAll(REQUIRED_MATERIALS.stream().map(RequiredMaterial::key).toList());
    }


    private final EnchantmentView view;
    private final EnchantingInventory inventory;

    private final Random random = new Random();

    private ItemStack itemStack;
    private final List<Enchantment> applicableEnchantments = new ArrayList<>();

    public EnchantApplier(EnchantmentView view) {
        this.view = view;
        this.inventory = view.getTopInventory();
        this.itemStack = inventory.getItem();
    }

    public @Nullable EnchantmentOffer getOffer(int index) {
        if(itemStack == null) return null;

        RequiredMaterial requiredMaterial = REQUIRED_MATERIALS.get(index);

        int ingredientAmount = 0;

        ItemStack ingredientItemStack = inventory.getSecondary();
        if(ingredientItemStack != null && ingredientItemStack.getType() == requiredMaterial.material())
            ingredientAmount = ingredientItemStack.getAmount();

        EnchantmentOffer offer = new EnchantmentOffer(Objects.requireNonNull(ENCHANTMENT_REGISTRY.get(requiredMaterial.key())),
                1, 999);

        if(ingredientAmount >= requiredMaterial.amount()) {
            if(index == 0) {
                if(!applicableEnchantments.isEmpty()) {
                    Enchantment enchantment = applicableEnchantments.get(random.nextInt(applicableEnchantments.size()));

                    offer = new EnchantmentOffer(enchantment, 1, 30);
                } else {
                    offer = null;
                }
            } else if(index == 1) {
                if(canUpgrade(itemStack))
                    offer = new EnchantmentOffer(Objects.requireNonNull(ENCHANTMENT_REGISTRY.get(UPGRADE_RANDOM_50_ENCHANTMENT)), 1, 40);
                else
                    offer = null;
            } else if(index == 2) {
                if(canUpgrade(itemStack))
                    offer = new EnchantmentOffer(Objects.requireNonNull(ENCHANTMENT_REGISTRY.get(UPGRADE_RANDOM_100_ENCHANTMENT)), 1, 50);
                else
                    offer = null;
            }
        }

        return offer;
    }

    public void upgradeRandom() {
        if(itemStack == null) return;
        if(!canUpgrade(itemStack)) return;

        List<Enchantment> enchantments = itemStack.getEnchantments().keySet().stream().toList();

        Enchantment enchantment = enchantments.get(random.nextInt(enchantments.size()));
        while(itemStack.getEnchantmentLevel(enchantment) + 1 > enchantment.getMaxLevel()) {
            enchantment = enchantments.get(random.nextInt(enchantments.size()));
        }
        itemStack.addUnsafeEnchantment(enchantment, Math.min(itemStack.getEnchantmentLevel(enchantment) + 1, enchantment.getMaxLevel()));
    }

    public boolean canUpgrade(@NotNull ItemStack itemStack) {
        if(itemStack.getEnchantments().size() < 3)
            return false;

        for(Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
            if(entry.getValue() < entry.getKey().getMaxLevel())
                return true;
        }
        return false;
    }

    public void update() {
        itemStack = inventory.getItem();

        if(itemStack == null) return;

        applicableEnchantments.clear();

        for (Enchantment enchantment : ENCHANTMENT_REGISTRY.stream()
                .filter((enchantment) -> !EXCLUDED_ENCHANTMENTS.contains(enchantment.key()) && !itemStack.getEnchantments().containsKey(enchantment)).toList()) {
            if (enchantment.canEnchantItem(itemStack)) {
                applicableEnchantments.add(enchantment);
            }
        }
    }
}
