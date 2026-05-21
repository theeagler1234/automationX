package com.automationx.listeners;

import com.automationx.AutomationX;
import com.automationx.items.CustomItem;
import com.automationx.machines.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WrenchListener implements Listener {

    private final AutomationX plugin;
    private final GUIListener guiListener;

    public WrenchListener(AutomationX plugin, GUIListener guiListener) {
        this.plugin = plugin;
        this.guiListener = guiListener;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack held = event.getPlayer().getInventory().getItemInMainHand();
        String id = CustomItem.getItemId(held);

        Block clicked = event.getClickedBlock();
        if (clicked == null) return;

        // === CONVEYOR PLACEMENT MODE ===
        if ("conveyor".equals(id) && clicked.getType() == Material.CHAIN) {
            event.setCancelled(true);

            var sessions = plugin.getPlacementSessionManager();

            // Start session
            if (!sessions.has(event.getPlayer())) {
                ConveyorPlacementSession session =
                    new ConveyorPlacementSession(event.getPlayer(), clicked.getLocation());
                sessions.startSession(event.getPlayer(), session);
                event.getPlayer().sendMessage("§aConveyor placement started. Select the second shaft.");
                return;
            }

            // Finish session
            ConveyorPlacementSession session = sessions.get(event.getPlayer());

            if (!isValidShaftConnection(session.getStart().getBlock(), clicked)) {
                event.getPlayer().sendMessage("§cInvalid shaft connection.");
                sessions.endSession(event.getPlayer());
                return;
            }

            boolean ok = ConveyorPlacer.placeConveyorLine(
                event.getPlayer(),
                session.getStart(),
                clicked.getLocation(),
                Material.POLISHED_DEEPSLATE_SLAB
            );

            if (ok) event.getPlayer().sendMessage("§aConveyor placed.");
            sessions.endSession(event.getPlayer());
            return;
        }
    }

    private boolean isValidShaftConnection(Block a, Block b) {
        int dx = b.getX() - a.getX();
        int dy = b.getY() - a.getY();
        int dz = b.getZ() - a.getZ();

        if (dx == 0 && dz == 0) return true;
        if (dx == 0 && dy == 0) return true;
        if (dz == 0 && dy == 0) return true;

        if (dx == 0 && Math.abs(dy) == Math.abs(dz)) return true;
        if (dz == 0 && Math.abs(dy) == Math.abs(dx)) return true;

        return false;
    }
}
