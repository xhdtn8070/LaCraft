package org.lacraft.virtualinventory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CharSequenceReader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.lacraft.virtualinventory.command.ViCommand;
import org.lacraft.virtualinventory.manager.VirtualInventoryManager;
import org.lacraft.virtualinventory.manager.YmlDataManager;
import org.lacraft.virtualinventory.tab.ViTab;
import org.lacraft.util.LaUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import net.md_5.bungee.api.chat.TextComponent;

import static org.lacraft.util.LaUtil.sendConsoleMessage;

public final class LaVirtualInventory extends JavaPlugin {

    public YmlDataManager messageConfig;
    public VirtualInventoryManager virtualInventoryManager;
    public FileConfiguration tabConfiguration;
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
        super.onLoad();

        //TODO config 화
        //LaUtil.flywayMigrate("jdbc:mysql://localhost:3306/la_craft", "root", "1q2w3e4r", "flyway_virtual_schema_history");
        // Plugin startup logic
        getConfig().options().configuration();
        saveDefaultConfig();

        messageConfig = new YmlDataManager(this,"message");
        try {
            tabConfiguration = YamlConfiguration.loadConfiguration(getReaderFromStream(this.getResource("tab.yml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.virtualInventoryManager = new VirtualInventoryManager(this,new File(getDataFolder(), "inventory"));
        try{
            virtualInventoryManager.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        viCommand = new ViCommand(this);
        viTab = new ViTab(this);
        this.getCommand("vi").setExecutor(viCommand);
        this.getCommand("vi").setTabCompleter(viTab);
        repeatSaveVirtualInventory();

    }

    @Override
    public void onDisable() {
        sendConsoleMessage("&가상 인벤토리 서버 onDisable");
        super.onLoad();

        try {
            this.virtualInventoryManager.saveAll(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void repeatSaveVirtualInventory() {
        int second = this.getConfig().getInt("auto-save");
        if (second != 0) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
                try {
                    virtualInventoryManager.saveAll(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, 0L, second * 20L); //0 Tick initial delay, 20 Tick (1 Second) between repeats
        }
    }

    public Reader getReaderFromStream(InputStream initialStream)
            throws IOException {

        byte[] buffer = IOUtils.toByteArray(initialStream);

        Reader targetReader = new CharSequenceReader(new String(buffer));
        return targetReader;
    }
}
