package com.automationx;

import com.automationx.commands.AXCommand;
import com.automationx.items.ItemRegistry;
import com.automationx.listeners.*;
import com.automationx.machines.*;
import com.automationx.power.PowerManager;
import com.automationx.recipes.RecipeRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public class AutomationX extends JavaPlugin {

    private static AutomationX instance;
    private ItemRegistry itemRegistry;
    private RecipeRegistry recipeRegistry;
    private MachineRegistry machineRegistry;
    private PowerManager powerManager;

    private PlacementSessionManager placementSessionManager;
    private ConveyorItemManager conveyorItemManager;
    private ConveyorSpeedManager conveyorSpeedManager;
    private RPMNetworkManager rpmNetworkManager;
    private ShaftListener shaftListener;
    private SignGuiListener signGuiListener;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        itemRegistry    = new ItemRegistry(this);
        recipeRegistry  = new RecipeRegistry(this);
        machineRegistry = new MachineRegistry(this);
        powerManager    = new PowerManager(this);

        placementSessionManager = new PlacementSessionManager();
        conveyorSpeedManager    = new ConveyorSpeedManager();
        conveyorItemManager     = new ConveyorItemManager(this);
        rpmNetworkManager       = new RPMNetworkManager(this);

        getCommand("automationx").setExecutor(new AXCommand(this));

        GUIListener guiListener = new GUIListener(this);
        signGuiListener = new SignGuiListener(this);
        shaftListener   = new ShaftListener(this);

        getServer().getPluginManager().registerEvents(new LeashProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new WrenchListener(this, guiListener), this);
        getServer().getPluginManager().registerEvents(new AnvilGravityListener(this), this);
        getServer().getPluginManager().registerEvents(new ConveyorListener(this), this);
        getServer().getPluginManager().registerEvents(shaftListener, this);
        getServer().getPluginManager().registerEvents(guiListener, this);
        getServer().getPluginManager().registerEvents(signGuiListener, this);

        getServer().getScheduler().runTaskTimer(this,
                () -> placementSessionManager.tick(), 1L, 1L);

        getServer().getScheduler().runTaskTimer(this,
                () -> conveyorItemManager.tick(), 1L, 1L);

        getServer().getScheduler().runTaskTimer(this,
                () -> rpmNetworkManager.tick(), 1L, 1L);
    }

    @Override
    public void onDisable() {
        if (machineRegistry != null) machineRegistry.shutdown();
        if (powerManager != null) powerManager.shutdown();
    }

    public static AutomationX getInstance() { return instance; }
    public ItemRegistry getItemRegistry() { return itemRegistry; }
    public RecipeRegistry getRecipeRegistry() { return recipeRegistry; }
    public MachineRegistry getMachineRegistry() { return machineRegistry; }
    public PowerManager getPowerManager() { return powerManager; }

    public PlacementSessionManager getPlacementSessionManager() { return placementSessionManager; }
    public ConveyorItemManager getConveyorItemManager() { return conveyorItemManager; }
    public ConveyorSpeedManager getConveyorSpeedManager() { return conveyorSpeedManager; }
    public RPMNetworkManager getRpmNetworkManager() { return rpmNetworkManager; }
    public ShaftListener getShaftListener() { return shaftListener; }
    public SignGuiListener getSignGuiListener() { return signGuiListener; }
}
