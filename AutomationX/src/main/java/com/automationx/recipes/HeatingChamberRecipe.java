package com.automationx.recipes;

import com.automationx.AutomationX;
import com.automationx.items.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

public class HeatingChamberRecipe {

    public static void register(AutomationX plugin) {
        CustomItem heatingChamber = plugin.getItemRegistry().get("heating_chamber");
        ItemStack result = heatingChamber.createItem(1);

        NamespacedKey key = new NamespacedKey(plugin, "heating_chamber");
        ShapedRecipe recipe = new ShapedRecipe(key, result);

        // S S S
        // S F S
        // S S S
        // S = steel plate, F = furnace
        recipe.shape("SSS", "SFS", "SSS");
        ItemStack steelPlate = plugin.getItemRegistry().get("steel_plate").createItem(1);
        recipe.setIngredient('S', new RecipeChoice.ExactChoice(steelPlate));
        recipe.setIngredient('F', Material.FURNACE);

        plugin.getServer().addRecipe(recipe);
        plugin.getLogger().info("Registered heating chamber recipe.");
    }
}
