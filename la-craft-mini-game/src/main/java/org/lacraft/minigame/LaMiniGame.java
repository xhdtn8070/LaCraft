package org.lacraft.minigame;

import org.bukkit.plugin.java.JavaPlugin;
import org.lacraft.util.LaUtil;

public final class LaMiniGame extends JavaPlugin{

    @Override
    public void onLoad() {
        LaUtil.sendConsoleMessage("&미니게임 서버 onLoad");
        super.onLoad();
    }

    @Override
    public void onDisable() {
        LaUtil.sendConsoleMessage("&미니게임 서버 onDisable");
        super.onDisable();
    }

    @Override
    public void onEnable() {
        LaUtil.sendConsoleMessage("&미니게임 서버 onEnable");
        super.onEnable();
    }
}