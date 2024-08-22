package me.sword7.starmail.gui.page;

import me.sword7.starmail.gui.*;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.gui.data.WarehouseData;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.pack.UpdateResult;
import me.sword7.starmail.pack.tracking.TrackingCache;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.X.XGlass;
import me.sword7.starmail.util.X.XMaterial;
import me.sword7.starmail.warehouse.WarehouseCache;
import me.sword7.starmail.warehouse.WarehouseEntry;
import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class WarehouseContents extends IUpdater {

    public WarehouseContents() {
        buildInsertables();
    }

    @Override
    public Inventory populate(Inventory menu, SessionData sessionData) {
        WarehouseData warehouseData = (WarehouseData) sessionData;
        WarehouseEntry entry = warehouseData.getEntry();
        menu.setItem(8, Icons.createMail(entry.getMail()));

        UUID trackingNo = Pack.getTrackingNo(entry.getItemStack());
        if (TrackingCache.isTracked(trackingNo)) {
            menu.setItem(4, Icons.createIcon(XMaterial.CHEST.parseMaterial(), ChatColor.WHITE.toString() + Language.LABEL_CONTENTS));
            ItemStack[] contents = TrackingCache.getContents(trackingNo);
            int index = 10;
            for (int i = 0; i < 21; i++) {
                int mailIndex = i;
                ItemStack itemStack = (mailIndex < contents.length) ? contents[mailIndex] : Icons.AIR;
                if (itemStack == null) itemStack = Icons.AIR;
                menu.setItem(index, itemStack);
                index += ((index + 2) % 9 == 0 ? 3 : 1);
            }
            menu.setItem(41, Icons.createIcon(XMaterial.LEVER.parseMaterial(), ChatColor.WHITE.toString() + Language.ICON_APPLY));
        } else {
            menu.setItem(4, Icons.createIcon(XMaterial.BARRIER.parseMaterial(), ChatColor.RED.toString() + Language.LABEL_EXPIRED));
            menu.setItem(41, Icons.createIcon(XMaterial.BARRIER.parseMaterial(), ChatColor.RED.toString() + Language.LABEL_EXPIRED));
        }

        ItemStack blackStack = XGlass.BLACK.getDot();
        menu.setItem(38, blackStack);
        menu.setItem(39, Icons.CLOSE);
        menu.setItem(40, blackStack);
        menu.setItem(42, blackStack);


        return menu;
    }

    @Override
    public void processClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType) {
        if (slot == 39) {
            sessionData.exit();
        } else if (slot == 41) {
            MenuUtil.playClickSound(player);
            WarehouseData warehouseData = (WarehouseData) sessionData;
            WarehouseEntry entry = warehouseData.getEntry();
            ItemStack itemStack = entry.getItemStack();
            UUID trackingNo = Pack.getTrackingNo(itemStack);
            if (TrackingCache.isTracked(trackingNo)) {
                UpdateResult result = UpdateResult.getResult(menu);
                if (result.status == Pack.ContentStatus.VALID) {
                    ItemStack[] contents = result.getContents();
                    TrackingCache.updateContents(trackingNo, contents);

                    ItemMeta sealedMeta = Pack.seal(itemStack.getItemMeta(), trackingNo, contents);
                    itemStack.setItemMeta(sealedMeta);
                    entry.setItemStack(itemStack);

                    WarehouseCache.registerEdit(warehouseData.getType(), entry);
                    menu.setItem(8, Icons.createMail(entry.getMail()));
                    doAccept(player, sessionData, menu);
                } else {
                    doReject(player, sessionData, menu);
                }
            } else {
                doReject(player, sessionData, menu);
            }
        }
    }

    private List<Integer> insertableList = new ArrayList<>();
    private Set<Integer> insertableSet = new HashSet<>();

    private void buildInsertables() {
        for (int i = 10; i < 35; i++) {
            if (i % 9 != 0 && (i + 1) % 9 != 0) {
                insertableList.add(i);
                insertableSet.add(i);
            }
        }
    }

    @Override
    public boolean isInsertable(int slot) {
        return insertableSet.contains(slot);
    }

    @Override
    public List<Integer> getOrderedSlots() {
        return insertableList;
    }

    private List<Integer> animationSlots = new ImmutableList.Builder<Integer>()
            .add(1).add(2).add(3).add(5).add(6).add(7)
            .add(9).add(17)
            .add(18).add(26)
            .add(27).add(35)
            .add(36).add(37).add(43).add(44)
            .build();

    @Override
    public List<Integer> getAnimationSlots() {
        return animationSlots;
    }

}