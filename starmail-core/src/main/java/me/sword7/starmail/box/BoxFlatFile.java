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
            if (entry.getValue() == null) config.set(entry.getKey().toString(), null);
            if (entry.getValue().getOwnerId() == null) continue;
            PlacedBox placedBox = entry.getValue();
            StringBuilder value = placedBox.isGlobal()
                    ? new StringBuilder("global")
                    : new StringBuilder(placedBox.getOwnerId().toString());
            config.set(entry.getKey().toString(), value.append("=").append(placedBox.getBox().getName()).toString());
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
