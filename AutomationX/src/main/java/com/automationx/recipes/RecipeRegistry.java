package com.automationx.recipes;

import com.automationx.AutomationX;

public class RecipeRegistry {

    private final AutomationX plugin;

    public RecipeRegistry(AutomationX plugin) {
        this.plugin = plugin;
        registerAll();
        plugin.getLogger().info("RecipeRegistry loaded.");
    }

    private void registerAll() {
        SteelIngotRecipe.register(plugin);
        CopperWireRecipe.register(plugin);
        PowerCableRecipe.register(plugin);
        WrenchRecipe.register(plugin);
        HeatingChamberRecipe.register(plugin);
    }
}
