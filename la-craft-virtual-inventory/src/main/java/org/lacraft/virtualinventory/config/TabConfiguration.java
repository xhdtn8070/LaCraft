package org.lacraft.virtualinventory.config;

import java.util.List;
import lombok.Getter;
import org.lacraft.util.api.MessageUtil;
import org.lacraft.util.domain.ResourceYmlDataManager;
import org.lacraft.virtualinventory.LaVirtualInventory;

@Getter
public class TabConfiguration {

    @Getter
    private static final TabConfiguration instance = new TabConfiguration();

    private final String fileName = "tab.yml";
    private final ResourceYmlDataManager ymlDataManager;

    private List<String> initTabs;

    private List<String> testTabs;

    private TabConfiguration() {
        MessageUtil.sendConsoleMessage("tabConfig 로딩중");
        ymlDataManager = new ResourceYmlDataManager(LaVirtualInventory.getInstance(), fileName);
        init();
        test();
    }

    public void init() {
        initTabs = ymlDataManager.getConfig().getStringList("init");
    }

    public void test() {
        testTabs = ymlDataManager.getConfig().getStringList("test");
    }
}
