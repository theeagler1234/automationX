package com.automationx.machines;

import com.automationx.AutomationX;
import org.bukkit.Location;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MachineManager {

    private final AutomationX plugin;
    private final Map<Location, Machine> machines = new HashMap<>();

    public MachineManager(AutomationX plugin) {
        this.plugin = plugin;
    }

    public void shutdown() {
        // TODO: save machines later
    }

    public Machine getMachineAt(Location loc) {
        return machines.get(loc);
    }

    public void registerMachine(Machine machine) {
        machines.put(machine.getControllerLocation(), machine);
    }

    public Collection<Machine> getActiveMachines() {
        return machines.values();
    }

    // TEMPORARY STUB — prevents ConveyorItemManager errors
    public boolean isInputZone(Location loc) {
        return false;
    }
}
