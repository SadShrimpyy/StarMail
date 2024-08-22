package me.sword7.starmail.gui.data;

import me.sword7.starmail.gui.LiveSessions;
import me.sword7.starmail.gui.page.PageType;
import me.sword7.starmail.util.X.XGlass;
import me.sword7.starmail.warehouse.WarehouseEntry;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WarehouseData extends SessionData {

    private String type;
    private WarehouseEntry entry;

    public WarehouseData(Player player, String type, WarehouseEntry entry) {
        super(PageType.WAREHOUSE_HOME.getPage(), player);
        setTheme(XGlass.YELLOW);
        this.type = type;
        this.entry = entry;
    }

    @Override
    public String getEffectiveTitle() {
        if (getCurrent().getType() == PageType.WAREHOUSE_HOME) {
            return super.getEffectiveTitle() + ChatColor.DARK_GRAY + ChatColor.ITALIC + " - " + type;
        } else {
            return super.getEffectiveTitle();
        }
    }

    public WarehouseEntry getEntry() {
        return entry;
    }

    public String getType() {
        return type;
    }

    @Override
    public void onEnd() {
        LiveSessions.removeEntry(type);
    }

}
