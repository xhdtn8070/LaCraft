package org.lacraft.body.domain;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;

/**
 * 각각의 신체 부위의 건강 상태를 나타내는 클래스입니다.
 */
@Getter
@ToString
public class BodyHealth {

    // 체력 비율 상수
    public static final double HEAD_HP_RATIO = 0.18181818181818182D;
    public static final double BODY_HP_RATIO = 0.2727272727272727D;
    public static final double ARM_HP_RATIO = 0.13636363636363635D;
    public static final double LEG_HP_RATIO = 0.13636363636363635D;

    // 각 부위별 현재 체력
    private Double health;
    private Double headHealth;
    private Double bodyHealth;
    private Double rightArmHealth;
    private Double leftArmHealth;
    private Double rightLegHealth;
    private Double leftLegHealth;

    // 각 부위별 최대 체력
    private Double maxHealth;
    private Double maxHeadHealth;
    private Double maxBodyHealth;
    private Double maxRightArmHealth;
    private Double maxLeftArmHealth;
    private Double maxRightLegHealth;
    private Double maxLeftLegHealth;

    // 플레이어 데이터
    private Player player;

    public BodyHealth(Player player) {
        this.player = player;
        updateMaxHealth();
    }

    private void updateMaxHealth() {
        this.maxHealth = this.player.getMaxHealth();
        this.maxHeadHealth = HEAD_HP_RATIO * maxHealth;
        this.maxBodyHealth = BODY_HP_RATIO * maxHealth;
        this.maxRightArmHealth = ARM_HP_RATIO * maxHealth;
        this.maxLeftArmHealth = ARM_HP_RATIO * maxHealth;
        this.maxRightLegHealth = LEG_HP_RATIO * maxHealth;
        this.maxLeftLegHealth = LEG_HP_RATIO * maxHealth;

        updateHealth();
    }

    private void updateHealth() {
        this.health = this.player.getHealth();
        this.headHealth = HEAD_HP_RATIO * health;
        this.bodyHealth = BODY_HP_RATIO * health;
        this.rightArmHealth = ARM_HP_RATIO * health;
        this.leftArmHealth = ARM_HP_RATIO * health;
        this.rightLegHealth = LEG_HP_RATIO * health;
        this.leftLegHealth = LEG_HP_RATIO * health;
    }

    /**
     * 각 신체 부위의 건강 상태를 기반으로 총 건강 상태를 계산합니다.
     *
     * @return 총 건강 상태 값입니다.
     */
    public double getHealth() {
        return headHealth + bodyHealth + rightArmHealth + leftArmHealth + rightLegHealth + leftLegHealth;
    }

    public static BodyHealth generateFromPlayer(Player player) {
        return new BodyHealth(player);
    }

}
