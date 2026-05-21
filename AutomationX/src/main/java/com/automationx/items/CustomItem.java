package com.automationx.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import com.automationx.AutomationX;

import java.util.ArrayList;
import java.util.List;

public class CustomItem {

    private final String id;
    private final String displayName;
    private final Material material;
    private final List<String> lore;

    public CustomItem(String id, String displayName, Material material, List<String> lore) {
        this.id = id;
        this.displayName = displayName;
        this.material = material;
        this.lore = lore;
    }

    public ItemStack createItem() {
        return createItem(1);
    }

    public ItemStack createItem(int amount) {
        ItemStack stack = new ItemStack(material, amount);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(ChatColor.RESET + displayName);

        List<String> coloredLore = new ArrayList<>();
        coloredLore.add(ChatColor.DARK_GRAY + "AutomationX");
        for (String line : lore) {
            coloredLore.add(ChatColor.GRAY + line);
        }
        meta.setLore(coloredLore);

        NamespacedKey key = new NamespacedKey(AutomationX.getInstance(), "ax_item_id");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, id);

        stack.setItemMeta(meta);
        return stack;
    }

    public boolean matches(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) return false;
        NamespacedKey key = new NamespacedKey(AutomationX.getInstance(), "ax_item_id");
        String tagValue = stack.getItemMeta()
            .getPersistentDataContainer()
            .get(key, PersistentDataType.STRING);
        return id.equals(tagValue);
    }

    public static String getItemId(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) return null;
        NamespacedKey key = new NamespacedKey(AutomationX.getInstance(), "ax_item_id");
        return stack.getItemMeta()
            .getPersistentDataContainer()
            .get(key, PersistentDataType.STRING);
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public Material getMaterial() { return material; }
}
