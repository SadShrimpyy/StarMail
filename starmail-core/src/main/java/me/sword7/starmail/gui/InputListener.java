package me.sword7.starmail.gui;

import me.sword7.starmail.StarMail;
import me.sword7.starmail.gui.data.IUpdateable;
import me.sword7.starmail.gui.data.PackData;
import me.sword7.starmail.gui.data.SealedData;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.gui.page.IInsertable;
import me.sword7.starmail.gui.page.Page;
import me.sword7.starmail.util.X.XClickType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InputListener implements Listener {

    private final ItemStackHandler itemStackHandler;

    public InputListener() {
        itemStackHandler = new ItemStackHandler();
        Plugin plugin = StarMail.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Deprecated
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        if (playerHasNoSession(e)) return;
        SessionData sessionData = LiveSessions.getData((Player) e.getWhoClicked());
        if (sessionData.isUpdating()) {
            e.setCancelled(true);
            return;
        }

        Player player = (Player) e.getWhoClicked();
        sessionData.registerClick();
        ClickType clickType = e.getClick();
        Page page = sessionData.getCurrent();
        int slot = e.getSlot();
        int packItemSlot = sessionData instanceof PackData
                ? ((PackData) sessionData).getPackSlot()
                : -99;
        int packRawSlot = (page.getRows() + 3) * 9 + packItemSlot;
        boolean isInsertablePage = page.getContents() instanceof IInsertable;
        boolean isInsertableSlot = isInsertablePage && ((IInsertable) page.getContents()).isInsertable(slot);
        boolean isGUIClick = player.getOpenInventory().getTopInventory().equals(e.getClickedInventory());

        if (clickType == ClickType.DOUBLE_CLICK) {
            e.setCancelled(true);
            if (packItemSlot != slot && wantAndCanInsert(isGUIClick, isInsertableSlot)) {
                List<Integer> orderedRawSlots = new ArrayList<>();
                if (isInsertablePage) {
                    orderedRawSlots.addAll(((IInsertable) page.getContents()).getOrderedSlots());
                }
                int bottomStart = page.getRows() * 9;
                for (int i = bottomStart; i < bottomStart + 9 * 4; i++) {
                    orderedRawSlots.add(i);
                }
                itemStackHandler.doFakeDouble(e, player.getOpenInventory(), orderedRawSlots, packRawSlot);
            }
        } else if (clickType.isShiftClick()) {
            if (isPackInventory(e)) {
                e.setCancelled(false);
                return;
            }
            e.setCancelled(true);
            if (canInsertItem(isInsertablePage, packItemSlot, slot)) {
                Inventory top = player.getOpenInventory().getTopInventory();
                Inventory bottom = player.getOpenInventory().getBottomInventory();
                if (isGUIClick && isInsertableSlot) {
                    itemStackHandler.doFakeShift(bottom, top, slot, null, packItemSlot);
                } else if (!isGUIClick) {
                    List<Integer> orderedSlots = ((IInsertable) page.getContents()).getOrderedSlots();
                    itemStackHandler.doFakeShift(top, bottom, slot, orderedSlots, packItemSlot);
                    itemStackHandler.removeStackFromInventory(player.getInventory(), slot);
                }
            }
        } else if (XClickType.SWAP_OFFHAND.isSupported() && clickType == XClickType.SWAP_OFFHAND.getClickType()) {
            if (isGUIClick && !isInsertableSlot) {
                e.setCancelled(true);
                sessionData.fixOffHandGlitch();
            } else if (packItemSlot == slot) {
                e.setCancelled(true);
                sessionData.fixOffHandGlitch();
            }
        } else {
            if (isGUIClick && !isInsertableSlot) {
                e.setCancelled(true);
            }
        }

        if (slot == packItemSlot || e.getHotbarButton() == packItemSlot) e.setCancelled(true);

        if (isGUIClick || isInsertablePage) {
            int effectiveSlot = isGUIClick ? slot : -1;
            page.processClick(player, player.getOpenInventory().getTopInventory(), sessionData, effectiveSlot, e.getClick());
        }
    }

    private static boolean playerHasNoSession(InventoryClickEvent e) {
        return e.getClickedInventory() == null || !LiveSessions.hasSession(e.getWhoClicked());
    }

    private static boolean wantAndCanInsert(boolean isGUIClick, boolean isInsertableSlot) {
        return !isGUIClick || isInsertableSlot;
    }

    private static boolean canInsertItem(boolean isInsertablePage, int packItemSlot, int slot) {
        return isInsertablePage && packItemSlot != slot;
    }

    private static boolean isPackInventory(InventoryClickEvent e) {
        return Objects.requireNonNull(e.getClickedInventory()).getType() == InventoryType.CHEST;
    }

    private static boolean isMenuClick(Inventory clickedInventory, HumanEntity humanEntity) {
        return humanEntity.getOpenInventory().getTopInventory().equals(clickedInventory);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrag(InventoryDragEvent e) {
        if (LiveSessions.hasSession(e.getWhoClicked())) return;

        Player player = (Player) e.getWhoClicked();
        SessionData sessionData = LiveSessions.getData(player);
        if (sessionData.isUpdating()) {
            e.setCancelled(true);
        } else {
            sessionData.registerClick();
            Page page = sessionData.getCurrent();
            if (page.getContents() instanceof IInsertable) {
                IInsertable insertable = (IInsertable) page.getContents();
                page.processClick(player, player.getOpenInventory().getTopInventory(), sessionData, -1, null);
                for (int slot : e.getRawSlots()) {
                    if (!insertable.isInsertable(slot) && slot < page.getRows() * 9) {
                        e.setCancelled(true);
                        return;
                    }
                }
            } else {
                for (int slot : e.getRawSlots()) {
                    if (slot < page.getRows() * 9) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onClose(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player player = (Player) e.getPlayer();
            if (LiveSessions.hasSession(player)) {
                SessionData sessionData = LiveSessions.getData(player);
                if (!sessionData.isTransitioning()) {
                    if (sessionData instanceof IUpdateable) {
                        ((IUpdateable) sessionData).updateContents(e.getInventory());
                    }
                    LiveSessions.end(player);
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (LiveSessions.hasSession(player)) {
            SessionData sessionData = LiveSessions.getData(player);
            player.getOpenInventory();
            Inventory inventory = player.getOpenInventory().getTopInventory();
            if (isMenuClick(inventory, player)) {
                if (sessionData instanceof IUpdateable) {
                    ((IUpdateable) sessionData).updateContents(inventory);
                } else if (sessionData instanceof SealedData) {
                    SealedData sealedData = (SealedData) sessionData;
                    if (sealedData.isOpened()) {
                        e.getDrops().remove(player.getInventory().getItem(sealedData.getPackSlot()));
                    }
                }
            }
            LiveSessions.end(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (LiveSessions.hasSession(player)) {
            SessionData sessionData = LiveSessions.getData(player);
            player.getOpenInventory();
            Inventory inventory = player.getOpenInventory().getTopInventory();
            if (sessionData instanceof IUpdateable && isMenuClick(inventory, player)) {
                ((IUpdateable) sessionData).updateContents(inventory);
            }
            LiveSessions.end(player);
        }
    }

}
