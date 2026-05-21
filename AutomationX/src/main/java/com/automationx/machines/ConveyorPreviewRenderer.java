package com.automationx.machines;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class ConveyorPreviewRenderer {

    public static void drawLine(World world, Location start, Location end) {
        Vector dir = end.toVector().subtract(start.toVector());
        double length = dir.length();
        dir.normalize();

        for (double d = 0; d <= length; d += 0.25) {
            Location point = start.clone().add(dir.clone().multiply(d));
            world.spawnParticle(
                Particle.FIREWORK,
                point,
                1,
                0, 0, 0,
                0
            );
        }
    }
}
