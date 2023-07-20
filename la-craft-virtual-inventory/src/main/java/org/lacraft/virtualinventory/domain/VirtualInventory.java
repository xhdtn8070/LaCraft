package org.lacraft.virtualinventory.domain;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;

@Getter
@Setter
public class VirtualInventory {
    private Inventory inventory;
    private boolean isModify;
    private boolean isDelete;
    private int count;

    public VirtualInventory(Inventory inventory, boolean isModify) {
        this.inventory = inventory;
        this.isModify = isModify;
        this.isDelete = false;
        this.count = 0;
    }
}