package org.lacraft.minigame;

import org.bukkit.plugin.java.JavaPlugin;
import org.lacraft.util.api.MessageUtil;

public final class LaMiniGame extends JavaPlugin{

    @Override
    public void onLoad() {
        MessageUtil.sendConsoleMessage("&미니게임 서버 onLoad");
        super.onLoad();
    }

    @Override
    public void onDisable() {
        MessageUtil.sendConsoleMessage("&미니게임 서버 onDisable");
        super.onDisable();
    }

    @Override
    public void onEnable() {
        MessageUtil.sendConsoleMessage("&미니게임 서버 onEnable");
        super.onEnable();
    }
}