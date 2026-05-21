package com.automationx.machines;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ConveyorPlacementSession {

    private final Player player;
    private final Location startShaft;
    private Location currentTarget;

    public ConveyorPlacementSession(Player player, Location startShaft) {
        this.player = player;
        this.startShaft = startShaft;
    }

    public Player getPlayer() { return player; }
    public Location getStart() { return startShaft; }

    public void setCurrentTarget(Location loc) { this.currentTarget = loc; }
    public Location getCurrentTarget() { return currentTarget; }
}
