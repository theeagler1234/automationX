package com.automationx.recipes;

import com.automationx.AutomationX;
import com.automationx.items.CustomItem;
import com.automationx.items.ItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class PowerCableRecipe implements Listener {

    private final AutomationX plugin;

    public PowerCableRecipe(AutomationX plugin) {
        this.plugin = plugin;
    }

    public static void register(AutomationX plugin) {
        PowerCableRecipe listener = new PowerCableRecipe(plugin);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        plugin.getLogger().info("Registered power cable recipe.");
    }

    @EventHandler
    public void onPrepare(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        ItemStack[] matrix = inv.getMatrix();

        ItemRegistry reg = plugin.getItemRegistry();
        CustomItem copperWireItem = reg.get("copper_wire");
        CustomItem rubberItem     = reg.get("rubber");

        int wireCount   = 0;
        int rubberCount = 0;
        int otherCount  = 0;

        for (ItemStack slot : matrix) {
            if (slot == null || slot.getType().isAir()) continue;

            if (copperWireItem.matches(slot)) {
                wireCount++;
            } else if (rubberItem.matches(slot)) {
                rubberCount++;
            } else {
                otherCount++;
            }
        }

        // Recipe: exactly 1 copper wire + 4 rubber, nothing else
        if (wireCount == 1 && rubberCount == 4 && otherCount == 0) {
            ItemStack result = reg.get("power_cable").createItem(1);
            event.getInventory().setResult(result);
        } else {
            // Clear result only if our items are involved, to avoid
            // interfering with other recipes
            if (wireCount > 0 || rubberCount > 0) {
                event.getInventory().setResult(null);
            }
        }
    }
}
