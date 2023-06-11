package org.lacraft.lacraftvirtualinventory;

import org.bukkit.plugin.java.JavaPlugin;
import org.flywaydb.core.Flyway;

public final class LaCraftVirtualInventory extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            // Configure Flyway
            Flyway flyway = Flyway.configure()
                    .dataSource("jdbc:mysql://localhost:3306/la_craft", "root", "1q2w3e4r")
                    .table("flyway_virtual_schema_history")
                    .load();

            // Start migration
            flyway.migrate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
