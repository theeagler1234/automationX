package com.automationx.gui;

import com.automationx.machines.HydraulicPress;
import com.automationx.machines.Machine;
import com.automationx.machines.MechanicalPress;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PressGUI extends MachineGUI {

    private final Machine press;

    public PressGUI(Machine press) {
        super(ChatColor.DARK_GRAY + "" + ChatColor.BOLD
            + (press.getMachineType().equals("hydraulic_press")
                ? "Hydraulic Press" : "Mechanical Press"));
        this.press = press;
    }

    @Override
    public void build() {
        fillBorder(Material.GRAY_STAINED_GLASS_PANE);

        boolean isHydraulic = press.getMachineType().equals("hydraulic_press");
        setStatItem(4,
            isHydraulic ? Material.ANVIL : Material.PISTON,
            (isHydraulic ? ChatColor.AQUA : ChatColor.GREEN)
                + (isHydraulic ? "Hydraulic Press" : "Mechanical Press")
        );

        String[] last = press instanceof MechanicalPress mp
            ? mp.getLastProcessed()
            : ((HydraulicPress) press).getLastProcessed();

        boolean processing = press.getTicksRemaining() > 0;
        setStatItem(10,
            processing ? Material.LIME_DYE : Material.GRAY_DYE,
            processing ? ChatColor.GREEN + "Status: Processing" : ChatColor.GRAY + "Status: Idle",
            ChatColor.GRAY + "Ticks remaining: " + press.getTicksRemaining()
        );

        boolean hasPower = press instanceof MechanicalPress mp2
            ? mp2.hasPower() : ((HydraulicPress) press).hasPower();
        setStatItem(12,
            hasPower ? Material.GLOWSTONE_DUST : Material.GUNPOWDER,
            hasPower ? ChatColor.GREEN + "Power: Connected" : ChatColor.RED + "Power: No power",
            ChatColor.GRAY + "Type: " + (isHydraulic ? "Electrical" : "Rotational")
        );

        setStatItem(14, Material.CLOCK,
            ChatColor.YELLOW + "Speed",
            ChatColor.GRAY + "Ticks remaining: " + press.getTicksRemaining(),
            isHydraulic ? ChatColor.GRAY + "Min: 3s guaranteed" : ""
        );

        setStatItem(19, Material.BOOK, ChatColor.GOLD + "Last Processed #1",
            ChatColor.GRAY + last[0]);
        setStatItem(21, Material.BOOK, ChatColor.GOLD + "Last Processed #2",
            ChatColor.GRAY + last[1]);
        setStatItem(23, Material.BOOK, ChatColor.GOLD + "Last Processed #3",
            ChatColor.GRAY + last[2]);
    }

    @Override
    public boolean onClick(Player player, int slot, ItemStack cursor) {
        return true; // stats only, cancel all clicks
    }
}
