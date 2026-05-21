package com.automationx.util;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BlockDataUtil {

    public static void setDouble(Block block, NamespacedKey key, double value) {
        if (!(block.getState() instanceof TileState state)) return;
        state.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
        state.update(true, false);
    }

    public static double getDouble(Block block, NamespacedKey key, double def) {
        if (!(block.getState() instanceof TileState state)) return def;
        return state.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, def);
    }

    public static void setInt(Block block, NamespacedKey key, int value) {
        if (!(block.getState() instanceof TileState state)) return;
        state.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
        state.update(true, false);
    }

    public static int getInt(Block block, NamespacedKey key, int def) {
        if (!(block.getState() instanceof TileState state)) return def;
        return state.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, def);
    }
}
