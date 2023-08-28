package org.lacraft.util.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.lacraft.util.api.ColorUtil.Color;

public class MessageUtil {

    public static void sendConsoleMessage(String message) {
        sendConsoleMessage(message, null);
    }

    public static void sendConsoleMessage(String message, Color color) {
        if (color != null) {
            message = ColorUtil.colorText(message, color);
        }
        Component component = MiniMessage.miniMessage().deserialize(message);
        Bukkit.getConsoleSender().sendMessage(component);
    }

    public static void sendPlayerMessage(CommandSender sender, String message) {
        sendPlayerMessage(sender, message, null);
    }

    public static void sendPlayerMessage(CommandSender sender, String message, Color color) {
        if (color != null) {
            message = ColorUtil.colorText(message, color);
        }
        Component component = MiniMessage.miniMessage().deserialize(message);
        sender.sendMessage(component);
    }

    public static void sendBroadcastMessage(String message) {
        sendBroadcastMessage(message, null);
    }

    public static void sendBroadcastMessage(String message, Color color) {
        if (color != null) {
            message = ColorUtil.colorText(message, color);
        }
        Component component = MiniMessage.miniMessage().deserialize(message);
        Bukkit.getServer().broadcast(component);
    }


    @Deprecated
    private static TextColor getTextColor(org.bukkit.Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int rgb = (red << 16) | (green << 8) | blue;
        return TextColor.color(rgb);
    }


}
