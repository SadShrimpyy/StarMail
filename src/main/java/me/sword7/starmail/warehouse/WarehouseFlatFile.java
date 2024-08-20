package me.sword7.starmail.warehouse;

import me.sword7.starmail.util.storage.StorageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WarehouseFlatFile {


    private static final File file = new File("plugins/StarMail/Data", "warehouse.yml");
    private static final FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    public void store(Map<String, WarehouseEntry> users) {
        for (Map.Entry<String, WarehouseEntry> entry : users.entrySet()) {
            try {
                String key = entry.getKey();
                WarehouseEntry warehouseEntry = entry.getValue();
                if (warehouseEntry != null) {
                    config.set(key + ".itemstack", warehouseEntry.getItemStack());
                    config.set(key + ".from", warehouseEntry.getFrom());
                } else {
                    config.set(key, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        StorageUtil.save(config, file);
    }

    public Map<String, WarehouseEntry> fetch() {
        Map<String, WarehouseEntry> entries = new HashMap<>();
        for (String name : config.getRoot().getKeys(false)) {
            try {
                String key = name;
                ItemStack itemStack = config.getItemStack(key + ".itemstack");
                String from = config.getString(key + ".from");
                entries.put(name, new WarehouseEntry(itemStack, from));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entries;
    }


}
