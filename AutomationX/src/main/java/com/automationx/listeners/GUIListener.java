package com.automationx.listeners;

import com.automationx.AutomationX;
import com.automationx.gui.ForgeGUI;
import com.automationx.gui.MachineGUI;
import com.automationx.gui.PressGUI;
import com.automationx.machines.Forge;
import com.automationx.machines.HydraulicPress;
import com.automationx.machines.Machine;
import com.automationx.machines.MechanicalPress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIListener implements Listener {

    private final AutomationX plugin;
    private final Map<UUID, MachineGUI> openGUIs = new HashMap<>();

    public GUIListener(AutomationX plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player, Machine machine) {
        MachineGUI gui;

        if (machine instanceof Forge forge) {
            ForgeGUI forgeGUI = new ForgeGUI(forge);
            forge.setGuiInventory(forgeGUI.getInventory());
            gui = forgeGUI;
        } else if (machine instanceof MechanicalPress || machine instanceof HydraulicPress) {
            gui = new PressGUI(machine);
        } else {
            for (String line : machine.getStats()) player.sendMessage(line);
            return;
        }

        openGUIs.put(player.getUniqueId(), gui);
        gui.open(player);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        MachineGUI gui = openGUIs.get(player.getUniqueId());
        if (gui == null) return;

        Inventory clicked = event.getClickedInventory();
        if (clicked == null || !clicked.equals(gui.getInventory())) {
            if (event.isShiftClick()) event.setCancelled(true);
            return;
        }

        if (gui.onClick(player, event.getSlot(), event.getCursor()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        openGUIs.remove(player.getUniqueId());
    }
}
