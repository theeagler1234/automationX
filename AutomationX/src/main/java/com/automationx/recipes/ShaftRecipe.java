package com.automationx.recipes;

import com.automationx.AutomationX;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.NamespacedKey;

public class ShaftRecipe {

    private final AutomationX plugin;

    public ShaftRecipe(AutomationX plugin) {
        this.plugin = plugin;
    }

    public void register() {
        NamespacedKey key = new NamespacedKey(plugin, "shaft_recipe");

        ShapelessRecipe recipe = new ShapelessRecipe(key, new ItemStack(Material.CHAIN, 1));
        recipe.addIngredient(Material.IRON_NUGGET);
        recipe.addIngredient(Material.COBBLESTONE);
        recipe.addIngredient(Material.COBBLESTONE);

        plugin.getServer().addRecipe(recipe);
    }
}
