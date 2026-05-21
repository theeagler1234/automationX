package com.automationx.machines;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;

public class ConveyorUtils {

    public static Location getNextLocation(Location loc) {
        Block b = loc.getBlock();
        if (!(b.getBlockData() instanceof Slab slab)) return null;

        // Determine direction from slab type
        if (slab.getType() == Slab.Type.TOP) {
            // Upward conveyor
            return loc.clone().add(0, 1, 0);
        } else {
            // Flat or downward conveyor
            return loc.clone().add(0, -1, 0);
        }
    }
}
