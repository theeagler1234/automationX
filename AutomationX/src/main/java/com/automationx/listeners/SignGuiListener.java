package com.automationx.listeners;

import com.automationx.AutomationX;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignGuiListener implements Listener {

    public enum Mode { GEARBOX_RATIO, RSC_RPM }

    private static class Session {
        final Block block;
        final Mode mode;
        Session(Block block, Mode mode) { this.block = block; this.mode = mode; }
    }

    private final AutomationX plugin;
    private final Map<UUID, Session> sessions = new HashMap<>();

    public SignGuiListener(AutomationX plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, Block core, Mode mode) {
        sessions.put(player.getUniqueId(), new Session(core, mode));
        Block signBlock = core.getRelative(0, 1, 0);
        signBlock.setType(Material.OAK_SIGN);
        Sign sign = (Sign) signBlock.getState();
        sign.setLine(0, mode == Mode.GEARBOX_RATIO ? "Gearbox Ratio" : "Target RPM");
        sign.update(true, false);
        player.openSign(sign);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        Session session = sessions.remove(player.getUniqueId());
        if (session == null) return;

        String line = event.getLine(1);
        if (line == null) line = "";
        line = line.trim();

        Block signBlock = event.getBlock();
        Bukkit.getScheduler().runTask(plugin, () -> signBlock.setType(Material.AIR));

        int value;
        try {
            value = Integer.parseInt(line);
        } catch (NumberFormatException ex) {
            player.sendMessage("§cInvalid number.");
            return;
        }

        switch (session.mode) {
            case GEARBOX_RATIO ->
                    plugin.getRpmNetworkManager().applyGearboxConfig(player, session.block, value);
            case RSC_RPM ->
                    plugin.getRpmNetworkManager().applyRscConfig(player, session.block, value);
        }
    }
}
