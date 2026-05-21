package com.automationx.listeners;

import com.automationx.AutomationX;
import com.automationx.items.CustomItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.inventory.ItemStack;

public class LeashProtectionListener implements Listener {

    private final AutomationX plugin;

    public LeashProtectionListener(AutomationX plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeash(PlayerLeashEntityEvent event) {
        ItemStack held = event.getPlayer().getInventory().getItemInMainHand();

        String itemId = CustomItem.getItemId(held);
        if ("copper_wire".equals(itemId) || "power_cable".equals(itemId)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cThat's not a leash!");
        }
    }
}
