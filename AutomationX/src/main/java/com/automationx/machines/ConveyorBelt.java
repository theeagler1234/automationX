package com.automationx.machines;

import com.automationx.AutomationX;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;

import java.util.Collection;

public class ConveyorBelt extends Machine {

    public enum Direction { NORTH, SOUTH, EAST, WEST }
    public enum Mode { HORIZONTAL, UP, DOWN }

    private final AutomationX plugin;
    private Direction facing;
    private Mode mode;
    private static final int MOVE_INTERVAL = 10;

    public ConveyorBelt(Location location, AutomationX plugin, Direction facing, Mode mode) {
        super(location, "conveyor_belt");
        this.plugin  = plugin;
        this.facing  = facing;
        this.mode    = mode;
        setTicksRemaining(MOVE_INTERVAL);
    }

    @Override
    public boolean isStructureValid() {
        return getControllerBlock().getType() == Material.POLISHED_DEEPSLATE_SLAB;
    }

    @Override
    public boolean tick() {
        if (!isStructureValid()) return false;

        decrementTicks();
        if (getTicksRemaining() > 0) return false;
        setTicksRemaining(MOVE_INTERVAL);

        Location center = getControllerBlock().getLocation().add(0.5, 1.1, 0.5);
        Collection<Item> items = center.getWorld().getNearbyEntitiesByType(
            Item.class, center, 0.6
        );
        if (items.isEmpty()) return false;

        Item item = items.iterator().next();
        Vector velocity = switch (mode) {
            case HORIZONTAL -> switch (facing) {
                case NORTH -> new Vector(0,    0,   -0.2);
                case SOUTH -> new Vector(0,    0,    0.2);
                case EAST  -> new Vector(0.2,  0,    0);
                case WEST  -> new Vector(-0.2, 0,    0);
            };
            case UP   -> new Vector(0,  0.2, 0);
            case DOWN -> new Vector(0, -0.2, 0);
        };

        item.setVelocity(velocity);
        item.setPickupDelay(40);
        return true;
    }

    @Override
    public String[] getStats() {
        return new String[]{
            "§6=== Conveyor Belt ===",
            "§7Direction: §e" + facing.name(),
            "§7Mode: §e"      + mode.name(),
            "§7Interval: §e"  + MOVE_INTERVAL + " ticks"
        };
    }

    public Direction getFacing() { return facing; }
    public Mode getMode()        { return mode; }
    public void setFacing(Direction f) { this.facing = f; }
    public void setMode(Mode m)        { this.mode = m; }
}
