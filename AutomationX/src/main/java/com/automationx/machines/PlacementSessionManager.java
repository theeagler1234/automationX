package com.automationx.machines;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlacementSessionManager {

    private final Map<Player, ConveyorPlacementSession> sessions = new HashMap<>();

    public void startSession(Player p, ConveyorPlacementSession session) {
        sessions.put(p, session);
    }

    public void endSession(Player p) {
        sessions.remove(p);
    }

    public ConveyorPlacementSession get(Player p) {
        return sessions.get(p);
    }

    public boolean has(Player p) {
        return sessions.containsKey(p);
    }

    // Called every tick by AutomationX
    public void tick() {
        for (ConveyorPlacementSession session : sessions.values()) {
            if (session.getCurrentTarget() != null) {
                ConveyorPreviewRenderer.drawLine(
                    session.getStart().getWorld(),
                    session.getStart(),
                    session.getCurrentTarget()
                );
            }
        }
    }
}
