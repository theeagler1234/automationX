package com.automationx.gui;

import com.automationx.machines.Forge;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ForgeGUI extends MachineGUI {

    private final Forge forge;
    public static final int[] INPUT_SLOTS = {10, 11, 12, 13, 14, 15, 16};

    public ForgeGUI(Forge forge) {
        super(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Forge");
        this.forge = forge;
    }

    @Override
    public void build() {
        // Top and bottom border
        for (int i = 0; i < 9; i++)
            setStatItem(i,  Material.ORANGE_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + " ");
        for (int i = 18; i < 27; i++)
            setStatItem(i,  Material.ORANGE_STAINED_GLASS_PANE, ChatColor.DARK_GRAY + " ");

        setStatItem(9, Material.FURNACE,
            ChatColor.GOLD + "Heating Chamber",
            ChatColor.GRAY + "Place smeltable items in the slots →"
        );

        boolean processing = forge.getTicksRemaining() > 0;
        setStatItem(17,
            processing ? Material.LIME_DYE : Material.GRAY_DYE,
            processing ? ChatColor.GREEN + "Status: Processing" : ChatColor.GRAY + "Status: Idle",
            ChatColor.GRAY + "Ticks remaining: " + forge.getTicksRemaining()
        );

        setStatItem(18, Material.CLOCK,
            ChatColor.YELLOW + "Speed",
            ChatColor.GRAY + "~" + (forge.getTicksPerItem() / 20.0) + "s per item"
        );

        String[] last = forge.getLastProcessed();
        setStatItem(19, Material.PAPER, ChatColor.GOLD + "Last: "  + last[0]);
        setStatItem(20, Material.PAPER, ChatColor.GOLD + "Prev: "  + last[1]);

        // Restore existing items in forge slots
        ItemStack[] contents = forge.getInputContents();
        for (int i = 0; i < INPUT_SLOTS.length && i < contents.length; i++) {
            inventory.setItem(INPUT_SLOTS[i], contents[i]);
        }
    }

    @Override
    public boolean onClick(Player player, int slot, ItemStack cursor) {
        for (int inputSlot : INPUT_SLOTS) {
            if (slot == inputSlot) return false; // allow
        }
        return true; // cancel everything else
    }
}
