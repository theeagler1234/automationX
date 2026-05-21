package com.automationx.listeners;

import com.automationx.AutomationX;
import com.automationx.items.CustomItem;
import com.automationx.machines.ConveyorBelt;
import com.automationx.machines.Machine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ConveyorListener implements Listener {

    private final AutomationX plugin;

    public ConveyorListener(AutomationX plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block placed = event.getBlockPlaced();
        if (placed.getType() != Material.POLISHED_DEEPSLATE_SLAB) return;

        Player player = event.getPlayer();
        ConveyorBelt.Direction facing = getFacingFromPlayer(player);

        Block below = placed.getRelative(BlockFace.DOWN);
        ConveyorBelt.Mode mode = ConveyorBelt.Mode.HORIZONTAL;
        if (below.getType() == Material.POLISHED_DEEPSLATE_SLAB) {
            Machine existing = plugin.getMachineRegistry()
                .getManager().getMachineAt(below.getLocation());
            if (existing instanceof ConveyorBelt) mode = ConveyorBelt.Mode.UP;
        }

        ConveyorBelt belt = new ConveyorBelt(placed.getLocation(), plugin, facing, mode);
        plugin.getMachineRegistry().getManager().registerMachine(belt);
        player.sendMessage("§7Conveyor placed facing §e" + facing + " §8[" + mode + "]");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block broken = event.getBlock();
        if (broken.getType() != Material.POLISHED_DEEPSLATE_SLAB) return;
        Machine machine = plugin.getMachineRegistry()
            .getManager().getMachineAt(broken.getLocation());
        if (machine instanceof ConveyorBelt)
            plugin.getMachineRegistry().getManager()
                .getActiveMachines().remove(machine.getId());
    }

    @EventHandler
    public void onWrenchLeft(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        ItemStack held = event.getPlayer().getInventory().getItemInMainHand();
        if (!"wrench".equals(CustomItem.getItemId(held))) return;

        Block clicked = event.getClickedBlock();
        if (clicked == null || clicked.getType() != Material.POLISHED_DEEPSLATE_SLAB) return;

        Machine machine = plugin.getMachineRegistry()
            .getManager().getMachineAt(clicked.getLocation());
        if (!(machine instanceof ConveyorBelt belt)) {
            event.getPlayer().sendMessage("§cThis belt is not registered.");
            return;
        }

        event.setCancelled(true);
        showDirectionParticles(belt, event.getPlayer());
        event.getPlayer().sendMessage("§6Belt: §e" + belt.getFacing() + " §8| §e" + belt.getMode());
    }

    private void showDirectionParticles(ConveyorBelt belt, Player player) {
        Location base = belt.getControllerLocation().add(0.5, 1.2, 0.5);
        double dx = 0, dz = 0, dy = 0;
        switch (belt.getMode()) {
            case UP   -> dy =  0.3;
            case DOWN -> dy = -0.3;
            case HORIZONTAL -> {
                switch (belt.getFacing()) {
                    case NORTH -> dz = -0.3;
                    case SOUTH -> dz =  0.3;
                    case EAST  -> dx =  0.3;
                    case WEST  -> dx = -0.3;
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            Location p = base.clone().add(dx * i * 0.3, dy * i * 0.3, dz * i * 0.3);
            player.spawnParticle(Particle.FLAME, p, 1, 0, 0, 0, 0);
        }
    }

    private ConveyorBelt.Direction getFacingFromPlayer(Player player) {
        float yaw = player.getLocation().getYaw();
        if (yaw < 0) yaw += 360;
        if (yaw >= 315 || yaw < 45)  return ConveyorBelt.Direction.SOUTH;
        if (yaw >= 45  && yaw < 135) return ConveyorBelt.Direction.WEST;
        if (yaw >= 135 && yaw < 225) return ConveyorBelt.Direction.NORTH;
        return ConveyorBelt.Direction.EAST;
    }
}
