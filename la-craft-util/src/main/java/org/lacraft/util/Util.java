package org.lacraft.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.flywaydb.core.Flyway;

public final class Util extends JavaPlugin {

    @Override
    public void onLoad() {
        sendConsoleMessage("&유틸 서버 onLoad");
        super.onLoad();
    }

    @Override
    public void onDisable() {
        sendConsoleMessage("&유틸 서버 onDisable");
        super.onDisable();
    }

    @Override
    public void onEnable() {
        sendConsoleMessage("&유틸 서버 onEnable");
        super.onEnable();
    }

    public static void sendConsoleMessage(String message) {
        Component component = MiniMessage.miniMessage().deserialize(message);
        Bukkit.getServer().sendMessage(component);
    }

    public static void flywayMigrate(String url, String user, String password, String table) {
        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .table(table)
                .load();

        // Start migration
        flyway.migrate();
    }
}