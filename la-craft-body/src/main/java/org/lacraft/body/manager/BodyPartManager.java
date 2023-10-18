package org.lacraft.body.manager;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.lacraft.body.LaBody;
import org.lacraft.body.domain.BodyHealth;

@Getter
public class BodyPartManager implements Listener{

    @Getter
    private static final BodyPartManager instance = new BodyPartManager();

    private static final Map<Player, BodyHealth> playerBodyHealthMap = new HashMap<>();

    private BodyPartManager() {
        Bukkit.getPluginManager().registerEvents(this, LaBody.getInstance());

        this.initPlayerBodyHealth();
    }

    private void initPlayerBodyHealth(){
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (Boolean.FALSE.equals(playerBodyHealthMap.containsKey(p))) {
                playerBodyHealthMap.put(p, BodyHealth.generateFromPlayer(p));
            }
        }
    }

    private void run(){
        BukkitRunnable healthCheckRunnable = new BukkitRunnable() {
            public void run() {
                for (Map.Entry<Player, BodyHealth> entry : playerBodyHealthMap.entrySet()) {
                    Player player = entry.getKey();
                    BodyHealth health = entry.getValue();
                    processPlayerHealth(player, health);
                }
            }
        };
        healthCheckRunnable.runTaskTimer(LaBody.getInstance(), 1L, 1L);
    }

    private void processPlayerHealth(Player player, BodyHealth health) {
        // Existing if statements and logic, considering refactored methods and constants...
        if (player.isDead()) {
//            LaPartDamage.sendPopupMessage(player, ChatColor.RED + "죽었습니다");
            return;
        }
        double maxHp = player.getMaxHealth();
        double headHp = BodyHealth.HEAD_HP_RATIO * maxHp;
        double bodyHp = BodyHealth.BODY_HP_RATIO * maxHp;
        double armHp = BodyHealth.ARM_HP_RATIO * maxHp;
        double legHp = BodyHealth.LEG_HP_RATIO * maxHp;

        double allHealth = health.getHealth();
        if (player.getHealth() > allHealth) {
//            distributeHeal(player, health, player.getHealth() - allHealth);
        } else if (player.getHealth() < allHealth) {
//            distributeDamage(health, allHealth - player.getHealth());
        }

//        applyEffectsBasedOnHealth(player, health, maxHp);
//        displayHealthPopupMessage(player, maxHp);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // 피해자와 가해자가 둘 다 Player 인스턴스인지 확인합니다.
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player victim = (Player) event.getEntity(); // 피해자
            Player damager = (Player) event.getDamager(); // 가해자

            BoundingBox boundingBox = victim.getBoundingBox();
            victim.getPose();
            Location victimLocation = victim.getLocation(); // 피해자의 위치
            Location damagerEyeLocation = damager.getEyeLocation(); // 가해자의 눈 위치

            // 가해자의 눈 위치에서 피해자의 위치를 빼고, 정규화하여 벡터를 얻습니다.
            // 이렇게 함으로써 가해자와 피해자 사이의 "방향" 벡터를 얻게 됩니다.
            Vector toVictim = victimLocation.toVector().subtract(damagerEyeLocation.toVector()).normalize();

            // 가해자의 시선 방향 벡터를 얻습니다.
            Vector damagerDirection = damagerEyeLocation.getDirection();

            // 가해자의 시선 방향과 가해자에서 피해자로의 방향 사이의 각도를 계산합니다.
            double angle = toVictim.angle(damagerDirection);

            // 이제 각도와 거리를 기반으로 어느 부위가 타격되었는지를 추정합니다.
            if(angle < 0.2) {  // 임의로 설정한 각도임. 실제 테스트를 통해 조절 필요.
                // 각도가 매우 작으므로 머리에 맞았다고 추정할 수 있습니다.
            } else if(angle < 0.5) {  // 이 값도 임의로 설정함. 테스트를 통해 조절 필요.
                // 각도가 약간 크므로 몸에 맞았다고 추정할 수 있습니다.
            }

            // 추가적인 검사 및 로직...
        }
    }


    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            // Healing logic here
        }
    }
}
