package com.automationx.power;

import com.automationx.AutomationX;

public class PowerManager {
    private final AutomationX plugin;

    public PowerManager(AutomationX plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("PowerManager loaded.");
    }

    public void shutdown() {
        plugin.getLogger().info("PowerManager stopped.");
    }
}
