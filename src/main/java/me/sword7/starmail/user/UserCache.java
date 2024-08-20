package me.sword7.starmail.user;

import me.sword7.starmail.StarMail;
import me.sword7.starmail.box.BoxCache;
import me.sword7.starmail.post.PostCache;
import me.sword7.starmail.post.notifications.Notifications;
import me.sword7.starmail.sys.config.PluginConfig;
import me.sword7.starmail.util.Scheduler;
import me.sword7.starmail.util.storage.CallbackQuery;
import me.sword7.starmail.util.storage.Saves;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

import static me.sword7.starmail.sys.Language.INFO_MAIL;

public class UserCache implements Listener {

    private static final int MAX_RECENT_SIZE = 50;
    private static int recentSize = 0;
    private static LinkedList<UUID> recent = new LinkedList<>();

    private static Map<UUID, User> idToUser = new HashMap<>();
    private static Map<String, User> nameToUser = new HashMap<>();

    private static UserFlatFile userFlatFile = new UserFlatFile();
    private static Saves<UUID, User> saves = new Saves<>(userFlatFile);
    private static final Map<String, UUID> savesLookup = new HashMap<>();

    public UserCache() {
        Plugin plugin = StarMail.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        onEnable();
    }

    private void onEnable() {

        saves.setOnSaveFinalized(() -> {
            savesLookup.clear();
        });

        Set<UUID> toLoad = new HashSet<>();
        toLoad.addAll(BoxCache.getPlacedBoxUsers());
        for (Player player : Bukkit.getOnlinePlayers()) {
            toLoad.add(player.getUniqueId());
        }

        for (UUID userID : toLoad) {
            User user = userFlatFile.fetch(userID);
            if (user != null) {
                idToUser.put(userID, user);
                nameToUser.put(user.getNameKey(), user);
            }
        }

        BoxCache.initializeUsers(idToUser);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!idToUser.containsKey(player.getUniqueId())) {
                registerUser(player);
            }
        }

    }

    public static void shutdown() {
        saves.shutdown();
        recent.clear();
        idToUser.clear();
        nameToUser.clear();
    }

    public static User getCachedUser(UUID userID) {
        if (idToUser.containsKey(userID)) {
            return idToUser.get(userID);
        } else if (saves.containsKey(userID)) {
            return saves.get(userID);
        } else {
            return null;
        }
    }

    public static void getUser(UUID userID, CallbackQuery<User> callback) {
        if (idToUser.containsKey(userID)) {
            callback.onQueryDone(idToUser.get(userID));
            if (!recent.remove(userID)) recentSize++;
            recent.add(userID);
        } else if (saves.containsKey(userID)) {
            User user = saves.get(userID);
            cacheRecent(user);
            callback.onQueryDone(user);
        } else {
            userFlatFile.fetchAsync(userID, (User user) -> {
                if (user != null && !idToUser.containsKey(userID)) {
                    cacheRecent(user);
                }
                callback.onQueryDone(user);
            });
        }
    }

    private static void cacheRecent(User user) {
        if (user != null) {
            idToUser.put(user.getID(), user);
            nameToUser.put(user.getNameKey(), user);
            recentSize++;
            recent.offer(user.getID());
            purgeCache();
        }
    }

    private static void purgeCache() {
        if (recentSize > MAX_RECENT_SIZE) {
            for (int i = 0; i < 9; i++) {
                if (recent.size() > 0) {
                    UUID userID = recent.poll();
                    recentSize--;
                    User user = idToUser.get(userID);
                    if (!user.isOnline() && !BoxCache.isPlacedBoxUser(userID)) {
                        idToUser.remove(userID);
                        nameToUser.remove(user.getNameKey());
                    }
                }
            }
        }
    }

    public static void getUser(String userName, CallbackQuery<User> callback) {
        String lookupName = userName.toUpperCase();
        if (nameToUser.containsKey(lookupName)) {
            User user = nameToUser.get(lookupName);
            callback.onQueryDone(user);
            if (!recent.remove(user.getID())) recentSize++;
            recent.add(user.getID());
        } else if (savesLookup.containsKey(lookupName) && saves.containsKey(savesLookup.get(lookupName))) {
            User user = saves.get(savesLookup.get(lookupName));
            cacheRecent(user);
            callback.onQueryDone(user);
        } else {
            userFlatFile.fetchAsync(lookupName, (User user) -> {
                if (user != null && !nameToUser.containsKey(user.getNameKey())) {
                    cacheRecent(user);
                }
                callback.onQueryDone(user);
            });
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {

        final Player player = e.getPlayer();
        UUID playerID = player.getUniqueId();

        CallbackQuery<User> callback = (User user) -> {
            user.setOnline(true);
            user.clearPerms();
            if (!user.getName().equals(player.getName())) {
                user.setName(player.getName());
                nameToUser.remove(player.getName().toUpperCase());
                nameToUser.put(user.getNameKey(), user);
                save(user);
            }
            sendNotification(player, user);
        };

        if (idToUser.containsKey(playerID)) {
            callback.onQueryDone(idToUser.get(playerID));
        } else if (saves.containsKey(playerID)) {
            User user = saves.get(playerID);
            idToUser.put(user.getID(), user);
            nameToUser.put(user.getNameKey(), user);
            callback.onQueryDone(user);
        } else {
            userFlatFile.fetchAsync(playerID, (User loaded) -> {
                if (loaded != null) {
                    idToUser.put(loaded.getID(), loaded);
                    nameToUser.put(loaded.getNameKey(), loaded);
                    callback.onQueryDone(loaded);
                } else {
                    registerUser(player);
                }
            });
        }

    }

    private void sendNotification(Player player, User user) {
        if (player.isOnline()) {
            UUID playerID = player.getUniqueId();
            if (user.getNotifications().isOnJoin()) {
                if (PostCache.hasMail(playerID)) {
                    int mailItems = PostCache.getMail(playerID).size();
                    if (mailItems > 0) {
                        int delayTicks = PluginConfig.getOnJoinDelayTicks();
                        if (delayTicks > 0) {
                            Scheduler.runLater(() -> {
                                player.sendMessage(INFO_MAIL.fromAmount(mailItems));
                            }, delayTicks);
                        } else {
                            player.sendMessage(INFO_MAIL.fromAmount(mailItems));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        UUID userID = e.getPlayer().getUniqueId();
        User user = idToUser.get(userID);
        if (user != null) {
            if (!recent.remove(userID)) recentSize++;
            recent.add(userID);
            user.setOnline(false);
            user.setLastUsed();
            save(user);
        }
    }


    public static void registerUser(Player player) {
        UUID playerID = player.getUniqueId();
        User user = new User(playerID, player.getName(), new Notifications());
        user.setOnline(true);
        idToUser.put(playerID, user);
        nameToUser.put(player.getName().toUpperCase(), user);
        userFlatFile.register(user);
        save(user);
    }

    public static void touch(User user) {
        save(user);
    }

    private static void save(User user) {
        savesLookup.put(user.getNameKey(), user.getID());
        saves.commit(user.getID(), user);
    }


}
