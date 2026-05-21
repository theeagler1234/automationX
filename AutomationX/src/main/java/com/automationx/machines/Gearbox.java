package com.automationx.machines;

import com.automationx.AutomationX;
import com.automationx.util.BlockDataUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Chain;

import java.util.EnumSet;
import java.util.Set;

public class Gearbox {

    private final AutomationX plugin;
    private final Block core;

    private static final String KEY_RATIO = "gearbox_ratio";

    public Gearbox(AutomationX plugin, Block core) {
        this.plugin = plugin;
        this.core = core;
    }

    public Block getCore() {
        return core;
    }

    public double getRatio() {
        double def = plugin.getConfig().getDouble("mechanical.gearbox.default-ratio", 1.0);
        NamespacedKey key = new NamespacedKey(plugin, KEY_RATIO);
        return BlockDataUtil.getDouble(core, key, def);
    }

    public void setRatio(double ratio) {
        double max = plugin.getConfig().getDouble("mechanical.max-ratio", 4.0);
        ratio = Math.max(0.25, Math.min(max, ratio));
        NamespacedKey key = new NamespacedKey(plugin, KEY_RATIO);
        BlockDataUtil.setDouble(core, key, ratio);
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
