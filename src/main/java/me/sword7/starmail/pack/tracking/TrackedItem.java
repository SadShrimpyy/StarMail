package me.sword7.starmail.pack.tracking;

import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.time.Instant;

public class TrackedItem {

    private ItemStack[] itemStacks;
    private Timestamp date;

    public TrackedItem(ItemStack[] itemStacks) {
        this.itemStacks = itemStacks;
        this.date = Timestamp.from(Instant.now());
    }

    public TrackedItem(ItemStack[] itemStacks, Timestamp date) {
        this.itemStacks = itemStacks;
        this.date = date;
    }

    public Timestamp getDate() {
        return date;
    }

    public ItemStack[] getItemStacks() {
        return itemStacks;
    }

    public void setItemStacks(ItemStack[] itemStacks) {
        this.itemStacks = itemStacks;
    }

}
