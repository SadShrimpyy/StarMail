package me.sword7.starmail.pack.tracking;

import me.sword7.starmail.util.storage.StorageUtil;
import me.sword7.starmail.sys.config.PluginConfig;
import me.sword7.starmail.warehouse.WarehouseCache;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrackingFlatFile {


    private static final File DIR = new File("plugins/StarMail/Data/Tracking");

    public void store(Map<UUID, TrackedItem> orders) {
        for (Map.Entry<UUID, TrackedItem> entry : orders.entrySet()) {
            UUID trackingNo = entry.getKey();
            File file = new File(DIR, trackingNo.toString() + ".yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            TrackedItem trackedItem = entry.getValue();
            if (trackedItem != null) {
                config.set("packaged", trackedItem.getDate().toString());
                ItemStack[] itemStacks = trackedItem.getItemStacks();
                for (int i = 0; i < 21; i++) {
                    if (itemStacks.length > i) {
                        ItemStack itemStack = itemStacks[i];
                        if (itemStack != null) {
                            config.set("contents." + i, itemStack);
                        }
                    }
                }
                StorageUtil.save(config, file);
            } else {
                file.delete();
            }
        }
    }

    public Map<UUID, TrackedItem> fetch() {
        Map<UUID, TrackedItem> orders = new HashMap<>();
        if (DIR.exists()) {
            for (File file : StorageUtil.getYmlFiles(DIR)) {
                try {
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                    UUID trackingNo = UUID.fromString(file.getName().split("\\.")[0]);
                    Timestamp date = Timestamp.valueOf(config.getString("packaged"));
                    ItemStack[] itemStacks = new ItemStack[21];
                    if (config.contains("contents")) {
                        for (String indexString : config.getConfigurationSection("contents").getKeys(false)) {
                            ItemStack itemStack = config.getItemStack("contents." + indexString);
                            int index = Integer.parseInt(indexString);
                            itemStacks[index] = itemStack;
                        }
                    }
                    if (!StorageUtil.isExpired(date, PluginConfig.getPackageExpirationDays()) || WarehouseCache.isWarehousePack(trackingNo)) {
                        orders.put(trackingNo, new TrackedItem(itemStacks, date));
                    } else {
                        file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return orders;
    }


}
