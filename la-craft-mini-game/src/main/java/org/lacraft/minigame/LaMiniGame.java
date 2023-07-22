package org.lacraft.minigame;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.lacraft.minigame.catchdiamond.command.CatchDiamondCommand;
import org.lacraft.minigame.catchdiamond.manager.CatchDiamondManager;
import org.lacraft.util.api.MessageUtil;

public final class LaMiniGame extends JavaPlugin{

    private static volatile LaMiniGame instance;



    @Override
    public void onLoad() {
        MessageUtil.sendConsoleMessage("&미니게임 서버 onLoad");

    }

    @Override
    public void onDisable() {
        MessageUtil.sendConsoleMessage("&미니게임 서버 onDisable");

        instance = null;
    }

    @Override
    public void onEnable() {
        MessageUtil.sendConsoleMessage("&미니게임 서버 onEnable");
        instance = this;


        CatchDiamondManager.getInstance();
        CatchDiamondCommand.getInstance();
    }
    public static LaMiniGame getInstance() {
        if (instance == null) {
            synchronized (LaMiniGame.class) {
                if (instance == null) {
                    instance = getPlugin(LaMiniGame.class);
                }
            }
        }
        return instance;
    }
}