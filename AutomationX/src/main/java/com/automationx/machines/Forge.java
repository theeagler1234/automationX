package com.automationx.machines;

import com.automationx.AutomationX;
import com.automationx.items.CustomItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Forge extends Machine {

    private final AutomationX plugin;
    private Inventory guiInventory = null;

    public int getTicksPerItem() {
        double multiplier = plugin.getConfig().getDouble("forge.speed-multiplier", 3.0);
        return Math.max(1, (int)(100 / multiplier));
    }

    public Forge(Location controllerLocation, AutomationX plugin) {
        super(controllerLocation, "forge");
        this.plugin = plugin;
        setTicksRemaining(getTicksPerItem());
    }

    @Override
    public boolean isStructureValid() {
        Block controller = getControllerBlock();
        if (controller.getType() != Material.IRON_BLOCK) return false;
        Block heatingChamber = controller.getRelative(BlockFace.DOWN);
        if (heatingChamber.getType() != Material.DROPPER) return false;
        if (!(heatingChamber.getBlockData() instanceof org.bukkit.block.data.type.Dispenser dispenser))
            return false;
        if (dispenser.getFacing() != BlockFace.UP) return false;
        Block fireLoc = heatingChamber.getRelative(BlockFace.DOWN);
        return fireLoc.getType() == Material.FIRE         ||
               fireLoc.getType() == Material.SOUL_FIRE    ||
               fireLoc.getType() == Material.CAMPFIRE     ||
               fireLoc.getType() == Material.SOUL_CAMPFIRE||
               heatingChamber.getRelative(BlockFace.DOWN, 2).getType() == Material.FIRE ||
               heatingChamber.getRelative(BlockFace.DOWN, 2).getType() == Material.SOUL_FIRE;
    }

    public void setGuiInventory(Inventory inv) { this.guiInventory = inv; }

    public static final int[] INPUT_SLOTS = {10, 11, 12, 13, 14, 15, 16};

    public ItemStack[] getInputContents() {
        if (guiInventory == null) return new ItemStack[INPUT_SLOTS.length];
        ItemStack[] contents = new ItemStack[INPUT_SLOTS.length];
        for (int i = 0; i < INPUT_SLOTS.length; i++) {
            contents[i] = guiInventory.getItem(INPUT_SLOTS[i]);
        }
        return contents;
    }

    @Override
    public boolean tick() {
        if (!isStructureValid()) return false;

        decrementTicks();
        if (getTicksRemaining() > 0) return false;

        ItemStack input = null;
        int inputSlot   = -1;

        if (guiInventory != null) {
            for (int i = 0; i < INPUT_SLOTS.length; i++) {
                ItemStack item = guiInventory.getItem(INPUT_SLOTS[i]);
                if (item != null && getSmeltResult(item) != null) {
                    input     = item;
                    inputSlot = i;
                    break;
                }
            }
        }

        if (input == null) { setTicksRemaining(getTicksPerItem()); return false; }

        ItemStack result = getSmeltResult(input);
        if (result == null) { setTicksRemaining(getTicksPerItem()); return false; }

        if (input.getAmount() > 1) {
            input.setAmount(input.getAmount() - 1);
            guiInventory.setItem(INPUT_SLOTS[inputSlot], input);
        } else {
            guiInventory.setItem(INPUT_SLOTS[inputSlot], null);
        }

        boolean placed = false;
        for (int slot : INPUT_SLOTS) {
            ItemStack existing = guiInventory.getItem(slot);
            if (existing == null) {
                guiInventory.setItem(slot, result);
                placed = true;
                break;
            } else if (existing.isSimilar(result) &&
                       existing.getAmount() < existing.getMaxStackSize()) {
                existing.setAmount(existing.getAmount() + 1);
                placed = true;
                break;
            }
        }
        if (!placed) {
            Location dropLoc = getControllerLocation().add(0.5, 1.5, 0.5);
            dropLoc.getWorld().dropItemNaturally(dropLoc, result);
        }

        recordProcessed(result.hasItemMeta() && result.getItemMeta().hasDisplayName()
            ? result.getItemMeta().getDisplayName()
            : result.getType().name());

        setTicksRemaining(getTicksPerItem());
        return true;
    }

    private ItemStack getSmeltResult(ItemStack input) {
        if (input == null) return null;
        String axId = CustomItem.getItemId(input);
        if (axId == null && input.getType() == Material.IRON_INGOT)
            return plugin.getItemRegistry().get("steel_ingot").createItem(1);
        return switch (input.getType()) {
            case IRON_ORE, DEEPSLATE_IRON_ORE, RAW_IRON        -> new ItemStack(Material.IRON_INGOT);
            case GOLD_ORE, DEEPSLATE_GOLD_ORE,
                 NETHER_GOLD_ORE, RAW_GOLD                     -> new ItemStack(Material.GOLD_INGOT);
            case COPPER_ORE, DEEPSLATE_COPPER_ORE, RAW_COPPER  -> new ItemStack(Material.COPPER_INGOT);
            case SAND                                           -> new ItemStack(Material.GLASS);
            case COBBLESTONE                                    -> new ItemStack(Material.STONE);
            case OAK_LOG, SPRUCE_LOG, BIRCH_LOG, JUNGLE_LOG,
                 ACACIA_LOG, DARK_OAK_LOG, MANGROVE_LOG,
                 CHERRY_LOG                                     -> new ItemStack(Material.CHARCOAL);
            case KELP                                           -> new ItemStack(Material.DRIED_KELP);
            case WET_SPONGE                                     -> new ItemStack(Material.SPONGE);
            default -> null;
        };
    }

    @Override
    public String[] getStats() {
        String[] last = getLastProcessed();
        return new String[]{
            "§6=== Forge ===",
            "§7Status: §e" + (getTicksRemaining() > 0 ? "§aProcessing" : "§7Idle"),
            "§7Speed: §e"  + plugin.getConfig().getDouble("forge.speed-multiplier", 3.0) + "x",
            "§7Last: §f"   + last[0]
        };
    }
}
