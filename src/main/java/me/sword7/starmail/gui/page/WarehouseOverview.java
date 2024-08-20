package me.sword7.starmail.gui.page;

import me.sword7.starmail.gui.*;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.gui.data.WarehouseData;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.pack.tracking.TrackingCache;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.X.XDye;
import me.sword7.starmail.util.X.XMaterial;
import me.sword7.starmail.warehouse.WarehouseEntry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class WarehouseOverview implements IPageContents {
    @Override
    public Inventory populate(Inventory menu, SessionData sessionData) {
        menu.setItem(4, Icons.WAREHOUSE_ITEM);

        WarehouseData warehouseData = (WarehouseData) sessionData;
        WarehouseEntry entry = warehouseData.getEntry();
        menu.setItem(8, Icons.createMail(entry.getMail()));
        WarehouseEntry.Type entryType = entry.getType();

        ItemStack itemStack = Icons.createIcon(Material.DIAMOND, ChatColor.YELLOW.toString() + Language.LABEL_ITEM);
        ItemStack fromStack = Icons.createIcon(XMaterial.NAME_TAG.parseMaterial(), ChatColor.YELLOW.toString() + Language.LABEL_FROM);
        ItemStack style = XDye.YELLOW.parseItemStack();
        ItemMeta meta = style.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW.toString() + Language.LABEL_STYLE);
        style.setItemMeta(meta);

        ItemStack background = Icons.BACKGROUND_ITEM;
        if (entryType == WarehouseEntry.Type.PACK) {
            menu.setItem(10, itemStack);
            menu.setItem(11, background);
            UUID trackingNo = Pack.getTrackingNo(entry.getItemStack());
            ItemStack contents = TrackingCache.isTracked(trackingNo) ? Icons.createIcon(XMaterial.CHEST.parseMaterial(), ChatColor.YELLOW.toString() + Language.LABEL_CONTENTS) :
                    Icons.createIcon(XMaterial.BARRIER.parseMaterial(), ChatColor.RED + Language.LABEL_EXPIRED.toString());
            menu.setItem(12, contents);
            menu.setItem(13, background);
            menu.setItem(14, style);
            menu.setItem(15, background);
            menu.setItem(16, fromStack);
        } else {
            menu.setItem(10, background);
            menu.setItem(11, itemStack);
            menu.setItem(12, background);
            if (entryType == WarehouseEntry.Type.LETTER) {
                menu.setItem(13, style);
            } else {
                menu.setItem(13, background);
            }
            menu.setItem(14, background);
            menu.setItem(15, fromStack);
            menu.setItem(16, background);
        }

        menu.setItem(22, Icons.CLOSE);

        return menu;
    }

    @Override
    public void processClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType) {
        if (slot == 22) {
            sessionData.exit();
        } else {
            WarehouseData warehouseData = (WarehouseData) sessionData;
            WarehouseEntry entry = warehouseData.getEntry();
            WarehouseEntry.Type entryType = entry.getType();
            if (entryType == WarehouseEntry.Type.PACK) {
                if (slot == 10) {
                    sessionData.transition(PageType.WAREHOUSE_ITEM);
                } else if (slot == 12) {
                    sessionData.transition(PageType.WAREHOUSE_CONTENTS);
                } else if (slot == 14) {
                    sessionData.transition(PageType.WAREHOUSE_STYLE);
                } else if (slot == 16) {
                    sessionData.transition(PageType.WAREHOUSE_FROM);
                }
            } else {
                if (slot == 11) {
                    sessionData.transition(PageType.WAREHOUSE_ITEM);
                } else if (slot == 13 && entryType == WarehouseEntry.Type.LETTER) {
                    sessionData.transition(PageType.WAREHOUSE_STYLE);
                } else if (slot == 15) {
                    sessionData.transition(PageType.WAREHOUSE_FROM);
                }
            }
        }
    }
}