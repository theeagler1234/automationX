package com.automationx.items;

import com.automationx.AutomationX;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {

    private final AutomationX plugin;
    private final Map<String, CustomItem> items = new HashMap<>();

    public ItemRegistry(AutomationX plugin) {
        this.plugin = plugin;
        registerAll();
        plugin.getLogger().info("ItemRegistry loaded " + items.size() + " items.");
    }

    private void registerAll() {
        register(new CustomItem("iron_plate",   "§fIron Plate",   Material.IRON_INGOT,
            Arrays.asList("A flat sheet of iron.", "Used in machine construction.")));
        register(new CustomItem("steel_ingot",  "§7Steel Ingot",  Material.IRON_INGOT,
            Arrays.asList("Refined iron, smelted twice.", "Stronger than iron.")));
        register(new CustomItem("steel_plate",  "§7Steel Plate",  Material.IRON_INGOT,
            Arrays.asList("A flat sheet of steel.", "Used in advanced machines.")));
        register(new CustomItem("copper_plate", "§6Copper Plate", Material.COPPER_INGOT,
            Arrays.asList("A flat sheet of copper.", "Conducts electricity well.")));
        register(new CustomItem("copper_wire",  "§6Copper Wire",  Material.LEAD,
            Arrays.asList("Thin copper wire.", "Used in electrical components.")));
        register(new CustomItem("power_cable",  "§8Power Cable",  Material.LEAD,
            Arrays.asList("Insulated copper cable.", "Transfers electrical power.")));
        register(new CustomItem("rubber",       "§8Rubber",       Material.GRAY_DYE,
            Arrays.asList("Flexible rubber material.", "Used for insulation and seals.")));
        register(new CustomItem("wrench",       "§6Wrench",       Material.SHEARS,
            Arrays.asList("Used to activate machines.", "Right-click a machine controller.")));
        register(new CustomItem("heating_chamber", "§8Heating Chamber", Material.DROPPER,
            Arrays.asList("Used in the Forge structure.", "Place below an Iron Block",
                          "with fire underneath.")));
    }

    private void register(CustomItem item) { items.put(item.getId(), item); }

    public CustomItem get(String id) { return items.get(id); }
    public Map<String, CustomItem> getAll() { return Collections.unmodifiableMap(items); }
}
