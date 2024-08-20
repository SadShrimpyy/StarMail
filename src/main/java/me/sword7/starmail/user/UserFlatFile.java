package me.sword7.starmail.user;

import me.sword7.starmail.post.notifications.Notifications;
import me.sword7.starmail.util.storage.CallbackQuery;
import me.sword7.starmail.util.storage.MultiFileStorage;
import me.sword7.starmail.util.storage.StorageUtil;
import me.sword7.starmail.sys.config.PluginConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserFlatFile extends MultiFileStorage<UUID, User> {

    private static final File DIR = new File("plugins/StarMail/Data/Users");
    private static final File lookupFile = new File("plugins/StarMail/Data/Users/lookup.yml");

    public UserFlatFile() {
        convertLegacy();
    }

    @Override
    protected void store(Map<UUID, User> users) {
        for (Map.Entry<UUID, User> entry : users.entrySet()) {
            UUID userID = entry.getKey();
            File file = new File(DIR, userID.toString() + ".yml");
            FileConfiguration config = new YamlConfiguration();
            User user = entry.getValue();
            if (user != null && !StorageUtil.isExpired(user.getLastUsed(), PluginConfig.getInactiveUserExpirationDays())) {
                config.set("name", user.getName());
                Notifications notifications = user.getNotifications();
                String notificationKey = "notifications";
                config.set(notificationKey + ".on join", notifications.isOnJoin());
                config.set(notificationKey + ".on receive", notifications.isOnReceive());
                config.set("last used", user.getLastUsed());
                StorageUtil.save(config, file);
            } else {
                file.delete();
            }
        }
    }

    @Override
    protected User fetch(UUID userID) {
        File file = new File(DIR, userID.toString() + ".yml");
        if (file.exists()) {
            try {
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                String name = config.getString("name");
                String notificationKey = "notifications";
                boolean onJoin = config.getBoolean(notificationKey + ".on join", true);
                boolean onReceive = config.getBoolean(notificationKey + ".on receive", true);
                return new User(userID, name, new Notifications(onJoin, onReceive));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public void register(User user) {
        FileConfiguration lookupConfig = YamlConfiguration.loadConfiguration(lookupFile);
        lookupConfig.set(user.getNameKey(), user.getID().toString());
        StorageUtil.save(lookupConfig, lookupFile);
    }

    public void fetchAsync(String name, CallbackQuery<User> callback) {
        FileConfiguration lookupConfig = YamlConfiguration.loadConfiguration(lookupFile);
        try {
            UUID userID = UUID.fromString(lookupConfig.getString(name));
            specialFetchAsync(() -> {
                return fetch(userID);
            }, callback);
        } catch (Exception e) {
            callback.onQueryDone(null);
        }
    }

    private static final File legacyFile = new File("plugins/StarMail/Data", "users.yml");

    public void convertLegacy() {
        if (legacyFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(legacyFile);
            Map<UUID, User> users = new HashMap<>();
            for (String userIDString : config.getRoot().getKeys(false)) {
                try {
                    UUID userID = UUID.fromString(userIDString);
                    String value = config.getString(userIDString);

                    String userName = value;
                    boolean onJoin = true;
                    boolean onReceive = true;
                    if (value.contains("=")) {
                        String[] parts = value.split("=");
                        userName = parts[0];
                        String[] notificationParts = parts[1].split("-");
                        onJoin = Boolean.valueOf(notificationParts[0]);
                        onReceive = Boolean.valueOf(notificationParts[1]);
                    }
                    User user = new User(userID, userName, new Notifications(onJoin, onReceive));
                    users.put(userID, user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            FileConfiguration lookupConfig = YamlConfiguration.loadConfiguration(lookupFile);
            for (User user : users.values()) {
                lookupConfig.set(user.getNameKey(), user.getID().toString());
            }
            StorageUtil.save(lookupConfig, lookupFile);
            store(users);
            legacyFile.delete();
        }
    }

}
