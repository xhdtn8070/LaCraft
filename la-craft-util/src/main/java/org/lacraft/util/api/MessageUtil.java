package org.lacraft.util.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class MessageUtil {

    public static void sendConsoleMessage(String message) {
        Component component = MiniMessage.miniMessage().deserialize(message);
        Bukkit.getServer().sendMessage(component);
    }

    public static void sendPlayerMessage(CommandSender sender, String message) {
        Component component = MiniMessage.miniMessage().deserialize(message);
        sender.sendMessage(component);
    }

    public static void sendBroadcastMessage(String message) {
        Component component = MiniMessage.miniMessage().deserialize(message);
        Bukkit.getServer().broadcast(component);
    }
}
