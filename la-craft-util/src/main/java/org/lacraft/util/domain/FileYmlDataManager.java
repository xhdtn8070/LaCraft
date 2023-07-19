package org.lacraft.util.domain;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.lacraft.util.api.FileUtil;

// Yml 데이터를 관리하는 클래스
public class FileYmlDataManager extends AbstractYmlDataManager {

    @Getter
    private File configFile;

    // 생성자. plugin과 fileName을 받습니다. 이후, 부모 클래스의 생성자를 호출하고 configFile 변수를 null로 초기화합니다.
    public FileYmlDataManager(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
        this.configFile = null;
    }

    // 설정 파일을 다시 로드합니다. 설정 파일과 설정을 모두 null로 설정하고, loadConfig 메서드를 호출합니다.
    @Override
    public void reloadConfig() {
        this.configFile = null;
        this.dataConfig = null;
        loadConfig();
    }

    // 설정 파일을 로드합니다. 플러그인의 데이터 폴더에서 파일을 찾고, 해당 파일이 없으면 리소스에서 해당 파일을 저장합니다.
    // 그리고 파일을 읽어 설정을 로드합니다.
    @Override
    public void loadConfig() {
        this.configFile = new File(this.plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            this.plugin.saveResource(fileName, false);
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(configFile);
    }

    // 설정을 저장합니다. 설정과 설정 파일이 모두 null이 아닐 때에만 저장을 진행합니다.
    public void save() {
        if (this.dataConfig == null || this.configFile == null) {
            return;
        }
        try {
            this.dataConfig.save(configFile);
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + fileName, ex);
        }
    }
}


