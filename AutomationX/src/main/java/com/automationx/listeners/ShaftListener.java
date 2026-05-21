package com.automationx.listeners;

import com.automationx.AutomationX;
import com.automationx.items.CustomItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ShaftListener implements Listener {

    private final AutomationX plugin;
    private final Set<Block> shafts = new HashSet<>();

    public ShaftListener(AutomationX plugin) {
        this.plugin = plugin;
    }

    public Set<Block> getAllShafts() {
        return Collections.unmodifiableSet(shafts);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack held = player.getInventory().getItemInMainHand();
        String id = CustomItem.getItemId(held);

        if (!"wrench".equals(id)) return;

        if (block.getType() == Material.CHAIN) {
            shafts.add(block);
        }

        if (block.getType() == Material.BARREL) {
            event.setCancelled(true);
            plugin.getSignGuiListener().open(player, block,
                    SignGuiListener.Mode.GEARBOX_RATIO);
        } else if (block.getType() == Material.LECTERN) {
            event.setCancelled(true);
            plugin.getSignGuiListener().open(player, block,
                    SignGuiListener.Mode.RSC_RPM);
        }
    }
}
