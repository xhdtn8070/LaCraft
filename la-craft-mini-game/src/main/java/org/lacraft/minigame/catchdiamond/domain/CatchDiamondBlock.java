package org.lacraft.minigame.catchdiamond.domain;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class CatchDiamondBlock {

    private ItemStack block;

    //획득 점수
    private int score;

    //잔존 시간
    private int duration;

    public CatchDiamondBlock(ItemStack block, int score, int duration) {
        this.block = block;
        this.score = score;
        this.duration = duration;
    }
}
