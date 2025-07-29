package org.dslofficial.object;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;


public class UIItemMap extends HashMap<UIItem, String> {
    public UIItemMap() {
        super();
    }

    private String result = null;
    public String getRegisteredName(ItemStack item) {
        result = null;
        this.forEach((k, v) -> {
            if (k.item().equals(item)) result = v;
        });
        return result;
    }
}
