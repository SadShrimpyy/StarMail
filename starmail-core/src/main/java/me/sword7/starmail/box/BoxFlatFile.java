package me.sword7.starmail.box;

import me.sword7.starmail.util.LocationParts;
import me.sword7.starmail.util.storage.SingleFileStorage;
import me.sword7.starmail.util.storage.StorageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoxFlatFile extends SingleFileStorage<LocationParts, PlacedBox> {

    private static File file = new File("plugins/StarMail/Data", "boxes.yml");

    @Override
    protected void store(final Map<LocationParts, PlacedBox> boxes) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (Map.Entry<LocationParts, PlacedBox> entry : boxes.entrySet()) {
            String locKey = entry.getKey().toString();
            if (entry.getValue() != null) {
                PlacedBox placedBox = entry.getValue();
                if (placedBox.isGlobal()) {
                    config.set(locKey, "global" + "=" + placedBox.getBox().getName());
                } else {
                    config.set(locKey, placedBox.getOwnerId().toString() + "=" + placedBox.getBox().getName());
                }
            } else {
                config.set(locKey, null);
            }
        }
        StorageUtil.save(config, file);
    }

    @Override
    protected Map<LocationParts, PlacedBox> fetchAll() {
        Map<LocationParts, PlacedBox> boxes = new HashMap<>();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String locStrings : config.getRoot().getKeys(false)) {
            try {
                String placedString = config.getString(locStrings);
                String[] parts = placedString.split("=");
                String idString = parts[0];
                Box box = Box.getBox(parts[1]);
                if (box != null) {
                    if (idString.equalsIgnoreCase("global")) {
                        boxes.put(StorageUtil.getLocationParts(locStrings), new PlacedBox(box));
                    } else {
                        boxes.put(StorageUtil.getLocationParts(locStrings), new PlacedBox(box, UUID.fromString(idString)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return boxes;
    }

}
