package org.lacraft.minigame.catchdiamond.manager;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.lacraft.minigame.catchdiamond.domain.CatchDiamondBlock;
import org.lacraft.minigame.catchdiamond.event.CatchDiamondEndEvent;
import org.lacraft.util.api.ColorUtil;
import org.lacraft.util.api.ColorUtil.Color;
import org.lacraft.util.api.ItemsAdderUtil;
import org.lacraft.util.api.MessageUtil;


@Getter
public class CatchDiamondManager implements Listener, Runnable {

    @Getter
    private static final CatchDiamondManager instance = new CatchDiamondManager();

    @Setter
    private boolean isNeedsIaZip;

    private final String guiTitle; // GUI 타이틀
    private final String startMessage; // 게임 시작 메시지
    private final String endSuccessMessage; // 게임 성공 메시지
    private final String endFailMessage; // 게임 실패 메시지

    public final List<CatchDiamondBlock> catchDiamondBlocks;

    public final List<CatchDiamondBlock> bombBlocks;

    public final Map<ItemStack, CatchDiamondBlock> blockItemStacks;

    private final HashMap<Player, CatchDiamond> games;

    public final int guiSize;
    public final int timerSlot;

    public final int gameDuration; // 게임 시간 (초)
    public final int maxBlockCount; // 블럭 생성 개수
    public final int maxBombCount; // 블럭 생성 개수
    public final int startBlockCount; // 초기 타겟 블럭 개수

    private Integer taskId;

    public CatchDiamondManager() {

        MessageUtil.sendConsoleMessage("CatchDiamondManager init...", Color.GREEN);
        isNeedsIaZip = ItemsAdderUtil.extractDefaultAssets(LaMiniGame.getInstance());
        MessageUtil.sendConsoleMessage("CacheDiamondMange.isNeedsIaZip : " + isNeedsIaZip, Color.GREEN);

        guiTitle = "Mini Game"; // GUI 타이틀
        startMessage = ColorUtil.colorText("Mini Game started!", Color.YELLOW); // 게임 시작 메시지
        endSuccessMessage = ColorUtil.colorText("Congratulations! You win!", Color.GREEN); // 게임 성공 메시지
        endFailMessage = ColorUtil.colorText("Game over! You lose!", Color.RED); // 게임 실패 메시지

        games = new HashMap<>();

        guiSize = 54;
        timerSlot = 45;
        gameDuration = 30; // 게임 시간 (초)
        maxBlockCount = 8; // 블럭 생성 개수
        maxBombCount = 5; // 블럭 생성 개수
        startBlockCount = 3; //  초기 타겟 블럭 개수

        catchDiamondBlocks =
                Arrays.asList(
                        new CatchDiamondBlock(ItemsAdderUtil.getCustomItemStack("customblock:ruby", new ItemStack(Material.DIAMOND)), 10, 2),
                        new CatchDiamondBlock(new ItemStack(Material.GOLD_INGOT, 1), 7, 4),
                        new CatchDiamondBlock(new ItemStack(Material.COAL, 1), 5, 5)
                );
        bombBlocks =
                Arrays.asList(
                        new CatchDiamondBlock(new ItemStack(Material.TNT, 1), -20, 4)
                );

        blockItemStacks = new HashMap<>();

        for (CatchDiamondBlock catchDiamondBlock : catchDiamondBlocks) {
            blockItemStacks.put(catchDiamondBlock.getBlock(), catchDiamondBlock);
        }
        for (CatchDiamondBlock catchDiamondBlock : bombBlocks) {
            blockItemStacks.put(catchDiamondBlock.getBlock(), catchDiamondBlock);
        }

        this.taskId = null;
        Bukkit.getPluginManager().registerEvents(this, LaMiniGame.getInstance());
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
            Bukkit.getScheduler().runTaskLater(LaMiniGame.getInstance(), () -> {
                player.openInventory(event.getInventory());
                MessageUtil.sendPlayerMessage(player, "게임 중에는 창을 종료하실 수 없습니다.");
            }, 1L);
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

                if (clickedItem != null && blockItemStacks.containsKey(clickedItem)) {

                    Integer targetScore = blockItemStacks.get(clickedItem).getScore();
                    catchDiamond.addScore(targetScore);
                    clickedInv.setItem(event.getSlot(), new ItemStack(Material.AIR));

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
            CatchDiamond catchDiamond = new CatchDiamond(guiTitle, player);
            games.put(player, catchDiamond);

            player.openInventory(catchDiamond.getGui());
        }
        if (Objects.isNull(taskId)) {
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(LaMiniGame.getInstance(), this, 20L, 20L);
        }
    }

    public void endGame(Player player) {
        if (games.containsKey(player)) {
            CatchDiamond catchDiamond = games.get(player);
            games.remove(player);
            player.closeInventory();

            CatchDiamondEndEvent catchDiamondEndEvent = new CatchDiamondEndEvent(catchDiamond);
            Bukkit.getPluginManager().callEvent(catchDiamondEndEvent);

            catchDiamond.free();
        }
    }

    @EventHandler
    public void onCatchDiamondEndEvent(CatchDiamondEndEvent event) {

        CatchDiamond catchDiamond = event.getCatchDiamond();
        //TODO LOG 생성
        MessageUtil.sendPlayerMessage(catchDiamond.getPlayer(), "- 게임 결과 -");
        MessageUtil.sendPlayerMessage(catchDiamond.getPlayer(), " 부순 블록 : " + catchDiamond.getBrokenBlockCount());
        MessageUtil.sendPlayerMessage(catchDiamond.getPlayer(), " 점수 : " + catchDiamond.getScore());

    }

}