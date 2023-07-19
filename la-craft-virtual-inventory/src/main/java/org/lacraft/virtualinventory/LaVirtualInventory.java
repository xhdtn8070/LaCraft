package org.lacraft.virtualinventory;

import static org.lacraft.util.api.MessageUtil.sendConsoleMessage;

import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.lacraft.virtualinventory.command.ViCommand;
import org.lacraft.virtualinventory.config.MessageConfig;
import org.lacraft.virtualinventory.config.PluginConfig;
import org.lacraft.virtualinventory.config.TabConfiguration;
import org.lacraft.virtualinventory.manager.VirtualInventoryManager;
import org.lacraft.virtualinventory.scheduler.RepeatSaveVirtualInventory;
import org.lacraft.virtualinventory.tab.ViTab;

public final class LaVirtualInventory extends JavaPlugin {

    private static volatile LaVirtualInventory instance;

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

        /**
         * 설정파일 로드
         */
        PluginConfig.getInstance();
        MessageConfig.getInstance();
        TabConfiguration.getInstance();

        /**
         * 인벤토리 매니저 로드
         */
        VirtualInventoryManager.getInstance();

        /**
         * 커맨드 관련 인스턴스 로드
         */
        ViCommand.getInstance();
        ViTab.getInstance();

        /**
         * 자동 세이브 실행
         */
        RepeatSaveVirtualInventory.getInstance();

    }

    @Override
    public void onDisable() {
        sendConsoleMessage("&가상 인벤토리 서버 onDisable");
        super.onDisable();
        try {
            VirtualInventoryManager.getInstance().saveAll(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        instance = null;
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
