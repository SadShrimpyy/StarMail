package me.sword7.starmail.post.notifications;

import me.sword7.starmail.user.User;
import me.sword7.starmail.user.UserCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Alerts {

    private static Map<UUID, Alert> playerToAlert = new HashMap<>();

    protected static void unRegisterAlert(Player player) {
        playerToAlert.remove(player.getUniqueId());
    }

    protected static void registerAlert(Player player, Alert alert) {
        playerToAlert.put(player.getUniqueId(), alert);
    }

    public static void onReceive(UUID playerID, String sender) {

        User user = UserCache.getCachedUser(playerID);
        if (user != null) {
            Player player = Bukkit.getPlayer(playerID);
            Notifications notifications = user.getNotifications();
            if (player != null && notifications.isOnReceive()) {
                Alert alert = playerToAlert.get(player.getUniqueId());
                if (alert != null) {
                    alert.onExtend();
                } else {
                    new Alert(player, sender, notifications);
                }
            }
        }

    }


}
