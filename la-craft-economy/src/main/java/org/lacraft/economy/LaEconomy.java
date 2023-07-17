package org.lacraft.economy;

import org.bukkit.plugin.java.JavaPlugin;
import org.lacraft.util.api.MessageUtil;

public class LaEconomy extends JavaPlugin {

    @Override
    public void onLoad() {
        MessageUtil.sendConsoleMessage("&경제 서버 onLoad");
        super.onLoad();
    }

    @Override
    public void onDisable() {
        MessageUtil.sendConsoleMessage("&경제 서버 onDisable");
        super.onDisable();
    }

    @Override
    public void onEnable() {
        MessageUtil.sendConsoleMessage("&경제 서버 onEnable");
        super.onEnable();
    }
}