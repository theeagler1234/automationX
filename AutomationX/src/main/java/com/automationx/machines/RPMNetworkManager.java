package com.automationx.machines;

import com.automationx.AutomationX;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RPMNetworkManager {

    private final AutomationX plugin;

    public RPMNetworkManager(AutomationX plugin) {
        this.plugin = plugin;
    }

    public void tick() {
        // Placeholder: real RPM propagation can be added later
        // For now, we leave existing RPM values as-is.
    }

    public void applyGearboxConfig(Player player, Block core, int value) {
        double ratio = value / 64.0;
        Gearbox gb = new Gearbox(plugin, core);
        gb.setRatio(ratio);
        player.sendMessage("§aGearbox ratio set to §e" + ratio + "x");
    }

    public void applyRscConfig(Player player, Block core, int value) {
        RotationalSpeedController rsc = new RotationalSpeedController(plugin, core);
        rsc.setTargetRPM(value);
        player.sendMessage("§aRSC target RPM set to §e" + value);
    }
}
