package me.sword7.starmail.gui.page;

import me.sword7.starmail.gui.LiveSessions;
import me.sword7.starmail.gui.data.SealedData;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.pack.Crate;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.pack.tracking.TrackingCache;
import me.sword7.starmail.util.Scheduler;
import me.sword7.starmail.warehouse.WarehouseCache;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.UUID;

import static me.sword7.starmail.sys.Language.WARN_TRACKING_EXPIRED;

public abstract class Seal implements IPageContents {

    public abstract Inventory populate(Inventory menu, Pack pack);

    public abstract Set<Integer> getSealedSlots();

    @Override
    public Inventory populate(Inventory menu, SessionData sessionData) {
        return populate(menu, ((SealedData) sessionData).getPack());
    }

    @Override
    public void processClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType) {
        if (getSealedSlots().contains(slot)) {
            SealedData sealedData = (SealedData) sessionData;
            Pack pack = sealedData.getPack();

            if (pack instanceof Crate) {
                int packSlot = sealedData.getPackSlot();
                ItemStack packItem = player.getInventory().getItem(packSlot);
                if (packItem != null && packItem.getAmount() == 1) {
                    ((Crate) pack).removeStraps(packItem);
                    player.getInventory().setItem(packSlot, packItem);
                }
            }

            pack.playUnsealSound(player);
            sealedData.setOpened(true);
            UUID trackingNo = sealedData.getTrackingNo();
            if (!WarehouseCache.isWarehousePack(trackingNo)) {
                TrackingCache.unTrack(sealedData.getTrackingNo());
            }

            Scheduler.runLater(() -> {
                if (LiveSessions.hasSession(player)) {
                    pack.playOpenSound(player);
                    if (!sealedData.isExpired()) {
                        sessionData.transitionSilent(PageType.PACKAGE_CONTENTS);
                    } else {
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + WARN_TRACKING_EXPIRED.toString());
                    }
                }
            }, 10);


        }
    }
}
