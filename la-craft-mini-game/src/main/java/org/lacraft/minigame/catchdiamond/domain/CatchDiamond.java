package org.lacraft.minigame.catchdiamond.domain;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class CatchDiamond {

    //획득 점수
    private Integer score;

    //남은 시간
    private Integer countdown;

    //인벤토리
    private Inventory gui;

    private Integer blockCount;

    private Integer noReactionTime;

    private int GUI_SIZE = 54;
    private int TIMER_SLOT = 45;

    private int GAME_DURATION = 30; // 게임 시간 (초)
    private int MAX_BLOCK_COUNT = 6; // 블럭 생성 개수
    private int START_BLOCK_COUNT = 3; // 초기 타겟 블럭 개수

    //타겟 슬롯 위치<위치, 지속 시간>
    private Map<Integer, Integer> targetLocation;

    //폭탄 슬롯 위치<위치, 지속 시간>
    private Map<Integer, Integer> bombLocation;

    public CatchDiamond(String guiTitle,Player player) {
        this.score = 0;
        this.noReactionTime = 0;
        this.countdown = GAME_DURATION;
        this.blockCount = 0;
        this.targetLocation = new HashMap<>();
        this.bombLocation = new HashMap<>();

        this.initGui(guiTitle, player);
    }

    public void addScore(Integer targetScore) {
        this.score += targetScore;
    }

    public void free() {
        score = null;
        countdown = null;
        gui = null;
        targetLocation = null;
        bombLocation = null;
    }

    private void initGui(String guiTitle, Player player) {
        // 랜덤 좌표 뽑기
        this.gui = Bukkit.createInventory(player, GUI_SIZE, guiTitle);
        gui.setItem(TIMER_SLOT, new ItemStack(Material.COMPASS, countdown));

        //초기 블록 추가
        for (int i = 0; i < START_BLOCK_COUNT; i++) {
            int slot = (int) (Math.random() * (GUI_SIZE - 10)) + 1;
            if(targetLocation.containsKey(slot)){
                i--;
                continue;
            }
            targetLocation.put(slot, 0);
            ItemStack item = new ItemStack(Material.DIAMOND, 1);
            gui.setItem(slot, item);
        }

    }

    public void progress() {
        countdown--;
        //countdown --
        //noReactionTime ++
        //블록 추가 로직 및 변경 시키기
        //기존 폭탄 생존 시간 감소
        //랜덤하게 블록 추가
        //랜덤하게 폭탄 추가
        gui.setItem(TIMER_SLOT, new ItemStack(Material.COMPASS, countdown));
    }
}

