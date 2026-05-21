package com.automationx.machines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ConveyorPlacer {

    public static boolean placeConveyorLine(Player player, Location start, Location end, Material conveyorMaterial) {
        World world = start.getWorld();
        Vector dir = end.toVector().subtract(start.toVector());
        double length = dir.length();
        dir.normalize();

        int blocks = (int) Math.ceil(length);
        int required = (int) Math.ceil(blocks / 16.0);

        ItemStack conveyorItem = new ItemStack(conveyorMaterial);

        if (!player.getInventory().containsAtLeast(conveyorItem, required)) {
            player.sendMessage("§cYou need §l" + required + "§c conveyors to build this line.");
            return false;
        }

        // Check intersections
        for (double d = 0; d <= length; d += 1.0) {
            Location point = start.clone().add(dir.clone().multiply(d));
            Block b = world.getBlockAt(point);
            if (b.getType() != Material.AIR && b.getType() != Material.CHAIN) {
                player.sendMessage("§c§lConveyors cannot intersect existing blocks!");
                return false;
            }
        }

        // Consume items
        player.getInventory().removeItem(new ItemStack(conveyorMaterial, required));

        // Place conveyors
        for (double d = 0; d <= length; d += 1.0) {
            Location point = start.clone().add(dir.clone().multiply(d));
            Block b = world.getBlockAt(point);
            b.setType(conveyorMaterial);

            Slab slab = (Slab) b.getBlockData();

            if (dir.getY() > 0) slab.setType(Slab.Type.TOP);
            else slab.setType(Slab.Type.BOTTOM);

            b.setBlockData(slab);
        }

        return true;
    }
}
