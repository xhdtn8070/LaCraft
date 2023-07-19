package org.lacraft.virtualinventory.tab;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.lacraft.virtualinventory.LaVirtualInventory;

import java.util.ArrayList;
import java.util.List;

import org.lacraft.virtualinventory.command.ViCommand;
import org.lacraft.virtualinventory.config.TabConfiguration;
import org.lacraft.virtualinventory.manager.VirtualInventoryManager;

public class ViTab implements TabCompleter {

    @Getter
    private static final ViTab instance = new ViTab();
    
    private ViTab() {
        LaVirtualInventory.getInstance().getCommand("vi").setTabCompleter(this);
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        /**
         * /vi
         *  - 커맨드 자체의 설명
         */
        List<String> result=new ArrayList<>();

        if(args.length==1 ){
            for(String string : TabConfiguration.getInstance().getInitTabs()){
                if(string.toLowerCase().startsWith(args[0])){
                    result.add(string);
                }
            }
            return result;
        }


        /**
         * /vi {name or n} {#displayName}
         *  - displayName에 해당하는 실제 유저의 Name을 알 수 있음
         *  - 온라인 유저만 가능
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("n")
                || args[0].equalsIgnoreCase("name"))){
            for(Player player :Bukkit.getOnlinePlayers()){
                if(player.getDisplayName().startsWith(args[1])){
                    result.add(player.getDisplayName());
                }
            }
            return result;
        }

        /**
         * /vi {list or l} {#UserName}
         *  - 다른 사람의 가상 인벤토리 리스트를 확인할 수 있음
         *  - permission
         * 		- vc.admin
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("l")
                || args[0].equalsIgnoreCase("list"))){
            
            if(!ViCommand.getInstance().checkPermission(sender,"vi.admin")){
                return result;
            }
            for(Player player :Bukkit.getOnlinePlayers()){
                if(player.getName().startsWith(args[1])){
                    result.add(player.getName());
                }
            }
            return result;
        }

        /**
         * /vi {create or c} {#InventoryName}
         *  - 자신의 가상 인벤토리를 생성한다.
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("c")
                || args[0].equalsIgnoreCase("create"))){
            return result;
        }

        /**
         * /vi {open or o} {#InventoryName}
         *  - 자신의 {#InventoryName}이라는 가상 인벤토리를 오픈한다.
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("o")
                || args[0].equalsIgnoreCase("open"))){
            if(ViCommand.getInstance().isPlayer(sender)) {
                Player player = (Player) sender;
                for(String string :VirtualInventoryManager.getInstance().getList(player)){
                    if(string.startsWith(args[1])){
                        result.add(string);
                    }
                }
                return result;
            }
            return result;
        }

        /**
         * /vi {openother or oo} {#userName} {#InventoryName}
         *  - 다른 사람의 가상 인벤토리를 오픈한다 확인할 수 있음
         *  - permission
         * 		- vc.admin
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("oo")
                || args[0].equalsIgnoreCase("openother"))){
            if(!ViCommand.getInstance().checkPermission(sender,"vi.admin")){
                return result;
            }

            for(Player player :Bukkit.getOnlinePlayers()){
                if(player.getName().startsWith(args[1])){
                    result.add(player.getName());
                }
            }
            return result;
        }
        if(args.length==3 && (args[0].equalsIgnoreCase("oo")
                || args[0].equalsIgnoreCase("openother"))){
            if(!ViCommand.getInstance().checkPermission(sender,"vi.admin")) {
                return result;
            }

            if(ViCommand.getInstance().isPlayer(sender)) {

                Player targetPlayer = Bukkit.getPlayer(args[1]);
                if(!ViCommand.getInstance().checkPlayer(sender,targetPlayer)){
                    return result;
                }
                for(String string :VirtualInventoryManager.getInstance().getList(targetPlayer)){
                    if(string.startsWith(args[2])){
                        result.add(string);
                    }
                }
                return result;
            }
            return result;
        }
        /**
         * /vi {remove or rm} {#InventoryName}
         *  - 자신의 가상 인벤토리 {#InventoryName}를 삭제한다.
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("rm")
                || args[0].equalsIgnoreCase("remove"))){
            if(ViCommand.getInstance().isPlayer(sender)) {
                Player player = (Player) sender;
                for(String string :VirtualInventoryManager.getInstance().getList(player)){
                    if(string.startsWith(args[1])){
                        result.add(string);
                    }
                }
                return result;
            }
            return result;
        }

        /**
         * /vi {removeother or rmo} {#userName} {#InventoryName}
         *  - 다른 사람의 인벤토리를 삭제한다.
         *  - permission
         * 		- vc.admin
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("rmo")
                || args[0].equalsIgnoreCase("removeother"))){
            if(!ViCommand.getInstance().checkPermission(sender,"vi.admin")){
                return result;
            }

            for(Player player :Bukkit.getOnlinePlayers()){
                if(player.getName().startsWith(args[1])){
                    result.add(player.getName());
                }
            }
            return result;
        }

        if(args.length==3 && (args[0].equalsIgnoreCase("rmo")
                || args[0].equalsIgnoreCase("removeother"))){
            if(!ViCommand.getInstance().checkPermission(sender,"vi.admin")) {
                return result;
            }

            if(ViCommand.getInstance().isPlayer(sender)) {

                Player targetPlayer = Bukkit.getPlayer(args[2]);
                if(!ViCommand.getInstance().checkPlayer(sender,targetPlayer)){
                    return result;
                }
                for(String string :VirtualInventoryManager.getInstance().getList(targetPlayer)){
                    if(string.startsWith(args[2])){
                        result.add(string);
                    }
                }
                return result;
            }
            return result;
        }
        return result;
    }
}
