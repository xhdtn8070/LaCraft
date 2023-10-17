package org.lacraft.partdamage.manager;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.lacraft.partdamage.domain.BodyPart;

public class BodyPartChecker {
    private static final double YAW_CONSTANT = 1.5707963267948966D;
    private static final double ARM_SIZE = 0.225D;
    private static final double LOWER_HEIGHT_BOUND = 0.675D;
    private static final double UPPER_HEIGHT_BOUND = 1.35D;
    private static final double HEIGHT_OFFSET = 0.16875D;

    /**
     * @param entity   피격 판정을 받는 엔터티.
     * @param hitPoint 엔터티에 히트가 발생한 위치.
     * @return 피격된 몸체 부위를 의미하는 BodyPart 열거형.
     */
    public static BodyPart bodyCheck(Entity entity, Location hitPoint) {
        double height = hitPoint.getY() - entity.getLocation().getY() - HEIGHT_OFFSET;
        double yaw = (entity.getLocation().getYaw() / 180.0F) * Math.PI - YAW_CONSTANT;
        Vector left = new Vector(-Math.sin(yaw), 0.0D, Math.cos(yaw));
        yaw += Math.PI;
        Vector right = new Vector(-Math.sin(yaw), 0.0D, Math.cos(yaw));

        Vector hitPosition = new Vector(hitPoint.getX(), 0.0D, hitPoint.getZ());
        Vector entityPosition = new Vector(entity.getLocation().getX(), 0.0D, entity.getLocation().getZ());

        if (height >= 0.0D && height < LOWER_HEIGHT_BOUND) {
            double leftLegDistance = calculate2DDistance(hitPosition, entityPosition.clone().add(left.multiply(ARM_SIZE)));
            double rightLegDistance = calculate2DDistance(hitPosition, entityPosition.clone().add(right.multiply(ARM_SIZE)));
            return (rightLegDistance >= leftLegDistance) ? BodyPart.LEFT_LEG : BodyPart.RIGHT_LEG;
        }

        if (height >= LOWER_HEIGHT_BOUND && height < UPPER_HEIGHT_BOUND) {
            double leftArmDistance = calculate2DDistance(hitPosition, entityPosition.clone().add(left.multiply(ARM_SIZE * 2.0D)));
            double rightArmDistance = calculate2DDistance(hitPosition, entityPosition.clone().add(right.multiply(ARM_SIZE * 2.0D)));
            double torsoDistance = calculate2DDistance(hitPosition, entityPosition);

            if (leftArmDistance < torsoDistance && leftArmDistance <= rightArmDistance)
                return BodyPart.LEFT_ARM;
            if (rightArmDistance < torsoDistance && rightArmDistance < leftArmDistance)
                return BodyPart.RIGHT_ARM;
            if (torsoDistance <= leftArmDistance && torsoDistance <= rightArmDistance)
                return BodyPart.TORSO;
        }
        // height가 LOWER_HEIGHT_BOUND 및 UPPER_HEIGHT_BOUND 사이에 없는 경우, HEAD로 간주합니다.
        else if(height >= UPPER_HEIGHT_BOUND) {
            return BodyPart.HEAD;
        }

        return BodyPart.UNKNOWN;
    }

    private static double calculate2DDistance(Vector from, Vector to) {
        return from.clone().subtract(to).length();
    }
}

