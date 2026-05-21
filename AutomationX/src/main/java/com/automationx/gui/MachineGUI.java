package com.automationx.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class MachineGUI {

    protected final Inventory inventory;

    public MachineGUI(String title) {
        this.inventory = Bukkit.createInventory(null, 27, title);
    }

    public void open(Player player) {
        build();
        player.openInventory(inventory);
    }

    public abstract void build();
    public abstract boolean onClick(Player player, int slot, ItemStack cursor);

    protected void setStatItem(int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta  = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }

    protected void fillBorder(Material material) {
        int[] border = {0,1,2,3,4,5,6,7,8,9,17,18,19,20,21,22,23,24,25,26};
        ItemStack filler = new ItemStack(material);
        ItemMeta meta    = filler.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + " ");
        filler.setItemMeta(meta);
        for (int slot : border) inventory.setItem(slot, filler);
    }

    public Inventory getInventory() { return inventory; }
}
