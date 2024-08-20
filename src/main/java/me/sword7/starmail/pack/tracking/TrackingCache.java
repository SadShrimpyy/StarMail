package me.sword7.starmail.pack.tracking;

import me.sword7.starmail.util.Scheduler;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrackingCache {

    private static Map<UUID, TrackedItem> packageToContents = new HashMap<>();
    private static Map<UUID, TrackedItem> unsavedPackages = new HashMap<>();

    private static TrackingFlatFile trackingFlatFile = new TrackingFlatFile();

    public TrackingCache() {
        packageToContents.putAll(trackingFlatFile.fetch());
    }

    public static void save() {
        Map<UUID, TrackedItem> unsaved = new HashMap<>();
        unsaved.putAll(unsavedPackages);
        unsavedPackages.clear();
        Scheduler.runAsync(() -> {
            trackingFlatFile.store(unsaved);
            unsaved.clear();
        });
    }


    public static void saveSync() {
        trackingFlatFile.store(unsavedPackages);
        unsavedPackages.clear();
    }

    public static void shutdown() {
        saveSync();
        packageToContents.clear();
    }


    public static boolean isTracked(UUID trackingNo) {
        return packageToContents.containsKey(trackingNo);
    }

    public static ItemStack[] getContents(UUID trackingNo) {
        return packageToContents.get(trackingNo).getItemStacks();
    }

    public static UUID track(ItemStack[] contents) {
        UUID trackingNo = UUID.randomUUID();
        TrackedItem trackedItem = new TrackedItem(contents);
        packageToContents.put(trackingNo, trackedItem);
        unsavedPackages.put(trackingNo, trackedItem);
        save();
        return trackingNo;
    }

    public static void unTrack(UUID trackingNo) {
        packageToContents.remove(trackingNo);
        unsavedPackages.put(trackingNo, null);
        save();
    }


    public static void unTrackSync(UUID trackingNo) {
        packageToContents.remove(trackingNo);
        unsavedPackages.put(trackingNo, null);
        saveSync();
    }

    public static void updateContents(UUID trackingNo, ItemStack[] contents) {
        TrackedItem trackedItem = packageToContents.get(trackingNo);
        if (trackedItem != null) {
            trackedItem.setItemStacks(contents);
            unsavedPackages.put(trackingNo, trackedItem);
            save();
        }
    }


}
