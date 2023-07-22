package org.lacraft.minigame.catchdiamond.command;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lacraft.minigame.catchdiamond.manager.CatchDiamondManager;

public class CatchDiamondCommand implements CommandExecutor {
    @Getter
    private static final CatchDiamondCommand instance = new CatchDiamondCommand();

    private CatchDiamondCommand() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!label.equalsIgnoreCase("minigame")) return true; // 만약 명령어가 "minigame"이 아니면 더 이상 진행하지 않습니다.

        // "catchDiamond" 미니게임 시작 명령 처리
        if (args.length == 2 && args[0].equalsIgnoreCase("catchDiamond") && args[1].equalsIgnoreCase("start")) {
            if(!isPlayer(sender)) { // sender가 플레이어인지 확인합니다.
                sender.sendMessage("<RED>This command can only be run by a player.</RED>"); // 플레이어가 아닌 경우 오류 메시지를 출력합니다.
                return true;
            } else {
                List<Player> players = new ArrayList<>();
                players.add((Player) sender); // sender가 플레이어인 경우 게임을 시작합니다.
                CatchDiamondManager.getInstance().startGame(players);
                return true;
            }
        }
        // TODO: 여기에 추가적인 명령어 처리 코드를 작성하세요.

        return false;
    }

    /**
     * 이 메소드는 주어진 CommandSender 객체가 플레이어인지 아닌지를 판별합니다.
     * @param sender 명령어를 실행한 주체
     * @return 만약 sender가 Player의 인스턴스라면 true, 그렇지 않다면 false를 반환합니다.
     */
    private boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }
}



