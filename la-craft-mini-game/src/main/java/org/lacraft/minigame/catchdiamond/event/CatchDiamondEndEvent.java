package org.lacraft.minigame.catchdiamond.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.lacraft.minigame.catchdiamond.domain.CatchDiamond;

public class CatchDiamondEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private CatchDiamond catchDiamond;

    public CatchDiamondEndEvent(CatchDiamond catchDiamond) {
        this.catchDiamond = catchDiamond;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}