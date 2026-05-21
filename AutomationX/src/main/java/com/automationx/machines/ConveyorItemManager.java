package com.automationx.machines;

import com.automationx.AutomationX;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ConveyorItemManager {

    private final Map<Location, ItemStack> items = new HashMap<>();
    private final AutomationX plugin;

    public ConveyorItemManager(AutomationX plugin) {
        this.plugin = plugin;
    }

    public boolean hasItem(Location loc) {
        return items.containsKey(loc);
    }

    public ItemStack getItem(Location loc) {
        return items.get(loc);
    }

    public void setItem(Location loc, ItemStack item) {
        if (item == null) items.remove(loc);
        else items.put(loc, item);
    }

    public void tick() {
        Map<Location, ItemStack> next = new HashMap<>();

        for (var entry : items.entrySet()) {
            Location loc = entry.getKey();
            ItemStack item = entry.getValue();

            Block b = loc.getBlock();
            if (b.getType() != Material.POLISHED_DEEPSLATE_SLAB) {
                continue; // conveyor removed
            }

            // Freeze if under machine input zone
            if (plugin.getMachineRegistry().getManager().isInputZone(loc)) {
                next.put(loc, item);
                continue;
            }

            // Determine next conveyor block
            Location nextLoc = ConveyorUtils.getNextLocation(loc);

            if (nextLoc == null) {
                next.put(loc, item);
                continue;
            }

            Block nextBlock = nextLoc.getBlock();

            // If next block is not a conveyor, item stays
            if (nextBlock.getType() != Material.POLISHED_DEEPSLATE_SLAB) {
                next.put(loc, item);
                continue;
            }

            // If next conveyor already has an item, backlog
            if (items.containsKey(nextLoc) || next.containsKey(nextLoc)) {
                next.put(loc, item);
                continue;
            }

            // Move item
            next.put(nextLoc, item);
        }

        items.clear();
        items.putAll(next);
    }
}
