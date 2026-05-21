package com.automationx.machines;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class ConveyorSpeedManager {

    private final Map<Location, Integer> rpmMap = new HashMap<>();
    private static final int MAX_RPM = 256;

    public void setRPM(Location shaft, int rpm) {
        // Clamp RPM between 0 and MAX_RPM
        int clamped = Math.max(0, Math.min(MAX_RPM, rpm));
        rpmMap.put(shaft, clamped);
    }

    public int getRPM(Location shaft) {
        return rpmMap.getOrDefault(shaft, 0);
    }

    public double getBlocksPerTick(Location shaft) {
        int rpm = getRPM(shaft);

        // Convert RPM -> blocks per tick
        double blocksPerSecond = rpm / 60.0;
        return blocksPerSecond / 20.0;
    }
}
