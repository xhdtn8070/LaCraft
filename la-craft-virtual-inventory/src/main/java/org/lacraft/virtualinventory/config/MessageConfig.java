package org.lacraft.virtualinventory.config;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.lacraft.util.api.MessageUtil;
import org.lacraft.util.domain.FileYmlDataManager;
import org.lacraft.virtualinventory.LaVirtualInventory;

@Getter
public class MessageConfig {

    @Getter
    private static final MessageConfig instance = new MessageConfig();

    private final String fileName = "message.yml";

    private final FileYmlDataManager ymlDataManager;

    private String init;

    private List<String> help;

    private List<String> info;

    private Inventory inventory;

    private Player player;

    private MessageConfig() {
        MessageUtil.sendConsoleMessage("PluginConfig 로딩중");
        ymlDataManager = new FileYmlDataManager(LaVirtualInventory.getInstance(), fileName);
        init();
    }

    public void init() {

        this.init = ymlDataManager.getConfig().getString("init", "&f[&dVI&f] &2Running &bVirtual Inventory {#version}.\n&9Use &a/vi help &9to view available commands.");

        this.help =  ymlDataManager.getConfig().getStringList("help");
        this.info =  ymlDataManager.getConfig().getStringList("info");

        this.inventory = Inventory
                .builder()
                .start(ymlDataManager.getConfig().getString("message.inventory.start", "&aVirtual Inventroy start and load..."))
                .stop(ymlDataManager.getConfig().getString("message.inventory.stop", "&aVirtual Inventroy stop and save..."))
                .title(ymlDataManager.getConfig().getString("message.inventory.title", "#{playerName} 님의 인벤토리 #{inventoryName}"))
                .alreadyExists(ymlDataManager.getConfig().getString("message.inventory.already-exists", "&c이미 같은 이름의 가상 인벤토리가 존재합니다."))
                .noExists(ymlDataManager.getConfig().getString("message.inventory.no-exists", "&c플레이어의 인벤토리 #{inventoryName}가 존재하지 않습니다."))
                .createError(ymlDataManager.getConfig().getString("message.inventory.create-error", "&c창고를 더 이상 생성할 수 없습니다."))
                .remove(ymlDataManager.getConfig().getString("message.inventory.remove", "&a인벤토리가 삭제되었습니다.")).build();


        this.player = Player
                .builder()
                .noExistsOnline(ymlDataManager.getConfig().getString("message.player.no-exists-online", "&c해당 플레이어가 온라인에 존재하지 않습니다."))
                .noExists(ymlDataManager.getConfig().getString("message.player.no-exists", "&c존재하지 않는 플레이어입니다."))
                .nonPlayer(ymlDataManager.getConfig().getString("message.player.non-player", "&c플레이어가 아닙니다. 플레이어로 입력해주시기 바랍니다."))
                .noAllowedPermission(ymlDataManager.getConfig().getString("message.player.no-allowed-permission", "&aYou do not have \"&e#{permission}&a\" permission to run that command"))
                .build();

    }

    @Getter
    @Builder
    public static class Inventory {

        private String start;
        private String stop;
        private String title;
        private String alreadyExists;
        private String noExists;
        private String createError;
        private String remove;

    }

    @Getter
    @Builder
    public static class Player {

        private String noExistsOnline;
        private String noExists;
        private String nonPlayer;
        private String noAllowedPermission;

    }
}
