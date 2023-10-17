package org.lacraft.partdamage.manager;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.lacraft.partdamage.LaPartDamage;
import org.lacraft.util.api.ColorUtil.Color;
import org.lacraft.util.api.MessageUtil;


@Getter
public class LaPartRecordManager implements Listener, Runnable {

    @Getter
    private static final LaPartRecordManager instance = new LaPartRecordManager();

    @Setter
    private boolean isNeedsIaZip;

    // 클래스 필드나 메서드 상단에 포맷 문자열 정의
    private static final String LOCATION_FORMAT = "Location: %s, Direction: %s";

    private static final String EYE_LOCATION_FORMAT = "Eye Location: %s, Eye Direction: %s";

    private final Map<Player, Integer> players;

    public final int times;

    public final int tick;
    private Integer taskId;

    public LaPartRecordManager() {

        MessageUtil.sendConsoleMessage("CatchDiamondManager init...", Color.GREEN);

        players = new HashMap<>();
        times = 5;
        tick = 20;
        this.taskId = null;
        Bukkit.getPluginManager().registerEvents(this, LaPartDamage.getInstance());
    }

    @Override
    public void run() {
        if (players.isEmpty()) {
            if (Objects.nonNull(taskId)) {
                Bukkit.getScheduler().cancelTask(taskId);
                taskId = null;
            }
            return;
        }
        for (Map.Entry<Player, Integer> entry : players.entrySet()) {
            Player player = entry.getKey();
            Integer interval = entry.getValue();
            if (interval <= 0) {

                Location loc = player.getLocation();
                Location eyeLoc = player.getEyeLocation();

                MessageUtil.sendPlayerMessage(player, String.format(LOCATION_FORMAT, loc, loc.getDirection()), Color.GREEN);
                MessageUtil.sendPlayerMessage(player, String.format(EYE_LOCATION_FORMAT, eyeLoc, eyeLoc.getDirection()), Color.GREEN);
                entry.setValue(this.times * this.tick);

            } else {
                entry.setValue(interval - tick);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (players.containsKey(player)) {
            this.stopRecord(player);
        }
    }

    public void startRecord(List<Player> players) {
        if (Objects.isNull(taskId)) {
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(LaPartDamage.getInstance(), this, 20L, 20L);
        }
        for (Player player : players) {
            if (this.players.containsKey(player)) {
                continue;
            }
            this.players.put(player, this.times * this.tick);
        }
    }

    public void startRecord(Player player) {
        this.startRecord(Collections.singletonList(player));
    }

    public void stopRecord(List<Player> players) {
        for (Player player : players) {
            this.players.remove(player);
        }
    }

    public void stopRecord(Player player) {
        this.stopRecord(Collections.singletonList(player));
    }

}