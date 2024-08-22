package me.sword7.starmail.warehouse;

import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.pack.tracking.TrackingCache;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.Scheduler;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class WarehouseCache {

    private static WarehouseFlatFile warehouseFlatFile = new WarehouseFlatFile();
    private static Map<String, WarehouseEntry> entries = new HashMap<>();
    private static Map<String, WarehouseEntry> unsavedData = new HashMap<>();
    private static Set<UUID> warehousePacks = new HashSet<>();

    public WarehouseCache() {
        load();
    }

    private void load() {
        entries.putAll(warehouseFlatFile.fetch());
        for (WarehouseEntry entry : entries.values()) {
            ItemStack itemStack = entry.getItemStack();
            if (Pack.isSealedPack(itemStack)) {
                warehousePacks.add(Pack.getTrackingNo(itemStack));
            }
        }
    }

    public static void shutdown() {
        warehouseFlatFile.store(unsavedData);
        unsavedData.clear();
        entries.clear();
    }

    public static void save() {
        Map<String, WarehouseEntry> unsaved = new HashMap<>();
        unsaved.putAll(unsavedData);
        unsavedData.clear();
        Scheduler.runAsync(() -> {
            warehouseFlatFile.store(unsaved);
            unsavedData.clear();
        });
    }

    public static boolean hasEntry(String string) {
        return entries.containsKey(string.toUpperCase());
    }

    public static WarehouseEntry getEntry(String string) {
        return entries.get(string.toUpperCase());
    }

    public static void register(WarehouseEntry warehouseEntry) {
        register(getDefaultString(), warehouseEntry);
    }

    private static String getDefaultString() {
        String base = Language.ARG_MAIL.toString().toUpperCase() + "_";
        int num = 1;
        String attempt = base + num;
        while (entries.containsKey(attempt)) {
            num++;
            attempt = base + num;
        }
        return attempt;
    }

    public static void register(String name, WarehouseEntry warehouseEntry) {
        entries.put(name, warehouseEntry);
        unsavedData.put(name, warehouseEntry);

        ItemStack itemStack = warehouseEntry.getItemStack();
        if (Pack.isSealedPack(itemStack)) {
            UUID trackingNo = Pack.getTrackingNo(itemStack);
            warehousePacks.add(trackingNo);
        }

        save();
    }

    public static void delete(String name) {
        if (entries.containsKey(name)) {
            WarehouseEntry entry = entries.get(name);
            entries.remove(name);
            ItemStack itemStack = entry.getItemStack();
            if (Pack.isSealedPack(itemStack)) {
                UUID trackingNo = Pack.getTrackingNo(itemStack);
                if (!isProtectedNo(trackingNo)) {
                    warehousePacks.remove(trackingNo);
                    TrackingCache.unTrack(trackingNo);
                }
            }
        }
        unsavedData.put(name, null);
        save();
    }

    public static boolean isProtectedNo(UUID trackingNo) {
        for (WarehouseEntry e : entries.values()) {
            ItemStack eStack = e.getItemStack();
            if (Pack.isSealedPack(eStack) && Pack.getTrackingNo(eStack).equals(trackingNo)) {
                return true;
            }
        }
        return false;
    }

    public static void registerEdit(String name, WarehouseEntry warehouseEntry) {
        unsavedData.put(name, warehouseEntry);
        save();
    }

    public static List<String> getEntryTypes() {
        List<String> types = new ArrayList<>();
        for (String type : entries.keySet()) {
            types.add(type);
        }
        return types;
    }

    public static void registerWarehousePack(UUID trackingNo) {
        warehousePacks.add(trackingNo);
    }

    public static boolean isWarehousePack(UUID trackingNo) {
        return warehousePacks.contains(trackingNo);
    }


}
