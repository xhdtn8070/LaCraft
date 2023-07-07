package org.lacraft.virtualinventory.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.lacraft.virtualinventory.LaVirtualInventory;

import java.io.*;
import java.util.logging.Level;

/**
 * https://bukkit.gamepedia.com/Configuration_API_Reference#Arbitrary_Configurations
 * modify by tikim
 */
public class YmlDataManager {
    private LaVirtualInventory plugin;
    private FileConfiguration dataConfig;
    private File configFile;
    private String fileName;
    public YmlDataManager(LaVirtualInventory LaVirtualInventory, String fileName) {
        this.plugin = LaVirtualInventory;
        this.fileName = fileName+".yml";
        this.dataConfig = null;
        this.configFile = null;
        saveDefaultConfig();
    }
    public void reloadConfig() throws UnsupportedEncodingException {
        if(this.configFile == null){
            this.configFile = new File(this.plugin.getDataFolder(),fileName);
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        Reader defConfigStream = new InputStreamReader(this.plugin.getResource(fileName), "UTF8");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            dataConfig.setDefaults(defConfig);
        }
    }
    public FileConfiguration getConfig(){
        try{
            if(this.dataConfig == null){
                reloadConfig();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  this.dataConfig;
    }
    public void saveConfig(){
        if(this.dataConfig==null || this.configFile == null){
            return;
        }
        try {
            this.getConfig().save(configFile);
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + fileName, ex);
        }
    }
    public void saveDefaultConfig() {
        if (configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), fileName);
        }
        if (!configFile.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }
}
