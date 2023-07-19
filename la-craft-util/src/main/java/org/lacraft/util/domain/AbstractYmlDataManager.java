package org.lacraft.util.domain;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

// 추상 클래스로서, YML 데이터 관리의 기본적인 골격을 제공합니다.
public abstract class AbstractYmlDataManager {

    // 플러그인 인스턴스
    protected JavaPlugin plugin;

    // YML 파일 구성 정보
    @Getter
    protected YamlConfiguration dataConfig;

    // YML 파일 이름
    @Getter
    protected String fileName;

    // 생성자에서는 플러그인, 파일 이름을 입력 받아 초기화를 수행합니다.
    public AbstractYmlDataManager(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.dataConfig = null;
        this.loadConfig();  // 초기 설정 로드
    }

    // 설정 파일의 내용을 가져옵니다. 만약 아직 로드되지 않았다면, 재로드를 수행합니다.
    public FileConfiguration getConfig() {
        try {
            if (this.dataConfig == null) {
                this.reloadConfig();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.dataConfig;
    }

    // 초기 설정 파일을 로드하는 메서드입니다. 구체적인 내용은 하위 클래스에서 구현합니다.
    public abstract void loadConfig();

    // 설정 파일을 재로드하는 메서드입니다. 구체적인 내용은 하위 클래스에서 구현합니다.
    public abstract void reloadConfig();
}
