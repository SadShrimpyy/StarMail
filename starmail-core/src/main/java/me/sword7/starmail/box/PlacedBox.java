package me.sword7.starmail.box;

import me.sword7.starmail.util.storage.ICopyable;

import java.util.UUID;

public class PlacedBox implements ICopyable<PlacedBox> {

    private Box box;
    private UUID ownerId;
    private boolean global;

    public PlacedBox(Box box, UUID ownerId) {
        this.box = box;
        this.ownerId = ownerId;
        this.global = false;
    }

    public PlacedBox(Box box) {
        this.box = box;
        this.global = true;
    }

    public Box getBox() {
        return box;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public boolean isGlobal() {
        return global;
    }


    @Override
    public PlacedBox copy() {
        return new PlacedBox(box, ownerId);
    }
}
