package com.automationx.machines;

import com.automationx.AutomationX;
import com.automationx.items.CustomItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class MechanicalPress extends Machine {

    private final AutomationX plugin;
    private boolean hasPower = false;

    public MechanicalPress(Location controllerLocation, AutomationX plugin) {
        super(controllerLocation, "mechanical_press");
        this.plugin = plugin;
        int baseTicks = plugin.getConfig().getInt("press.base-ticks", 200);
        setTicksRemaining(baseTicks);
    }

    @Override
    public boolean isStructureValid() {
        Block controller = getControllerBlock();
        if (controller.getType() != Material.PISTON &&
            controller.getType() != Material.STICKY_PISTON) return false;
        Block ironBlock = controller.getRelative(BlockFace.DOWN);
        if (ironBlock.getType() != Material.IRON_BLOCK) return false;
        Block north = ironBlock.getRelative(BlockFace.NORTH);
        Block south = ironBlock.getRelative(BlockFace.SOUTH);
        Block east  = ironBlock.getRelative(BlockFace.EAST);
        Block west  = ironBlock.getRelative(BlockFace.WEST);
        boolean nsLevers = north.getType() == Material.LEVER && south.getType() == Material.LEVER;
        boolean ewLevers = east.getType()  == Material.LEVER && west.getType()  == Material.LEVER;
        return nsLevers || ewLevers;
    }

    @Override
    public boolean tick() {
        if (!isStructureValid()) return false;

        decrementTicks();
        if (getTicksRemaining() > 0) return false;

        Block ironBlock   = getControllerBlock().getRelative(BlockFace.DOWN);
        Block inputBlock  = ironBlock.getRelative(BlockFace.DOWN);
        Location inputLoc = inputBlock.getLocation().add(0.5, 0.5, 0.5);

        int baseTicks = plugin.getConfig().getInt("press.base-ticks", 200);

        // ============================================================
        // === NEW: Conveyor item processing ===========================
        // ============================================================
        ConveyorItemManager cim = plugin.getConveyorItemManager();

        if (cim.hasItem(inputBlock.getLocation())) {
            ItemStack input = cim.getItem(inputBlock.getLocation());
            ItemStack result = getPressResult(input);

            if (result == null) {
                setTicksRemaining(baseTicks);
                return false;
            }

            Block outputBlock = inputBlock.getRelative(BlockFace.DOWN);

            // If output is a conveyor
            if (outputBlock.getType() == Material.POLISHED_DEEPSLATE_SLAB) {

                // If output conveyor is full → stall
                if (cim.hasItem(outputBlock.getLocation())) {
                    setTicksRemaining(1);
                    return false;
                }

                // Consume input
                cim.setItem(inputBlock.getLocation(), null);

                // Output result
                cim.setItem(outputBlock.getLocation(), result);

                recordProcessed(result.getType().name());
                setTicksRemaining(baseTicks);
                return true;
            }
        }

        // ============================================================
        // === ORIGINAL DROPPED-ITEM LOGIC (unchanged) ================
        // ============================================================

        Collection<Item> items = inputLoc.getWorld().getNearbyEntitiesByType(
            Item.class, inputLoc, 0.6
        );

        if (items.isEmpty()) {
            setTicksRemaining(baseTicks);
            return false;
        }

        Item inputEntity = items.iterator().next();
        ItemStack inputStack = inputEntity.getItemStack();
        ItemStack result = getPressResult(inputStack);

        if (result == null) {
            setTicksRemaining(baseTicks);
            return false;
        }

        if (inputStack.getAmount() > 1) {
            inputStack.setAmount(inputStack.getAmount() - 1);
            inputEntity.setItemStack(inputStack);
        } else {
            inputEntity.remove();
        }

        Block outputBlock = inputBlock.getRelative(BlockFace.DOWN);
        if (outputBlock.getState() instanceof Chest chest) {
            Inventory inv = chest.getInventory();
            if (inv.firstEmpty() != -1) {
                inv.addItem(result);
            } else {
                inputLoc.getWorld().dropItemNaturally(inputLoc, result);
            }
        } else {
            inputLoc.getWorld().dropItemNaturally(inputLoc, result);
        }

        recordProcessed(result.getItemMeta().getDisplayName());
        setTicksRemaining(baseTicks);
        return true;
    }

    private ItemStack getPressResult(ItemStack input) {
        if (input == null) return null;

        // Conveyor recipe (rubber -> conveyor)
        if (input.getType() == Material.SLIME_BALL) {
            return new ItemStack(Material.POLISHED_DEEPSLATE_SLAB, 1);
        }

        String axId = CustomItem.getItemId(input);
        if (axId != null) return null;

        return switch (input.getType()) {
            case IRON_INGOT   -> plugin.getItemRegistry().get("iron_plate").createItem(1);
            case COPPER_INGOT -> plugin.getItemRegistry().get("copper_plate").createItem(1);
            default           -> null;
        };
    }

    @Override
    public String[] getStats() {
        String[] last = getLastProcessed();
        int baseTicks = plugin.getConfig().getInt("press.base-ticks", 200);
        return new String[]{
            "§6=== Mechanical Press ===",
            "§7Status: "   + (getTicksRemaining() > 0 ? "§aProcessing" : "§7Idle"),
            "§7Power: "    + (hasPower ? "§a✔ Connected" : "§c✘ No power (coming soon)"),
            "§7Base speed: §e" + (baseTicks / 20) + "s per item",
            "§7Power type: §eRotational (mechanical)",
            "§7Last processed:",
            "§8  1. §f" + last[0],
            "§8  2. §f" + last[1],
            "§8  3. §f" + last[2]
        };
    }

    public void setPower(boolean powered) { this.hasPower = powered; }
    public boolean hasPower() { return hasPower; }
}
