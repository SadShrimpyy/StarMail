package me.sword7.starmail.postbox;

import me.sword7.starmail.util.LocationParts;
import me.sword7.starmail.util.storage.StorageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PostboxFlatFile {

    private static File file = new File("plugins/StarMail/Data", "postboxes.yml");

    public void store(Map<LocationParts, Postbox> locations) {
        FileConfiguration config = new YamlConfiguration();
        for (Map.Entry<LocationParts, Postbox> entry : locations.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue().getName());
        }
        StorageUtil.save(config, file);
    }

    public Map<LocationParts, Postbox> fetch() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        Map<LocationParts, Postbox> postboxes = new HashMap<>();
        for (String locString : config.getRoot().getKeys(false)) {
            try {
                Postbox postbox = Postbox.getPostbox(config.getString(locString));
                if (postbox != null) postboxes.put(StorageUtil.getLocationParts(locString), postbox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return postboxes;
    }

}
