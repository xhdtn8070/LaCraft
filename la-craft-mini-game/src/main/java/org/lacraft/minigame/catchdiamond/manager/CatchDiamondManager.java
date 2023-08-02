package org.lacraft.minigame.catchdiamond.manager;

import dev.lone.itemsadder.api.CustomStack;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.lacraft.minigame.LaMiniGame;
import org.lacraft.minigame.catchdiamond.domain.CatchDiamond;
import org.lacraft.util.api.ItemAdderUtil;
import org.lacraft.util.api.MessageUtil;

@Getter
public class CatchDiamondManager implements Listener, Runnable {

    @Getter
    private static final CatchDiamondManager instance = new CatchDiamondManager();

    @Getter
    @Setter
    private static boolean isNeedsIaZip;

    private final int GUI_SIZE = 54;
    private final int TIMER_SLOT = 45;

    private final int GAME_DURATION = 30; // 게임 시간 (초)
    private final int MAX_BLOCK_COUNT = 6; // 블럭 생성 개수
    private final int START_BLOCK_COUNT = 3; // 초기 타겟 블럭 개수

    private final int MAX_SCORE = 100; // 게임 최대 점수
    private final ItemStack TARGET_ITEM_DIA = new ItemStack(Material.DIAMOND, 1); // 클릭해야 하는 블럭 아이템

    private final ItemStack TARGET_ITEM_GOLD;
    //    private final ItemStack TARGET_ITEM_GOLD = new ItemStack(Material.GOLD_INGOT, 1); // 클릭해야 하는 블럭 아이템
    private final ItemStack TARGET_ITEM_COAL = new ItemStack(Material.COAL, 1); // 클릭해야 하는 블럭 아이템
    private final ItemStack BOMB_ITEM = new ItemStack(Material.TNT, 1); // GUI 테두리 아이템

    private final String GUI_TITLE = "<GREEN>Mini Game</GREEN>"; // GUI 타이틀
    private final String START_MESSAGE = "<YELLOW>Mini Game started!</YELLOW>"; // 게임 시작 메시지
    private final String END_SUCCESS_MESSAGE = "<GREEN>Congratulations! You win!</GREEN>"; // 게임 성공 메시지
    private final String END_FAIL_MESSAGE = "<RED>Game over! You lose!</RED>"; // 게임 실패 메시지

    private final HashMap<Player, CatchDiamond> games = new HashMap<>();

    private final HashMap<ItemStack, Integer> blockScore = new HashMap<>();

    private Integer taskId;

    //블록 생존 시간은 2초로 정함
    //다이아몬드는 2초 표기
    //다이아몬드 2초 지났을때 금으로 내려갈 확률 70% / 사라질 확률 30%
    //금 2초 지났을때 석탄으로 내려갈 확률 50% / 사라질 확률 50%
    //석탄 2초 지났을 땐 100% 삭제
    private CatchDiamondManager() {
        MessageUtil.sendConsoleMessage("<GREEN>CatchDiamondManager init...</GREEN>");
        isNeedsIaZip = ItemAdderUtil.extractDefaultAssets(LaMiniGame.getInstance());
        MessageUtil.sendConsoleMessage("<GREEN>CacheDiamondMange.isNeedsIaZip : " + isNeedsIaZip + "</GREEN>");

        CustomStack rubyCustomItem = CustomStack.getInstance("customblock:ruby");
        if (rubyCustomItem != null) {
            TARGET_ITEM_GOLD = rubyCustomItem.getItemStack();
        } else {
            TARGET_ITEM_GOLD = new ItemStack(Material.GOLD_INGOT);
        }
        this.blockScore.put(TARGET_ITEM_DIA, 10);
        this.blockScore.put(TARGET_ITEM_GOLD, 7);
        this.blockScore.put(TARGET_ITEM_COAL, 5);
        this.blockScore.put(BOMB_ITEM, -20);
        this.taskId = null;
    }

    @Override
    public void run() {
        if (games.isEmpty()) {
            if (Objects.nonNull(taskId)) {
                Bukkit.getScheduler().cancelTask(taskId);
                taskId = null;
            }
            return;
        }
        for (Player player : games.keySet()) {
            CatchDiamond catchDiamond = games.get(player);
            catchDiamond.progress();

            if (catchDiamond.getCountdown() <= 0) {
                // 게임 종료
                this.endGame(player);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (games.containsKey(player)) {
            player.openInventory(event.getInventory());
            MessageUtil.sendPlayerMessage(event.getPlayer(), "게임 중에는 창을 종료하실 수 없습니다.");
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (games.containsKey(player)) {
            CatchDiamond catchDiamond = games.get(player);
            Inventory clickedInv = event.getClickedInventory();
            Inventory guiInv = catchDiamond.getGui();
            if (clickedInv != null && clickedInv.equals(guiInv)) {
                event.setCancelled(true);
                ItemStack clickedItem = event.getCurrentItem();

                if (clickedItem != null && blockScore.containsKey(clickedItem)) {

                    Integer targetScore = blockScore.get(clickedItem);
                    catchDiamond.addScore(targetScore);

                    clickedInv.setItem(event.getSlot(), new ItemStack(Material.AIR));

//                    //맥스 스코어 달성 시 게임 종료 여부
//                    if (catchDiamond.getScore() >= MAX_SCORE) {
//                        // 게임 종료
//                        this.endGame(player);
//                    }

                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (games.containsKey(player)) {
            this.endGame(player);
        }
    }

    public void startGame(List<Player> players) {
        for (Player player : players) {
            if (games.containsKey(player)) {
                continue;
            }
            CatchDiamond catchDiamond = new CatchDiamond(GUI_TITLE, player);
            games.put(player, catchDiamond);

            player.openInventory(catchDiamond.getGui());
        }
        if (Objects.isNull(taskId)) {
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(LaMiniGame.getInstance(), this, 20L, 20L);
        }
    }

    public void endGame(Player player) {
        if (games.containsKey(player)) {
            player.closeInventory();
            CatchDiamond catchDiamond = games.get(player);

            //TODO 이벤트 발생
            //TODO 성공실패 분기
            //player.sendMessage(END_SUCCESS_MESSAGE); // in eventHandler

            catchDiamond.free();
            games.remove(player);
        }


    }

}
