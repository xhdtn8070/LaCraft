package org.lacraft.minigame.catchdiamond.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.lacraft.minigame.catchdiamond.manager.CatchDiamondManager;

@Getter
@Setter
public class CatchDiamond {
    private static final Random random = new Random();

    private Player player;
    //획득 점수
    private Integer score;

    //남은 시간
    private Integer countdown;

    //인벤토리
    private Inventory gui;

    private Integer brokenBlockCount;

    private Integer noReactionTime;

    private Scoreboard originalScoreBoard;

    private Score scoreBoardScore;

    //타겟 슬롯 위치<위치, 지속 시간>
    private Map<Integer, Integer> targetLocation;

    //폭탄 슬롯 위치<위치, 지속 시간>
    private Map<Integer, Integer> bombLocation;

    public CatchDiamond(String guiTitle, Player player) {
        this.player = player;
        this.score = 0;
        this.noReactionTime = 0;
        this.countdown = CatchDiamondManager.getInstance().gameDuration;
        this.brokenBlockCount = 0;
        this.targetLocation = new HashMap<>();
        this.bombLocation = new HashMap<>();

        this.initGui(guiTitle);
        this.createScoreBoard();
    }

    public void addScore(Integer targetScore) {
        this.score += targetScore;
        this.brokenBlockCount++;
        this.updateScoreBoard();
    }

    public void free() {
        score = null;
        countdown = null;
        gui = null;
        targetLocation = null;
        bombLocation = null;
        this.removeScoreBoard();
    }

    private void initGui(String guiTitle) {
        // 랜덤 좌표 뽑기
        this.gui = Bukkit.createInventory(player, CatchDiamondManager.getInstance().guiSize, MiniMessage.miniMessage().deserialize(guiTitle));
        gui.setItem(CatchDiamondManager.getInstance().timerSlot, new ItemStack(Material.COMPASS, countdown));

        //초기 블록 추가
        for (int i = 0; i < CatchDiamondManager.getInstance().startBlockCount; i++) {
            Integer slot = getEmptyRandomSlot();
            this.generateItemOnGui(slot, 0, CatchDiamondManager.getInstance().catchDiamondBlocks, targetLocation);
        }
    }

    private void generateItemOnGui(int slot, int index, List<CatchDiamondBlock> blocks, Map<Integer, Integer> location) {
        location.put(slot, blocks.get(index).getDuration());
        gui.setItem(slot, blocks.get(index).getBlock());
    }

    public void progress() {
        this.countdown--;
        this.removeTarget();
        this.removeBomb();
        this.addTarget();
        this.addBomb();
        gui.setItem(CatchDiamondManager.getInstance().timerSlot, new ItemStack(Material.COMPASS, countdown));
    }

    public void addTarget() {
        int blockCount = random.nextInt(CatchDiamondManager.getInstance().maxBlockCount - targetLocation.size() + 1);
        for (int i = 0; i < blockCount; i++) {
            int slot = getEmptyRandomSlot();
            this.generateItemOnGui(slot, random.nextInt(CatchDiamondManager.getInstance().catchDiamondBlocks.size()), CatchDiamondManager.getInstance().catchDiamondBlocks, targetLocation);
        }
    }


    public void removeTarget() {
        this.removeLocation(targetLocation);
    }

    public void addBomb() {
        Random random = new Random();
        int blockCount = random.nextInt(CatchDiamondManager.getInstance().maxBombCount - bombLocation.size() + 1);
        for (int i = 0; i < blockCount; i++) {
            int slot = getEmptyRandomSlot();
            this.generateItemOnGui(slot, random.nextInt(CatchDiamondManager.getInstance().bombBlocks.size()), CatchDiamondManager.getInstance().bombBlocks, bombLocation);
        }
    }


    public void removeBomb() {
        this.removeLocation(bombLocation);
    }

    private void removeLocation(Map<Integer, Integer> location) {
        Iterator<Entry<Integer, Integer>> iterator = location.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Integer, Integer> entry = iterator.next();
            int duration = entry.getValue();
            duration--;

            if (duration <= 0) {
                gui.setItem(entry.getKey(), new ItemStack(Material.AIR));
                iterator.remove();
            } else {
                entry.setValue(duration);
            }
        }
    }

    private Integer getEmptyRandomSlot() {
        while (true) {
            int slot = random.nextInt(CatchDiamondManager.getInstance().guiSize);
            if (targetLocation.containsKey(slot)) {
                continue;
            }
            if (bombLocation.containsKey(slot)) {
                continue;
            }
            if (Objects.equals(CatchDiamondManager.getInstance().timerSlot, slot)) {
                continue;
            }
            return slot;
        }
    }


    private void createScoreBoard() {
        this.originalScoreBoard = player.getScoreboard();

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("CatchDiamond", Criteria.DUMMY, MiniMessage.miniMessage().deserialize("<YELLOW>Catch Diamond</YELLOW>"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score objectiveScore = objective.getScore("점수");
        objectiveScore.setScore(this.score);
        this.scoreBoardScore = objectiveScore;
        player.setScoreboard(scoreboard);

    }

    private void updateScoreBoard() {
        this.scoreBoardScore.setScore(this.score);
    }

    private void removeScoreBoard() {
        player.setScoreboard(originalScoreBoard);
        this.scoreBoardScore = null;
    }
}
