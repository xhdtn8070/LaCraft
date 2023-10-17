package org.lacraft.partdamage;

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
import org.lacraft.partdamage.domain.BodyHealth;
import org.lacraft.util.api.ColorUtil.Color;
import org.lacraft.util.api.MessageUtil;

public class LaPartDamage extends JavaPlugin implements Listener {

    private static volatile LaPartDamage instance;


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

//
//        String version = getVersion(getServer());
//        final Integer second = Integer.valueOf(Integer.parseInt(version.split("_")[1]));
//        BukkitRunnable healthCheckRunnable = new BukkitRunnable() {
//            public void run() {
//                for (Map.Entry<Player, BodyHealth> entry : LaPartDamage.this.healthMap.entrySet()) {
//                    Player player = entry.getKey();
//                    if (player.isDead()) {
//                        LaPartDamage.sendPopupMessage(player, ChatColor.RED + "죽었습니다");
//                        return;
//                    }
//                    BodyHealth health = entry.getValue();
//                    double allHealth = health.getHealth();
//                    double maxHp = player.getMaxHealth();
//                    double headHp = 0.18181818181818182D * maxHp;
//                    double bodyHp = 0.2727272727272727D * maxHp;
//                    double rightArmHp = 0.13636363636363635D * maxHp;
//                    double leftArmHp = 0.13636363636363635D * maxHp;
//                    double rightLegHp = 0.13636363636363635D * maxHp;
//                    double leftLegHp = 0.13636363636363635D * maxHp;
//                    if (player.getHealth() > allHealth) {
//                        double heal = player.getHealth() - allHealth;
//                        if (heal >= headHp - health.headHealth) {
//                            heal -= headHp - health.headHealth;
//                            health.headHealth = headHp;
//                        } else {
//                            health.headHealth += heal;
//                            heal = 0.0D;
//                        }
//                        if (heal >= bodyHp - health.bodyHealth) {
//                            heal -= bodyHp - health.bodyHealth;
//                            health.bodyHealth = bodyHp;
//                        } else {
//                            health.bodyHealth += heal;
//                            heal = 0.0D;
//                        }
//                        if (heal >= rightArmHp - health.rightArmHealth) {
//                            heal -= rightArmHp - health.rightArmHealth;
//                            health.rightArmHealth = rightArmHp;
//                        } else {
//                            health.rightArmHealth += heal;
//                            heal = 0.0D;
//                        }
//                        if (heal >= leftArmHp - health.leftArmHealth) {
//                            heal -= leftArmHp - health.leftArmHealth;
//                            health.leftArmHealth = leftArmHp;
//                        } else {
//                            health.leftArmHealth += heal;
//                            heal = 0.0D;
//                        }
//                        if (heal >= rightLegHp - health.rightLegHealth) {
//                            heal -= rightLegHp - health.rightLegHealth;
//                            health.rightLegHealth = rightLegHp;
//                        } else {
//                            health.rightLegHealth += heal;
//                            heal = 0.0D;
//                        }
//                        if (heal >= leftLegHp - health.leftLegHealth) {
//                            heal -= leftLegHp - health.leftLegHealth;
//                            health.leftLegHealth = leftLegHp;
//                        } else {
//                            health.leftLegHealth += heal;
//                            heal = 0.0D;
//                        }
//                    } else if (player.getHealth() < allHealth) {
//                        double damage = allHealth - player.getHealth();
//                        health.headHealth -= 0.18181818181818182D * damage;
//                        if (health.headHealth < 0.0D)
//                            health.headHealth = 0.0D;
//                        health.bodyHealth -= 0.2727272727272727D * damage;
//                        if (health.bodyHealth < 0.0D)
//                            health.bodyHealth = 0.0D;
//                        health.rightArmHealth -= 0.13636363636363635D * damage;
//                        if (health.rightArmHealth < 0.0D)
//                            health.rightArmHealth = 0.0D;
//                        health.leftArmHealth -= 0.13636363636363635D * damage;
//                        if (health.leftArmHealth < 0.0D)
//                            health.leftArmHealth = 0.0D;
//                        health.rightLegHealth -= 0.13636363636363635D * damage;
//                        if (health.rightLegHealth < 0.0D)
//                            health.rightLegHealth = 0.0D;
//                        health.leftLegHealth -= 0.13636363636363635D * damage;
//                        if (health.leftLegHealth < 0.0D)
//                            health.leftLegHealth = 0.0D;
//                    }
//                    if (health.headHealth <= 0.18181818181818182D * maxHp / 2.0D)
//                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
//                    if (health.headHealth <= 0.18181818181818182D * maxHp / 4.0D * 3.0D)
//                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
//                    if (health.rightArmHealth < 0.001D)
//                        if (second.intValue() >= 9) {
//                            ItemStack item = player.getInventory().getItemInMainHand();
//                            if (item.getType() != Material.AIR) {
//                                player.getInventory().remove(item);
//                                double yaw = (player.getLocation().getYaw() / 180.0F) * Math.PI;
//                                double pitch = (player.getLocation().getPitch() / 180.0F) * Math.PI;
//                                Vector moveVector = new Vector(-Math.sin(yaw) * Math.cos(pitch), -Math.sin(pitch), Math.cos(yaw) * Math.cos(pitch));
//                                player.getWorld().dropItem(player.getLocation().add(moveVector), item);
//                            }
//                        } else {
//                            ItemStack item = player.getInventory().getItemInHand();
//                            if (item.getType() != Material.AIR) {
//                                player.getInventory().remove(item);
//                                double yaw = (player.getLocation().getYaw() / 180.0F) * Math.PI;
//                                double pitch = (player.getLocation().getPitch() / 180.0F) * Math.PI;
//                                Vector moveVector = new Vector(-Math.sin(yaw) * Math.cos(pitch), -Math.sin(pitch), Math.cos(yaw) * Math.cos(pitch));
//                                player.getWorld().dropItem(player.getLocation().add(moveVector), item);
//                            }
//                        }
//                    if (health.leftArmHealth < 0.001D &&
//                            second.intValue() >= 9) {
//                        ItemStack item = player.getInventory().getItemInOffHand();
//                        if (item.getType() != Material.AIR) {
//                            player.getInventory().remove(item);
//                            double yaw = (player.getLocation().getYaw() / 180.0F) * Math.PI;
//                            double pitch = (player.getLocation().getPitch() / 180.0F) * Math.PI;
//                            Vector moveVector = new Vector(-Math.sin(yaw) * Math.cos(pitch), -Math.sin(pitch), Math.cos(yaw) * Math.cos(pitch));
//                            player.getWorld().dropItem(player.getLocation().add(moveVector), item);
//                        }
//                    }
//                    if (health.leftLegHealth < 0.001D && health.rightLegHealth < 0.001D) {
//                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 7));
//                    } else if (health.leftLegHealth < 0.001D) {
//                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
//                    } else if (health.rightLegHealth < 0.001D) {
//                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
//                    }
//                    LaPartDamage.sendPopupMessage(player, String.format("%s, %s, %s, %s, %s, %s", new Object[] { ChatColor.AQUA + Double.toString(Math.floor(0.18181818181818182D * maxHp * 10.0D) / 10.0D) + ChatColor.WHITE, ChatColor.AQUA + Double.toString(Math.floor(0.2727272727272727D * maxHp * 10.0D) / 10.0D) + ChatColor.WHITE, ChatColor.AQUA + Double.toString(Math.floor(0.13636363636363635D * maxHp * 10.0D) / 10.0D) + ChatColor.WHITE, ChatColor.AQUA + Double.toString(Math.floor(0.13636363636363635D * maxHp * 10.0D) / 10.0D) + ChatColor.WHITE, ChatColor.AQUA + Double.toString(Math.floor(0.13636363636363635D * maxHp * 10.0D) / 10.0D) + ChatColor.WHITE, ChatColor.AQUA + Double.toString(Math.floor(0.13636363636363635D * maxHp * 10.0D) / 10.0D) + ChatColor.WHITE }));
//                }
//            }
//        };
//        healthCheckRunnable.runTaskTimer((Plugin)this, 1L, 1L);
//        try {
//            Class<?> chatSerializer = null;
//            Class<?> packetPlayOutChat = null;
//            String minecraftVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
//            chatSerializer = Class.forName("net.minecraft.server." + minecraftVersion + ".IChatBaseComponent$ChatSerializer");
//            a = chatSerializer.getMethod("a", new Class[] { (new String()).getClass() });
//            packetPlayOutChat = Class.forName("net.minecraft.server." + minecraftVersion + ".PacketPlayOutChat");
//            packetPlayOutChatConstructor = packetPlayOutChat.getConstructor(new Class[] { Class.forName("net.minecraft.server." + minecraftVersion + ".IChatBaseComponent"), byte.class });
//            getHandle = Class.forName("org.bukkit.craftbukkit." + minecraftVersion + ".entity.CraftPlayer").getMethod("getHandle", new Class[0]);
//            playerConnection = Class.forName("net.minecraft.server." + minecraftVersion + ".EntityPlayer").getField("playerConnection");
//            playerConnection.setAccessible(true);
//            sendPacket = playerConnection.getType().getMethod("sendPacket", new Class[] { Class.forName("net.minecraft.server." + minecraftVersion + ".Packet") });
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
    }

//    @EventHandler
//    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
//        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
//            if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
//                BodyHealth health;
//                Player attacker = (Player)event.getDamager();
//                Player victim = (Player)event.getEntity();
//                BodyPart bodyPart = BodyPartChecker.bodyCheck(event.getEntity(), this.finalLocations.get(attacker));
//                int partsNumber = bodyPart.getCode();
//                if (this.healthMap.containsKey(victim)) {
//                    health = this.healthMap.get(victim);
//                } else {
//                    health = initHealth((LivingEntity)victim);
//                    this.healthMap.put(victim, health);
//                }
//                double damage = event.getFinalDamage();
//                if (partsNumber == 0) {
//                    if (health.headHealth > damage) {
//                        health.headHealth -= damage;
//                    } else {
//                        health.headHealth = 0.0D;
//                        event.setDamage(10000.0D);
//                        return;
//                    }
//                } else if (partsNumber == 1) {
//                    if (health.bodyHealth > damage) {
//                        health.bodyHealth -= damage;
//                    } else {
//                        health.bodyHealth = 0.0D;
//                        event.setDamage(10000.0D);
//                        return;
//                    }
//                } else if (partsNumber == 2) {
//                    if (health.rightArmHealth > damage) {
//                        health.rightArmHealth -= damage;
//                    } else {
//                        event.setDamage(health.rightArmHealth);
//                        health.rightArmHealth = 0.0D;
//                    }
//                } else if (partsNumber == 3) {
//                    if (health.leftArmHealth > damage) {
//                        health.leftArmHealth -= damage;
//                    } else {
//                        event.setDamage(health.leftArmHealth);
//                        health.leftArmHealth = 0.0D;
//                    }
//                } else if (partsNumber == 4) {
//                    if (health.rightLegHealth > damage) {
//                        health.rightLegHealth -= damage;
//                    } else {
//                        event.setDamage(health.rightLegHealth);
//                        health.rightLegHealth = 0.0D;
//                    }
//                } else if (partsNumber == 5) {
//                    if (health.leftLegHealth > damage) {
//                        health.leftLegHealth -= damage;
//                    } else {
//                        event.setDamage(health.leftLegHealth);
//                        health.leftLegHealth = 0.0D;
//                    }
//                }
//                this.healthMap.put(victim, health);
//            }
//        } else if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) &&
//                event.getEntity() instanceof Player) {
//            BodyHealth health;
//            Projectile projectile = (Projectile)event.getDamager();
//            Player victim = (Player)event.getEntity();
//            BodyPart bodyPart = BodyPartChecker.bodyCheck(event.getEntity(), projectile.getLocation());
//            int partsNumber = bodyPart.getCode();
//            if (this.healthMap.containsKey(victim)) {
//                health = this.healthMap.get(victim);
//            } else {
//                health = initHealth((LivingEntity)victim);
//                this.healthMap.put(victim, health);
//            }
//            double damage = event.getFinalDamage();
//            if (partsNumber == 0) {
//                if (health.headHealth > damage) {
//                    health.headHealth -= damage;
//                } else {
//                    health.headHealth = 0.0D;
//                    event.setDamage(10000.0D);
//                    return;
//                }
//            } else if (partsNumber == 1) {
//                if (health.bodyHealth > damage) {
//                    health.bodyHealth -= damage;
//                } else {
//                    health.bodyHealth = 0.0D;
//                    event.setDamage(10000.0D);
//                    return;
//                }
//            } else if (partsNumber == 2) {
//                if (health.rightArmHealth > damage) {
//                    health.rightArmHealth -= damage;
//                } else {
//                    event.setDamage(health.rightArmHealth);
//                    health.rightArmHealth = 0.0D;
//                }
//            } else if (partsNumber == 3) {
//                if (health.leftArmHealth > damage) {
//                    health.leftArmHealth -= damage;
//                } else {
//                    event.setDamage(health.leftArmHealth);
//                    health.leftArmHealth = 0.0D;
//                }
//            } else if (partsNumber == 4) {
//                if (health.rightLegHealth > damage) {
//                    health.rightLegHealth -= damage;
//                } else {
//                    event.setDamage(health.rightLegHealth);
//                    health.rightLegHealth = 0.0D;
//                }
//            } else if (partsNumber == 5) {
//                if (health.leftLegHealth > damage) {
//                    health.leftLegHealth -= damage;
//                } else {
//                    event.setDamage(health.leftLegHealth);
//                    health.leftLegHealth = 0.0D;
//                }
//            }
//            this.healthMap.put(victim, health);
//        }
//    }

    @Override
    public void onDisable() {
        MessageUtil.sendConsoleMessage("파트 데미지 플러그인 onDisable", Color.GREEN);

        instance = null;
    }

    public static LaPartDamage getInstance() {
        if (instance == null) {
            synchronized (LaPartDamage.class) {
                if (instance == null) {
                    instance = getPlugin(LaPartDamage.class);
                }
            }
        }
        return instance;
    }
}