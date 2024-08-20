package me.sword7.starmail.gui.page;

import me.sword7.starmail.gui.*;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.gui.data.WarehouseData;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.pack.tracking.TrackingCache;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.X.XGlass;
import me.sword7.starmail.util.X.XMaterial;
import me.sword7.starmail.warehouse.WarehouseCache;
import me.sword7.starmail.warehouse.WarehouseEntry;
import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class WarehouseItem extends IUpdater {
    @Override
    public Inventory populate(Inventory menu, SessionData sessionData) {
        menu.setItem(4, Icons.createIcon(Material.DIAMOND, ChatColor.WHITE.toString() + Language.LABEL_ITEM));
        WarehouseData warehouseData = (WarehouseData) sessionData;
        WarehouseEntry entry = warehouseData.getEntry();
        menu.setItem(8, Icons.createMail(entry.getMail()));

        ItemStack background = Icons.BACKGROUND_ITEM;
        ItemStack blackStack = XGlass.BLACK.getDot();

        menu.setItem(10, background);
        menu.setItem(11, blackStack);
        menu.setItem(13, blackStack);
        menu.setItem(14, background);
        menu.setItem(15, Icons.createIcon(XMaterial.LEVER.parseMaterial(), ChatColor.WHITE.toString() + Language.ICON_APPLY));
        menu.setItem(16, background);
        menu.setItem(22, Icons.CLOSE);

        return menu;
    }

    @Override
    public void processClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType) {
        if (slot == 22) {
            sessionData.exit();
        } else if (slot == 15) {
            MenuUtil.playClickSound(player);
            ItemStack newStack = menu.getItem(12);
            if (newStack != null && newStack.getType() != Material.AIR) {
                WarehouseData warehouseData = (WarehouseData) sessionData;
                WarehouseEntry entry = warehouseData.getEntry();
                ItemStack old = entry.getItemStack();
                entry.setItemStack(newStack);
                if (Pack.isSealedPack(old)) {
                    UUID trackingNoOld = Pack.getTrackingNo(old);
                    UUID trackingNoNew = Pack.isSealedPack(newStack) ? Pack.getTrackingNo(newStack) : null;
                    if (!trackingNoOld.equals(trackingNoNew) && !WarehouseCache.isProtectedNo(trackingNoOld)) {
                        TrackingCache.unTrack(trackingNoOld);
                    }
                }
                if (Pack.isSealedPack(newStack)) {
                    WarehouseCache.registerWarehousePack(Pack.getTrackingNo(newStack));
                }
                WarehouseCache.registerEdit(warehouseData.getType(), entry);
                menu.setItem(8, Icons.createMail(entry.getMail()));
                doAccept(player, sessionData, menu);
            } else {
                doReject(player, sessionData, menu);
            }
        }
    }

    private List<Integer> insertable = Collections.singletonList(12);

    @Override
    public boolean isInsertable(int slot) {
        return slot == 12;
    }

    @Override
    public List<Integer> getOrderedSlots() {
        return insertable;
    }

    private List<Integer> animationSlots = new ImmutableList.Builder<Integer>()
            .add(1).add(2).add(3).add(5).add(6).add(7)
            .add(9).add(17)
            .add(18).add(19).add(20).add(21).add(23).add(24).add(25).add(26)
            .build();

    @Override
    public List<Integer> getAnimationSlots() {
        return animationSlots;
    }
}