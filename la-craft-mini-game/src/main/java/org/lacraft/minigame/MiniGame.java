package org.lacraft.minigame;

import org.bukkit.plugin.java.JavaPlugin;
import org.lacraft.util.Util;

public final class MiniGame extends JavaPlugin{

    @Override
    public void onLoad() {
        Util.sendConsoleMessage("&미니게임 서버 onLoad");
        super.onLoad();
    }

    @Override
    public void onDisable() {
        Util.sendConsoleMessage("&미니게임 서버 onDisable");
        super.onDisable();
    }

    @Override
    public void onEnable() {
        Util.sendConsoleMessage("&미니게임 서버 onEnable");
        super.onEnable();
    }
}