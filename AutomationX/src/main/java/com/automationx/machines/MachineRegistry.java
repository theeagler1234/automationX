package com.automationx.machines;

import com.automationx.AutomationX;

public class MachineRegistry {

    private final AutomationX plugin;
    private final MachineManager machineManager;

    public MachineRegistry(AutomationX plugin) {
        this.plugin = plugin;
        this.machineManager = new MachineManager(plugin);
        plugin.getLogger().info("MachineRegistry loaded.");
    }

    public MachineManager getManager() { return machineManager; }

    public void shutdown() { machineManager.shutdown(); }
}
