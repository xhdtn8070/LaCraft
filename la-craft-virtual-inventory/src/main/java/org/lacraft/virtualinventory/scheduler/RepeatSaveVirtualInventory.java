package org.lacraft.virtualinventory.scheduler;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.lacraft.virtualinventory.LaVirtualInventory;
import org.lacraft.virtualinventory.config.PluginConfig;
import org.lacraft.virtualinventory.manager.VirtualInventoryManager;

import java.io.IOException;


public class RepeatSaveVirtualInventory {

    @Getter
    private static final RepeatSaveVirtualInventory instance = new RepeatSaveVirtualInventory();

    private RepeatSaveVirtualInventory() {
        repeatSaveVirtualInventory();
    }


    private void repeatSaveVirtualInventory(){
        boolean autoSave = PluginConfig.getInstance().getAutoSave();
        int second = PluginConfig.getInstance().getAutoSaveTime();
        if(autoSave && second!=0){
            Bukkit.getScheduler().scheduleSyncRepeatingTask(LaVirtualInventory.getInstance(), new Runnable() {
                @Override
                public void run() {
                    try {
                        VirtualInventoryManager.getInstance().saveAll(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0L, second*20L); //0 Tick initial delay, 20 Tick (1 Second) between repeats

        }
    }
}
