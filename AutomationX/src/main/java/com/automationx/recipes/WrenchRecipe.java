package com.automationx.recipes;

import com.automationx.AutomationX;
import com.automationx.items.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class WrenchRecipe {

    public static void register(AutomationX plugin) {
        CustomItem wrench = plugin.getItemRegistry().get("wrench");
        ItemStack result = wrench.createItem(1);

        NamespacedKey key = new NamespacedKey(plugin, "wrench_craft");
        ShapedRecipe recipe = new ShapedRecipe(key, result);

        // Layout:
        //  _I_
        //  ISI
        //  _S_
        // I = iron ingot, S = stick
        recipe.shape(" I ", "ISI", " S ");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('S', Material.STICK);

        plugin.getServer().addRecipe(recipe);
        plugin.getLogger().info("Registered wrench recipe.");
    }
}
