package org.lacraft.util;

import static org.lacraft.util.api.MessageUtil.sendConsoleMessage;

import org.bukkit.plugin.java.JavaPlugin;
import org.flywaydb.core.Flyway;
import org.lacraft.util.api.MessageUtil;

public final class LaUtil extends JavaPlugin {

    @Override
    public void onLoad() {
        MessageUtil.sendConsoleMessage("&유틸 서버 onLoad");
        super.onLoad();
    }

    @Override
    public void onDisable() {
        MessageUtil.sendConsoleMessage("&유틸 서버 onDisable");
        super.onDisable();
    }

    @Override
    public void onEnable() {
        MessageUtil.sendConsoleMessage("&유틸 서버 onEnable");
        super.onEnable();
    }

}