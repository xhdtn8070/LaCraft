package org.lacraft.virtualinventory.config;

import lombok.Getter;
import org.lacraft.util.api.ColorUtil;
import org.lacraft.util.api.ColorUtil.Color;
import org.lacraft.util.api.MessageUtil;
import org.lacraft.util.domain.FileYmlDataManager;
import org.lacraft.virtualinventory.LaVirtualInventory;

@Getter
public class PluginConfig {

    @Getter
    private static final PluginConfig instance = new PluginConfig();

    private final String fileName = "config.yml";

    private final FileYmlDataManager ymlDataManager;

    private Boolean autoSave;

    private Integer autoSaveTime;

    private Boolean saveMessaging;

    private String saveMessage;

    private Boolean loadMessaging;

    private String loadMessage;

    private Integer saveOncePerSeveralTimes;

    private Integer maxInventory;

    private PluginConfig() {
        MessageUtil.sendConsoleMessage("PluginConfig 로딩중");
        ymlDataManager = new FileYmlDataManager(LaVirtualInventory.getInstance(), fileName);
        init();
    }

    public void init() {
        this.autoSave = ymlDataManager.getConfig().getBoolean("auto-save", true);
        this.autoSaveTime = ymlDataManager.getConfig().getInt("auto-save-time", 10);

        this.saveMessaging = ymlDataManager.getConfig().getBoolean("save-messaging", true);
        this.saveMessage = ymlDataManager.getConfig().getString("save-message", ColorUtil.colorText("[virtual Inventory] 가상 창고가 저장되었습니다", Color.GREEN));

        this.loadMessaging = ymlDataManager.getConfig().getBoolean("load-messaging", true);
        this.loadMessage = ymlDataManager.getConfig().getString("load-message", ColorUtil.colorText("\t파일 명 : #{fileName}", Color.GOLD));

        this.saveOncePerSeveralTimes = ymlDataManager.getConfig().getInt("save-once-per-several-times", 3);
        this.maxInventory = ymlDataManager.getConfig().getInt("max-inventory", 10);
    }

}
