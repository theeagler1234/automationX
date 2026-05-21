package com.automationx.machines;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.UUID;

public abstract class Machine {

    private final UUID id;
    private final Location controllerLocation;
    private final String machineType;
    private int ticksRemaining;
    private String lastProcessed1 = "None";
    private String lastProcessed2 = "None";
    private String lastProcessed3 = "None";

    public Machine(Location controllerLocation, String machineType) {
        this.id = UUID.randomUUID();
        this.controllerLocation = controllerLocation.clone();
        this.machineType = machineType;
        this.ticksRemaining = 0;
    }

    /**
     * Called every server tick by MachineManager.
     * Returns true if the machine successfully processed an item this tick cycle.
     */
    public abstract boolean tick();

    /**
     * Called when a player right-clicks the controller with a wrench.
     * Returns the stats lines to show the player.
     */
    public abstract String[] getStats();

    /**
     * Validate that the multiblock structure is still intact.
     */
    public abstract boolean isStructureValid();

    // --- Shared helpers ---

    protected void recordProcessed(String itemName) {
        lastProcessed3 = lastProcessed2;
        lastProcessed2 = lastProcessed1;
        lastProcessed1 = itemName;
    }

    public String[] getLastProcessed() {
        return new String[]{lastProcessed1, lastProcessed2, lastProcessed3};
    }

    public int getTicksRemaining() { return ticksRemaining; }
    public void setTicksRemaining(int ticks) { this.ticksRemaining = ticks; }
    public void decrementTicks() { if (ticksRemaining > 0) ticksRemaining--; }

    public UUID getId() { return id; }
    public Location getControllerLocation() { return controllerLocation.clone(); }
    public String getMachineType() { return machineType; }
    public Block getControllerBlock() { return controllerLocation.getBlock(); }
}
