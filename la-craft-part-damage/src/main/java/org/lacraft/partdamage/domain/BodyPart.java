package org.lacraft.partdamage.domain;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * BodyPart 열거형은 엔터티의 몸체 부위와 그에 해당하는 코드를 나타냅니다.
 */
@Getter
public enum BodyPart {
    HEAD(0),       // 머리
    TORSO(1),      // 몸통
    LEFT_ARM(2),   // 왼팔
    RIGHT_ARM(3),  // 오른팔
    LEFT_LEG(4),   // 왼다리
    RIGHT_LEG(5),  // 오른다리
    UNKNOWN(-1);   // 알 수 없음

    private final Integer code;  // 몸체 부위 코드

    private static Map<Integer, BodyPart> map = new HashMap<>();
    /**
     * BodyPart 생성자는 몸체 부위 코드를 초기화합니다.
     *
     * @param code 몸체 부위 코드
     */
    BodyPart(Integer code) {
        this.code = code;
    }

    /**
     * getBodyPartByCode 메서드는 코드에 해당하는 몸체 부위를 반환합니다.
     *
     * @param code 몸체 부위 코드
     * @return 코드에 해당하는 몸체 부위. 없을 경우 UNKNOWN.
     */
    static {
        for (BodyPart bodyPart : BodyPart.values()) {
            map.put(bodyPart.code, bodyPart);
        }
    }

    public static BodyPart valueOf(Integer code) {
        return map.getOrDefault(code, BodyPart.UNKNOWN);
    }
}
