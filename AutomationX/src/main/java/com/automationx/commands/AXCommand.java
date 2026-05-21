package com.automationx.commands;

import com.automationx.AutomationX;
import com.automationx.items.CustomItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AXCommand implements CommandExecutor {

    private final AutomationX plugin;

    public AXCommand(AutomationX plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("§6AutomationX §7v" + plugin.getDescription().getVersion());
            sender.sendMessage("§e/ax version §7- Show plugin version");
            sender.sendMessage("§e/ax give <item_id> §7- Give yourself a custom item");
            sender.sendMessage("§e/ax list §7- List all custom item IDs");
            return true;
        }

        if (args[0].equalsIgnoreCase("version")) {
            sender.sendMessage("§6AutomationX §7v" + plugin.getDescription().getVersion());
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            sender.sendMessage("§6Custom Items:");
            for (String id : plugin.getItemRegistry().getAll().keySet()) {
                sender.sendMessage("§7- §e" + id);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage("§cUsage: /ax give <item_id>");
                return true;
            }
            String itemId = args[1].toLowerCase();
            CustomItem customItem = plugin.getItemRegistry().get(itemId);
            if (customItem == null) {
                sender.sendMessage("§cUnknown item: §e" + itemId);
                sender.sendMessage("§7Use §e/ax list §7to see all item IDs.");
                return true;
            }
            ItemStack stack = customItem.createItem();
            player.getInventory().addItem(stack);
            player.sendMessage("§aGave you: §e" + customItem.getDisplayName());
            return true;
        }

        sender.sendMessage("§k== §f§cUnknown command. Use §e/ax help §k==");
        return true;
    }
}
