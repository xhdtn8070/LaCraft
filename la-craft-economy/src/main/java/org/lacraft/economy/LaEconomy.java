package org.lacraft.economy;

import org.bukkit.plugin.java.JavaPlugin;
import org.lacraft.util.LaUtil;

public class LaEconomy extends JavaPlugin {

    @Override
    public void onLoad() {
        LaUtil.sendConsoleMessage("&경제 서버 onLoad");
        super.onLoad();
    }

    @Override
    public void onDisable() {
        LaUtil.sendConsoleMessage("&경제 서버 onDisable");
        super.onDisable();
    }

    @Override
    public void onEnable() {
        LaUtil.sendConsoleMessage("&경제 서버 onEnable");
        super.onEnable();
    }
}