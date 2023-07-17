package org.lacraft.virtualinventory;

import static org.lacraft.util.api.MessageUtil.sendConsoleMessage;

import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.lacraft.virtualinventory.command.ViCommand;
import org.lacraft.virtualinventory.config.MessageConfig;
import org.lacraft.virtualinventory.config.PluginConfig;
import org.lacraft.virtualinventory.config.TabConfiguration;
import org.lacraft.virtualinventory.manager.VirtualInventoryManager;
import org.lacraft.virtualinventory.tab.ViTab;

public final class LaVirtualInventory extends JavaPlugin {

    private static volatile LaVirtualInventory instance;
    public VirtualInventoryManager virtualInventoryManager;

    public ViCommand viCommand;
    public ViTab viTab;

    @Override
    public void onLoad() {
        sendConsoleMessage("&가상 인벤토리 서버 onLoad");
        super.onLoad();
    }

    @Override
    public void onEnable() {
        sendConsoleMessage("&가상 인벤토리 서버 onEnable");
        super.onEnable();
        instance = this;

        //TODO config 화
        //LaUtil.flywayMigrate("jdbc:mysql://localhost:3306/la_craft", "root", "1q2w3e4r", "flyway_virtual_schema_history");
        // Plugin startup logic

        PluginConfig.getInstance();
        MessageConfig.getInstance();
        TabConfiguration.getInstance();


        this.virtualInventoryManager = VirtualInventoryManager.getInstance();
        try{
            virtualInventoryManager.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        viCommand = ViCommand.getInstance();
        viTab = new ViTab(this);
        this.getCommand("vi").setExecutor(viCommand);
        this.getCommand("vi").setTabCompleter(viTab);
        repeatSaveVirtualInventory();

    }

    @Override
    public void onDisable() {
        sendConsoleMessage("&가상 인벤토리 서버 onDisable");
        super.onDisable();
        try {
            this.virtualInventoryManager.saveAll(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        instance = null;
    }

    private void repeatSaveVirtualInventory(){
        boolean autoSave = PluginConfig.getInstance().getAutoSave();
        int second = PluginConfig.getInstance().getAutoSaveTime();
        if(autoSave && second!=0){
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    try {
                        virtualInventoryManager.saveAll(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0L, second*20L); //0 Tick initial delay, 20 Tick (1 Second) between repeats

        }
    }


    public static LaVirtualInventory getInstance() {
        if (instance == null) {
            synchronized (LaVirtualInventory.class) {
                if (instance == null) {
                    instance = getPlugin(LaVirtualInventory.class);
                }
            }
        }
        return instance;
    }
}
