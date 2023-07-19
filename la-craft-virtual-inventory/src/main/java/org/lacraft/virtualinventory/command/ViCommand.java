package org.lacraft.virtualinventory.command;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lacraft.util.api.MessageUtil;
import org.lacraft.virtualinventory.LaVirtualInventory;
import org.lacraft.virtualinventory.config.MessageConfig;
import org.lacraft.virtualinventory.config.PluginConfig;
import org.lacraft.virtualinventory.domain.VirtualInventory;
import org.lacraft.virtualinventory.manager.VirtualInventoryManager;

;

public class ViCommand implements CommandExecutor {

    @Getter
    private static final ViCommand instance = new ViCommand();

    private ViCommand() {
        LaVirtualInventory.getInstance().getCommand("vi").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        /**
         * /vi
         *  - 커맨드 자체의 설명
         */
        if (args.length == 0) {
            getInit(sender);
            return true;
        }

        /**
         * /vi info
         *  - 커맨드 자체의 설명
         */
        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            getInfo(sender);
            return true;
        }

        /**
         * /vi opt
         *  - 현재 적용되어 있는 옵션 보여줌
         */
        if (args.length == 1 && args[0].equalsIgnoreCase("opt")) {
            getOpt(sender);
            return true;
        }

        /**
         * /vi {help or h}
         *  - 명령어 소개
         */
        if (args.length == 1 && (args[0].equalsIgnoreCase("h")
                || args[0].equalsIgnoreCase("help"))) {
            getHelp(sender);
            return true;
        }

        /**
         * /vi {list or n}
         *  - 자신의 창고 리스트를 확인할 수 있음
         */
        if (args.length == 1 && (args[0].equalsIgnoreCase("l")
                || args[0].equalsIgnoreCase("list"))) {
            getMyList(sender);
            return true;
        }

        /**
         * /vi {name or n} {#displayName}
         *  - displayName에 해당하는 실제 유저의 Name을 알 수 있음
         *  - 온라인 유저만 가능
         */
        if (args.length == 2 && (args[0].equalsIgnoreCase("n")
                || args[0].equalsIgnoreCase("name"))) {
            getName(sender, args[1]);
            return true;
        }

        /**
         * /vi {list or l} {#UserName}
         *  - 다른 사람의 가상 인벤토리 리스트를 확인할 수 있음
         *  - permission
         * 		- vc.admin
         */
        if (args.length == 2 && (args[0].equalsIgnoreCase("l")
                || args[0].equalsIgnoreCase("list"))) {
            getOtherList(sender, args[1]);
            return true;
        }

        /**
         * /vi {create or c} {#InventoryName}
         *  - 자신의 가상 인벤토리를 생성한다.
         */
        if (args.length == 2 && (args[0].equalsIgnoreCase("c")
                || args[0].equalsIgnoreCase("create"))) {
            createInventory(sender, args[1]);
            return true;
        }

        /**
         * /vi {open or o} {#InventoryName}
         *  - 자신의 {#InventoryName}이라는 가상 인벤토리를 오픈한다.
         */
        if (args.length == 2 && (args[0].equalsIgnoreCase("o")
                || args[0].equalsIgnoreCase("open"))) {
            getMyInventory(sender, args[1]);
            return true;
        }

        /**
         * /vi {openother or oo} {#userName} {#InventoryName}
         *  - 다른 사람의 가상 인벤토리를 오픈한다 확인할 수 있음
         *  - permission
         * 		- vc.admin
         */
        if (args.length == 3 && (args[0].equalsIgnoreCase("oo")
                || args[0].equalsIgnoreCase("openother"))) {
            getOtherInventory(sender, args[1], args[2]);
            return true;
        }

        /**
         * /vi {remove or rm} {#InventoryName}
         *  - 자신의 가상 인벤토리 {#InventoryName}를 삭제한다.
         */
        if (args.length == 2 && (args[0].equalsIgnoreCase("rm")
                || args[0].equalsIgnoreCase("remove"))) {
            removeMyInventory(sender, args[1]);
            return true;
        }

        /**
         * /vi {removeother or rmo} {#userName} {#InventoryName}
         *  - 다른 사람의 인벤토리를 삭제한다.
         *  - permission
         * 		- vc.admin
         */
        if (args.length == 3 && (args[0].equalsIgnoreCase("rmo")
                || args[0].equalsIgnoreCase("removeother"))) {
            removeOtherInventory(sender, args[1], args[2]);
            return true;
        }

        /**
         * /vi {save}
         *  - 자신의 창고 리스트를 확인할 수 있음
         *  - 서버 렉을 발생시키는 등의 비효율적인 현상이 예상되서 개발 단계에서 제거.
         */

        /**
         * /vi {saveAll}
         *  - 자신의 창고 리스트를 확인할 수 있음
         *  - permission
         *  	- vc.admin
         */
        if (args.length == 1 && (args[0].equalsIgnoreCase("saveAll"))) {
            try {
                saveAll(sender);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void getInit(CommandSender sender) {
        MessageUtil.sendPlayerMessage(sender, MessageConfig.getInstance().getInit());
    }

    private void saveAll(CommandSender sender) throws IOException {
        if (!checkPermission(sender, "vi.admin")) {
            return;
        }
        VirtualInventoryManager.getInstance().saveAll(false);
    }

    private void removeOtherInventory(CommandSender sender, String name, String inventoryName) {
        if (!checkPermission(sender, "vi.admin")) {
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(name);
        if (!checkPlayer(sender, targetPlayer)) {
            return;
        }
        VirtualInventoryManager.getInstance().removeInventory(targetPlayer, inventoryName);
    }

    private void removeMyInventory(CommandSender sender, String inventoryName) {
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            VirtualInventoryManager.getInstance().removeInventory(player, inventoryName);
        }
    }

    public boolean checkPermission(CommandSender sender, String permission) {
        if (sender.hasPermission(permission)) {
            return true;
        }
        MessageUtil.sendPlayerMessage(sender, MessageConfig.getInstance().getPlayer().getNoAllowedPermission());
        return false;
    }

    public boolean checkPlayer(CommandSender sender, Player player) {
        if (player == null) {
            MessageUtil.sendPlayerMessage(sender, MessageConfig.getInstance().getPlayer().getNoExists());
            return false;
        }
        return true;
    }

    public boolean isPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        }
        MessageUtil.sendPlayerMessage(sender, MessageConfig.getInstance().getPlayer().getNonPlayer());
        return false;
    }

    private void getOtherInventory(CommandSender sender, String name, String inventoryName) {
        if (!checkPermission(sender, "vi.admin")) {
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(name);
        if (!checkPlayer(sender, targetPlayer)) {
            return;
        }

        if (isPlayer(sender)) {
            Player player = (Player) sender;
            VirtualInventory virtualInventory =VirtualInventoryManager.getInstance().getInventroy(targetPlayer, inventoryName);
            openInventory(player, virtualInventory, inventoryName);
        }
    }

    private void openInventory(Player player, VirtualInventory virtualInventory, String inventoryName) {
        if (virtualInventory == null) {
            sendMessageConfigString(player, MessageConfig.getInstance().getInventory().getNoExists(), inventoryName);
            return;
        }
        player.openInventory(virtualInventory.getInventory());
    }

    private void getMyInventory(CommandSender sender, String inventoryName) {
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            VirtualInventory virtualInventory = VirtualInventoryManager.getInstance().getInventroy(player, inventoryName);
            openInventory(player, virtualInventory, inventoryName);
        }
    }

    private void createInventory(CommandSender sender, String inventoryName) {
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            VirtualInventory virtualInventory = VirtualInventoryManager.getInstance().createInventory(player, inventoryName);
            if (virtualInventory != null) {
                player.openInventory(virtualInventory.getInventory());
            }
            return;
        }
    }


    private void getMyList(CommandSender sender) {
        if (isPlayer(sender)) {
            printList(sender, VirtualInventoryManager.getInstance().getList((Player) sender));
        }
    }

    private void getOtherList(CommandSender sender, String name) {
        if (!checkPermission(sender, "vi.admin")) {
            return;
        }
        Player targetPlayer = Bukkit.getPlayer(name);
        if (!checkPlayer(sender, targetPlayer)) {
            return;
        }
        printList(sender, VirtualInventoryManager.getInstance().getList(targetPlayer));
    }

    private void printList(CommandSender sender, List<String> list) {
        MessageUtil.sendPlayerMessage(sender, "<YELLOW>lists :</YELLOW>");
        if (list == null) {
            return;
        }
        for (String inventoryName : list) {
            MessageUtil.sendPlayerMessage(sender, "<GREEN>" + inventoryName + "</GREEN>");
        }
    }

    private void getName(CommandSender sender, String displayName) {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayers) {
            if (player.getDisplayName().equalsIgnoreCase(displayName)) {
                MessageUtil.sendPlayerMessage(sender, "<YELLOW>displayName : " + player.displayName() + "\nName : " + player.getName() + "</YELLOW>");
                return;
            }
        }
        MessageUtil.sendPlayerMessage(sender, MessageConfig.getInstance().getPlayer().getNoExistsOnline());
    }

    private void getInfo(CommandSender sender) {
        List<String> InfoList = MessageConfig.getInstance().getInfo();
        if (InfoList == null) {
            MessageUtil.sendPlayerMessage(sender, "Info is null");
        }
        for (String info : InfoList) {
            MessageUtil.sendPlayerMessage(sender, info);
        }

    }

    private void getHelp(CommandSender sender) {
        List<String> helpList = MessageConfig.getInstance().getHelp();
        if (helpList == null) {
            //TODO
            MessageUtil.sendPlayerMessage(sender, "무언가 잘못되었다!");
        }
        for (String help : helpList) {
            MessageUtil.sendPlayerMessage(sender, help);
        }

    }

    private void getOpt(CommandSender sender) {
        if (!checkPermission(sender, "vi.admin")) {
            return;
        }
        MessageUtil.sendPlayerMessage(sender, "<GREEN> auto-save : </GREEN>" + "<AQUA>" + PluginConfig.getInstance().getAutoSave() + "</AQUA>");
        MessageUtil.sendPlayerMessage(sender, "<GREEN> auto-save-time : </GREEN>" + "<AQUA>" + PluginConfig.getInstance().getAutoSaveTime() + "</AQUA>");
        MessageUtil.sendPlayerMessage(sender, "<GREEN> save-messaging :  </GREEN>" + "<AQUA>" + PluginConfig.getInstance().getSaveMessaging() + "</AQUA>");
        MessageUtil.sendPlayerMessage(sender, "<GREEN> save-message : </GREEN>" + "<AQUA>" + PluginConfig.getInstance().getSaveMessage() + "</AQUA>");
        MessageUtil.sendPlayerMessage(sender, "<GREEN> load-messaging : </GREEN>" + "<AQUA>" + PluginConfig.getInstance().getLoadMessaging() + "</AQUA>");
        MessageUtil.sendPlayerMessage(sender, "<GREEN> load-message : </GREEN>" + "<AQUA>" + PluginConfig.getInstance().getLoadMessage() + "</AQUA>");
        MessageUtil.sendPlayerMessage(sender, "<GREEN> max-inventory : </GREEN>" + "<AQUA>" + PluginConfig.getInstance().getMaxInventory() + "</AQUA>");
        MessageUtil.sendPlayerMessage(sender, "<GREEN> save-once-per-several-times : </GREEN>" + "<AQUA>" + PluginConfig.getInstance().getSaveOncePerSeveralTimes() + "</AQUA>");

    }

    private void sendMessageConfigString(CommandSender sender, String message, String replaceWith) {
        if (message.contains("#{inventoryName}")) {
            message = message.replace("#{inventoryName}", replaceWith);
        }
        MessageUtil.sendPlayerMessage(sender, message);
    }

}
