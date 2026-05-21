package com.automationx.recipes;

import com.automationx.AutomationX;
import com.automationx.items.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.ItemStack;

public class CopperWireRecipe {

    public static void register(AutomationX plugin) {
        CustomItem copperWire = plugin.getItemRegistry().get("copper_wire");
        ItemStack result = copperWire.createItem(2); // 2 wires per craft

        NamespacedKey key = new NamespacedKey(plugin, "copper_wire_craft");
        ShapelessRecipe recipe = new ShapelessRecipe(key, result);
        recipe.addIngredient(Material.COPPER_INGOT);

        plugin.getServer().addRecipe(recipe);
        plugin.getLogger().info("Registered copper wire recipe.");
    }
}
