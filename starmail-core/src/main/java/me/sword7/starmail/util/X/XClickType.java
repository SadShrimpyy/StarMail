package me.sword7.starmail.util.X;

import org.bukkit.event.inventory.ClickType;

public enum XClickType {
    SWAP_OFFHAND,
    ;

    private ClickType clickType;

    XClickType() {
        this.clickType = parseClickType(toString());
    }

    private ClickType parseClickType(String name) {
        try {
            return ClickType.valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isSupported() {
        return clickType != null;
    }

    public ClickType getClickType() {
        return clickType;
    }

}
