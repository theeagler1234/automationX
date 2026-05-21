package com.automationx.recipes;

import com.automationx.AutomationX;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ConveyorRecipe {

    private final AutomationX plugin;

    public ConveyorRecipe(AutomationX plugin) {
        this.plugin = plugin;
    }

    // Rubber -> Conveyor (press recipe)
    public ItemStack getInput() {
        return new ItemStack(Material.SLIME_BALL); // placeholder for rubber
    }

    public ItemStack getOutput() {
        return new ItemStack(Material.POLISHED_DEEPSLATE_SLAB); // conveyor base item
    }

    public int getPressTime() {
        return 40; // 2 seconds at 20tps
    }
}
