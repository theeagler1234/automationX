package com.automationx.listeners;

import com.automationx.AutomationX;
import com.automationx.machines.Machine;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class AnvilGravityListener implements Listener {

    private final AutomationX plugin;

    public AnvilGravityListener(AutomationX plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.ANVIL &&
            block.getType() != Material.CHIPPED_ANVIL &&
            block.getType() != Material.DAMAGED_ANVIL) return;

        Machine machine = plugin.getMachineRegistry()
            .getManager().getMachineAt(block.getLocation());

        if (machine != null && machine.getMachineType().equals("hydraulic_press"))
            event.setCancelled(true);
    }
}
