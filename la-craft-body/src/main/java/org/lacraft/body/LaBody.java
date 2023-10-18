package org.lacraft.body;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.lacraft.body.command.LaBodyRecordCommand;
import org.lacraft.body.domain.BodyHealth;
import org.lacraft.body.manager.LaBodyRecordManager;
import org.lacraft.util.api.ColorUtil.Color;
import org.lacraft.util.api.MessageUtil;

public class LaBody extends JavaPlugin implements Listener {

    private static volatile LaBody instance;


    public static final double legHeight = 0.675D;

    public static final double bodyHeight = 0.675D;

    public static final double headHeight = 0.45D;

    public static final double headHealth = 64.0D;

    public static final double leftArmHealth = 48.0D;

    public static final double rightArmHealth = 48.0D;

    public static final double bodyHealth = 96.0D;

    public static final double leftLegHealth = 48.0D;

    public static final double rightLegHealth = 48.0D;

    public static final double maxHealth = 352.0D;

    public HashMap<Player, BodyHealth> healthMap = new HashMap<>();

    private HashMap<Player, Location> finalLocations = new HashMap<>();

    private static Method a;

    private static Method getHandle;

    private static Field playerConnection;

    private static Method sendPacket;

    private static Constructor<?> packetPlayOutChatConstructor;

    public static Location attackCheck(Player attacker) {
        for (Entity entity : attacker.getNearbyEntities(5.0D, 5.0D, 5.0D)) {
            double yaw = (attacker.getLocation().getYaw() / 180.0F) * Math.PI;
            double pitch = (attacker.getLocation().getPitch() / 180.0F) * Math.PI;
            Vector moveVector = (new Vector(-Math.sin(yaw) * Math.cos(pitch), -Math.sin(pitch), Math.cos(yaw) * Math.cos(pitch))).multiply(0.2D);
            Location moved = attacker.getEyeLocation();
            for (int move = 0; move < 50; move++) {
                moved.add(moveVector);
                Collection<Entity> entities = attacker.getWorld().getNearbyEntities(moved, 0.0D, 0.0D, 0.0D);
                if ((entities.size() > 0 && !entities.contains(attacker)) || (entities.size() > 1 && entities.contains(attacker)))
                    return moved;
            }
        }
        return null;
    }


    public static void sendPopupMessage(Player player, String message) {
        if (a == null)
            return;
        try {
            Object cbc = a.invoke(null, new Object[] { "{\"text\": \"" + message + "\"}" });
            Object ppoc = packetPlayOutChatConstructor.newInstance(new Object[] { cbc, Byte.valueOf((byte)2) });
            sendPacket.invoke(playerConnection.get(getHandle.invoke(player, new Object[0])), new Object[] { ppoc });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void onEnable() {
        MessageUtil.sendConsoleMessage("파트 데미지 플러그인 onEnable", Color.GREEN);
        instance = this;

        LaBodyRecordManager.getInstance();
        LaBodyRecordCommand.getInstance();

    }

    @Override
    public void onDisable() {
        MessageUtil.sendConsoleMessage("파트 데미지 플러그인 onDisable", Color.GREEN);

        instance = null;
    }

    public static LaBody getInstance() {
        if (instance == null) {
            synchronized (LaBody.class) {
                if (instance == null) {
                    instance = getPlugin(LaBody.class);
                }
            }
        }
        return instance;
    }
}