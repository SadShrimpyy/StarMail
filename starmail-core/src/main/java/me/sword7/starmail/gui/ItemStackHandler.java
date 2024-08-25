package me.sword7.starmail.gui;

import com.avaje.ebean.validation.NotNull;
import com.google.common.collect.ImmutableList;
import me.sword7.starmail.sys.Version;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

class ItemStackHandler {

    private final List<Integer> ShiftOrderSlots = new ImmutableList.Builder<Integer>()
            .add(8).add(7).add(6).add(5).add(4).add(3).add(2).add(1).add(0)
            .add(35).add(34).add(33).add(32).add(31).add(30).add(29).add(28).add(27)
            .add(26).add(25).add(24).add(23).add(22).add(21).add(20).add(19).add(18)
            .add(17).add(16).add(15).add(14).add(13).add(12).add(10).add(9)
            .build();

    void fillExistingStacks(Inventory menuTo, ItemStack clickedItem, List<Integer> orderedSlots, int packSlot, int maxStack, int amountToRid) {
        for (int index = 0; amountToRid > 0 && index < orderedSlots.size(); index++) {
            int slotIndex = orderedSlots.get(index);
            if (slotIndex == packSlot) continue;
            ItemStack itemStack = menuTo.getItem(slotIndex);
            if (isSameItem(clickedItem, itemStack)) {
                int canTake = maxStack - itemStack.getAmount();
                int toAdd = Math.min(canTake, amountToRid);
                amountToRid -= toAdd;
                itemStack.setAmount(itemStack.getAmount() + toAdd);
            }
        }
    }

    void createNewStacks(Inventory menuTo, ItemStack clickedItem, List<Integer> orderedSlots, int packSlot, int amountToRid) {
        for (int index = 0; amountToRid > 0 && index < orderedSlots.size(); index++) {
            int slotIndex = orderedSlots.get(index);
            if (slotIndex == packSlot) continue;
            ItemStack itemStack = menuTo.getItem(slotIndex);
            if (itemStack != null) continue;
            ItemStack toSet = clickedItem.clone();
            toSet.setAmount(amountToRid);
            amountToRid -= amountToRid;
            menuTo.setItem(slotIndex, toSet);
        }
    }

    public void removeStackFromInventory(PlayerInventory inventory, int slot) {
        inventory.getItem(slot).setAmount(0);
    }

    public void doFakeShift(Inventory menuTo, Inventory menuFrom, int slot, List<Integer> orderedSlots, int packSlot) {
        if (orderedSlots == null) orderedSlots = ShiftOrderSlots;
        ItemStack clickedItem = menuFrom.getItem(slot);
        if (clickedItem == null) return;
        int amountToRid = clickedItem.getAmount();
        int maxStack = clickedItem.getMaxStackSize();

        fillExistingStacks(menuTo, clickedItem, orderedSlots, packSlot, maxStack, amountToRid);
        createNewStacks(menuTo, clickedItem, orderedSlots, packSlot, amountToRid);
    }

    @Deprecated
    public void doFakeDouble(InventoryClickEvent e, InventoryView inventoryView, List<Integer> orderedSlots, int packSlot) {
        ItemStack clickedItem = e.getCursor();
        if (clickedItem == null) return;

        int maxStack = clickedItem.getMaxStackSize();
        int amountTillStack = maxStack - clickedItem.getAmount();

        amountTillStack = handleOrderedSlots(amountTillStack, orderedSlots, packSlot, inventoryView, clickedItem, false);
        amountTillStack = handleOrderedSlots(amountTillStack, orderedSlots, packSlot, inventoryView, clickedItem, true);

        ItemStack toSet = clickedItem.clone();
        toSet.setAmount(maxStack - amountTillStack);
        e.setCursor(toSet);
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

    @Deprecated
    private void updateItemAmountInInventory(InventoryView inventory, ItemStack itemStack, int amount, int slot) {
        if (Version.current.isNormalItemConsume()) {
            itemStack.setAmount(amount);
        } else {
            ItemStack toSet = itemStack.clone();
            toSet.setAmount(amount);
            inventory.setItem(slot, toSet);
        }
    }

    private int handleOrderedSlots(int amountTillStack, List<Integer> orderedSlots, int packSlot, InventoryView inventoryView,
                                   ItemStack clickedItem, boolean ignoreAmount) {
        for (int index = 0; amountTillStack > 0 && index < orderedSlots.size(); index++) {
            if (canSkipSlot(orderedSlots, packSlot, index)) continue;

            ItemStack itemStack = inventoryView.getItem(orderedSlots.get(index));
            if (shouldTransferAmount(itemStack, clickedItem, ignoreAmount)) {
                int canTake = itemStack.getAmount();
                int toAdd = Math.min(canTake, amountTillStack);
                amountTillStack -= toAdd;
                updateItemAmountInInventory(inventoryView, itemStack, itemStack.getAmount() - toAdd, orderedSlots.get(index));
            }
        }
        return amountTillStack;
    }

    private boolean isSameItem(ItemStack source, ItemStack target) {
        return target != null && source.getType() == target.getType() && source.getItemMeta().equals(target.getItemMeta());
    }

    private boolean canSkipSlot(List<Integer> orderedSlots, int packSlot, int index) {
        return orderedSlots.get(index) == packSlot;
    }

    private boolean shouldTransferAmount(ItemStack currentStack, ItemStack clickedItem, boolean ignoreAmount) {
        return currentStack != null &&
                (!ignoreAmount ? currentStack.getAmount() < clickedItem.getMaxStackSize() : true) &&
                currentStack.getType() == clickedItem.getType() &&
                Objects.requireNonNull(currentStack.getItemMeta()).equals(clickedItem.getItemMeta());
    }
}
