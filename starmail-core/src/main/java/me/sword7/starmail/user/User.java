package me.sword7.starmail.user;

import me.sword7.starmail.post.notifications.Notifications;
import me.sword7.starmail.util.storage.ICopyable;
import me.sword7.starmail.sys.Permissions;
import me.sword7.starmail.util.Scheduler;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class User implements ICopyable<User> {

    private UUID ID;
    private String name;
    private Notifications notifications;
    private Timestamp lastUsed;
    private int placedBoxes = 0;
    private Integer maxBoxes;
    private Integer sendCooldown;
    private boolean online;
    private boolean waitingForPermData = true;

    public User(UUID ID, String name, Notifications notifications) {
        this.ID = ID;
        this.name = name;
        this.notifications = notifications;
        this.lastUsed = Timestamp.from(Instant.now());
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public UUID getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getNameKey() {
        return name.toUpperCase();
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlacedBoxes() {
        return placedBoxes;
    }

    public void incrementPlacedBoxes() {
        placedBoxes++;
    }

    public void decrementPlacedBoxes() {
        placedBoxes--;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public int getSendCooldown(Player player) {
        if (waitingForPermData) {
            return Permissions.getSendCooldown(player);
        } else if (sendCooldown == null) {
            sendCooldown = Permissions.getSendCooldown(player);
        }
        return sendCooldown;
    }

    public int getMaxBoxes(Player player) {
        if (waitingForPermData) {
            return Permissions.getMaxBoxes(player);
        } else if (maxBoxes == null) {
            maxBoxes = Permissions.getMaxBoxes(player);
        }
        return maxBoxes;
    }

    public void clearPerms() {
        sendCooldown = null;
        maxBoxes = null;
        Scheduler.runLater(() -> {
            waitingForPermData = false;
        }, 10);
    }

    public Timestamp getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed() {
        this.lastUsed = Timestamp.from(Instant.now());
    }

    @Override
    public User copy() {
        return new User(ID, name, new Notifications(notifications.isOnJoin(), notifications.isOnReceive()));
    }
}
