package me.sword7.starmail.gui;

import com.google.common.collect.ImmutableList;
import me.sword7.starmail.StarMail;
import me.sword7.starmail.gui.data.IUpdateable;
import me.sword7.starmail.gui.data.PackData;
import me.sword7.starmail.gui.data.SealedData;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.gui.page.IInsertable;
import me.sword7.starmail.gui.page.Page;
import me.sword7.starmail.sys.Version;
import me.sword7.starmail.util.X.XClickType;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class InputListener implements Listener {

    public InputListener() {
        Plugin plugin = StarMail.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        Inventory inventory = e.getClickedInventory();
        if (inventory != null && LiveSessions.hasSession(e.getWhoClicked())) {
            Player player = (Player) e.getWhoClicked();
            SessionData sessionData = LiveSessions.getData(player);
            if (!sessionData.isUpdating()) {
                sessionData.registerClick();
                int slot = e.getSlot();
                Page page = sessionData.getCurrent();
                boolean isInsertablePage = page.getContents() instanceof IInsertable;
                boolean isInsertableSlot = isInsertablePage ? ((IInsertable) page.getContents()).isInsertable(slot) : false;
                boolean isGUIClick = player.getOpenInventory().getTopInventory().equals(inventory);
                ClickType clickType = e.getClick();
                int packItemSlot = sessionData instanceof PackData ? ((PackData) sessionData).getPackSlot() : -99;
                int packRawSlot = (page.getRows() + 3) * 9 + packItemSlot;

                if (clickType == ClickType.DOUBLE_CLICK) {
                    e.setCancelled(true);
                    if (packItemSlot != slot) {
                        if (!isGUIClick || isInsertableSlot) {
                            List<Integer> orderedRawSlots = new ArrayList<>();
                            if (isInsertablePage) {
                                orderedRawSlots.addAll(((IInsertable) page.getContents()).getOrderedSlots());
                            }
                            int bottomStart = page.getRows() * 9;
                            for (int i = bottomStart; i < bottomStart + 9 * 4; i++) {
                                orderedRawSlots.add(i);
                            }
                            doFakeDouble(e, player.getOpenInventory(), orderedRawSlots, packRawSlot);
                        }
                    }
                } else if (clickType.isShiftClick()) {
                    e.setCancelled(true);
                    if (isInsertablePage && packItemSlot != slot) {
                        Inventory top = player.getOpenInventory().getTopInventory();
                        Inventory bottom = player.getOpenInventory().getBottomInventory();
                        if (isGUIClick && isInsertableSlot) {
                            doFakeShift(bottom, top, slot, ShiftOrderSlots, packItemSlot);
                        } else if (!isGUIClick) {
                            List<Integer> orderedSlots = ((IInsertable) page.getContents()).getOrderedSlots();
                            doFakeShift(top, bottom, slot, orderedSlots, packItemSlot);
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
            } else {
                e.setCancelled(true);
            }
        }
    }

    private List<Integer> ShiftOrderSlots = new ImmutableList.Builder<Integer>()
            .add(8).add(7).add(6).add(5).add(4).add(3).add(2).add(1).add(0)
            .add(35).add(34).add(33).add(32).add(31).add(30).add(29).add(28).add(27)
            .add(26).add(25).add(24).add(23).add(22).add(21).add(20).add(19).add(18)
            .add(17).add(16).add(15).add(14).add(13).add(12).add(10).add(9)
            .build();


    private void doFakeShift(Inventory menuTo, Inventory menuFrom, int slot, List<Integer> orderedSlots, int packSlot) {
        ItemStack clickedItem = menuFrom.getItem(slot);
        if (clickedItem != null) {
            int amountToRid = clickedItem.getAmount();
            int maxStack = clickedItem.getMaxStackSize();
            int index = 0;
            while (amountToRid > 0 && index < orderedSlots.size()) {
                if (orderedSlots.get(index) != packSlot) {
                    ItemStack itemStack = menuTo.getItem(orderedSlots.get(index));
                    if (itemStack != null && itemStack.getType() == clickedItem.getType() && itemStack.getItemMeta().equals(clickedItem.getItemMeta())) {
                        int canTake = maxStack - itemStack.getAmount();
                        int toAdd = canTake > amountToRid ? amountToRid : canTake;
                        amountToRid -= toAdd;
                        itemStack.setAmount(itemStack.getAmount() + toAdd);
                    }
                }
                index++;
            }
            //check again for null
            index = 0;
            while (amountToRid > 0 && index < orderedSlots.size()) {
                if (orderedSlots.get(index) != packSlot) {
                    ItemStack itemStack = menuTo.getItem(orderedSlots.get(index));
                    if (itemStack == null) {
                        ItemStack toSet = clickedItem.clone();
                        toSet.setAmount(amountToRid);
                        amountToRid -= amountToRid;
                        menuTo.setItem(orderedSlots.get(index), toSet);
                    }
                }
                index++;
            }

            ItemStack toSet = amountToRid > 0 ? clickedItem.clone() : new ItemStack(Material.AIR);
            if (amountToRid > 0) toSet.setAmount(amountToRid);
            menuFrom.setItem(slot, toSet);
        }
    }

    private void doFakeDouble(InventoryClickEvent e, InventoryView inventoryView, List<Integer> orderedSlots, int packSlot) {
        ItemStack clickedItem = e.getCursor();
        if (clickedItem != null) {
            int maxStack = clickedItem.getMaxStackSize();
            int amountTillStack = maxStack - clickedItem.getAmount();
            int index = 0;
            while (amountTillStack > 0 && index < orderedSlots.size()) {
                if (orderedSlots.get(index) != packSlot) {
                    ItemStack itemStack = inventoryView.getItem(orderedSlots.get(index));
                    if (itemStack != null && itemStack.getAmount() < maxStack && itemStack.getType() == clickedItem.getType() && itemStack.getItemMeta().equals(clickedItem.getItemMeta())) {
                        int canTake = itemStack.getAmount();
                        int toAdd = canTake > amountTillStack ? amountTillStack : canTake;
                        amountTillStack -= toAdd;
                        setAmount(inventoryView, itemStack, itemStack.getAmount() - toAdd, orderedSlots.get(index));
                    }
                }
                index++;
            }

            //check again and remove condition of itemstack amount not being max
            index = 0;
            while (amountTillStack > 0 && index < orderedSlots.size()) {
                if (orderedSlots.get(index) != packSlot) {
                    ItemStack itemStack = inventoryView.getItem(orderedSlots.get(index));
                    if (itemStack != null && itemStack.getType() == clickedItem.getType() && itemStack.getItemMeta().equals(clickedItem.getItemMeta())) {
                        int canTake = itemStack.getAmount();
                        int toAdd = canTake > amountTillStack ? amountTillStack : canTake;
                        amountTillStack -= toAdd;
                        setAmount(inventoryView, itemStack, itemStack.getAmount() - toAdd, orderedSlots.get(index));
                    }
                }

                index++;
            }


            ItemStack toSet = clickedItem.clone();
            toSet.setAmount(maxStack - amountTillStack);
            e.setCursor(toSet);
        }

    }

    @Deprecated
    public static void setAmount(InventoryView inventory, ItemStack itemStack, int amount, int slot) {
        if (Version.current.isNormalItemConsume()) {
            itemStack.setAmount(amount);
        } else {
            ItemStack toSet = itemStack.clone();
            toSet.setAmount(amount);
            inventory.setItem(slot, toSet);
        }
    }

    private boolean isMenuClick(Inventory clickedInventory, HumanEntity humanEntity) {
        return humanEntity.getOpenInventory().getTopInventory().equals(clickedInventory);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrag(InventoryDragEvent e) {
        if (LiveSessions.hasSession(e.getWhoClicked())) {
            Player player = (Player) e.getWhoClicked();
            SessionData sessionData = LiveSessions.getData(player);
            if (!sessionData.isUpdating()) {
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
            } else {
                e.setCancelled(true);
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
            if (player.getOpenInventory() != null) {
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
            }
            LiveSessions.end(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (LiveSessions.hasSession(player)) {
            SessionData sessionData = LiveSessions.getData(player);
            if (player.getOpenInventory() != null) {
                Inventory inventory = player.getOpenInventory().getTopInventory();
                if (sessionData instanceof IUpdateable && isMenuClick(inventory, player)) {
                    ((IUpdateable) sessionData).updateContents(inventory);
                }
            }
            LiveSessions.end(player);
        }
    }


}
