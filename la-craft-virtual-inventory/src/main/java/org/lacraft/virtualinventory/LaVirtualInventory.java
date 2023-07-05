package org.lacraft.virtualinventory;

import org.bukkit.plugin.java.JavaPlugin;
import org.lacraft.util.LaUtil;

public final class LaVirtualInventory extends JavaPlugin {

    @Override
    public void onEnable() {
        //TODO config 화
        LaUtil.flywayMigrate("jdbc:mysql://localhost:3306/la_craft", "root", "1q2w3e4r", "flyway_virtual_schema_history");
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
