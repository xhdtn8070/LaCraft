package org.lacraft.util.domain;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.lacraft.util.api.FileUtil;

public class ResourceYmlDataManager extends AbstractYmlDataManager {

    // 생성자: 부모 클래스의 생성자를 호출하여 초기화
    public ResourceYmlDataManager(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    // config 파일을 로드하는 메서드
    // 플러그인의 리소스로부터 파일을 읽어와 YamlConfiguration으로 로드
    @Override
    public void loadConfig() {
        this.dataConfig = YamlConfiguration.loadConfiguration(
                FileUtil.getReaderFromStream(
                        this.plugin.getResource(fileName)
                )
        );
    }

    // config 파일을 다시 로드하는 메서드
    // 현재의 dataConfig를 null로 설정 후 loadConfig를 호출하여 새롭게 로드
    @Override
    public void reloadConfig() {
        this.dataConfig = null;
        this.loadConfig();
    }

}


