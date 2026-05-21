package com.automationx.machines;

import com.automationx.AutomationX;
import com.automationx.util.BlockDataUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Chain;

import java.util.EnumSet;
import java.util.Set;

public class RotationalSpeedController {

    private final AutomationX plugin;
    private final Block core;

    private static final String KEY_RPM = "rsc_rpm";

    public RotationalSpeedController(AutomationX plugin, Block core) {
        this.plugin = plugin;
        this.core = core;
    }

    public Block getCore() {
        return core;
    }

    public int getTargetRPM() {
        int def = plugin.getConfig().getInt("mechanical.rsc.default-rpm", 64);
        NamespacedKey key = new NamespacedKey(plugin, KEY_RPM);
        return BlockDataUtil.getInt(core, key, def);
    }

    public void setTargetRPM(int rpm) {
        int min = plugin.getConfig().getInt("mechanical.rsc.min-rpm", 16);
        int max = plugin.getConfig().getInt("mechanical.max-rpm", 256);
        rpm = Math.max(min, Math.min(max, rpm));
        NamespacedKey key = new NamespacedKey(plugin, KEY_RPM);
        BlockDataUtil.setInt(core, key, rpm);
    }

    public Set<BlockFace> getConnectedFaces() {
        Set<BlockFace> faces = EnumSet.noneOf(BlockFace.class);
        for (BlockFace face : BlockFace.values()) {
            if (!face.isCartesian()) continue;
            Block b = core.getRelative(face);
            if (b.getBlockData() instanceof Chain) faces.add(face);
        }
        return faces;
    }
}
