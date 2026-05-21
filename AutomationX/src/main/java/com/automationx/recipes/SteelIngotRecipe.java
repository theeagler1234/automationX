package com.automationx.recipes;

import com.automationx.AutomationX;
import com.automationx.items.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class SteelIngotRecipe {

    public static void register(AutomationX plugin) {
        CustomItem steelIngot = plugin.getItemRegistry().get("steel_ingot");
        ItemStack result = steelIngot.createItem();

        // Input: iron ingot -> steel ingot (via furnace/blast furnace)
        // cookingTime 400 = 20 seconds in a regular furnace (double normal smelt time)
        NamespacedKey key = new NamespacedKey(plugin, "steel_ingot_smelt");
        FurnaceRecipe recipe = new FurnaceRecipe(
            key,
            result,
            Material.IRON_INGOT,
            0.7f,  // experience
            400    // ticks (20 seconds — twice as long as normal)
        );

        plugin.getServer().addRecipe(recipe);
        plugin.getLogger().info("Registered steel ingot smelting recipe.");
    }
}
