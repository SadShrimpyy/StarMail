package me.sword7.starmail.gui;

import me.sword7.starmail.box.Box;
import me.sword7.starmail.gui.data.*;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.pack.tracking.TrackingCache;
import me.sword7.starmail.postbox.Postbox;
import me.sword7.starmail.user.User;
import me.sword7.starmail.user.UserCache;
import me.sword7.starmail.warehouse.WarehouseEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class LiveSessions {

    private static HashMap<UUID, SessionData> playerToData = new HashMap<>();
    private static Map<String, String> entryToEditor = new HashMap<>();

    public static void shutdown() {
        playerToData.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.closeInventory();
        }
    }

    public static void launchBox(Player player, User boxOwner, Box box, Location location) {
        end(player);
        UUID playerID = player.getUniqueId();
        FBoxData fBoxData = new FBoxData(player, box, boxOwner, location);
        player.playSound(location, Box.getOpenSound(), 1f, 1.2f);
        playerToData.put(playerID, fBoxData);
        fBoxData.start();
    }

    public static void launchPostbox(Player player, Postbox postbox) {
        end(player);
        SessionData sessionData = new PostData(player, postbox);
        playerToData.put(player.getUniqueId(), sessionData);
        sessionData.start();
    }

    public static void launchWarehouse(Player player, String type, WarehouseEntry entry) {
        end(player);
        SessionData sessionData = new WarehouseData(player, type, entry);
        playerToData.put(player.getUniqueId(), sessionData);
        sessionData.start();
        entryToEditor.put(type, player.getName());
    }

    private static Set<UUID> busyPlayers = new HashSet<>();

    public static void launchMail(final Player player, final Box box, final Location location) {
        UUID playerID = player.getUniqueId();
        if (!busyPlayers.contains(playerID)) {
            busyPlayers.add(playerID);
            UserCache.getUser(playerID, (User user) -> {
                busyPlayers.remove(playerID);
                if (player.isOnline() && user != null) {
                    end(player);
                    player.playSound(location, Box.getOpenSound(), 1f, 1.2f);
                    SessionData sessionData = new BoxData(player, box, user, location);
                    playerToData.put(playerID, sessionData);
                    sessionData.start();
                }
            });
        }
    }

    public static void launchVirtualMail(Player player) {
        UUID playerID = player.getUniqueId();
        if (!busyPlayers.contains(playerID)) {
            busyPlayers.add(playerID);
            UserCache.getUser(playerID, (User user) -> {
                busyPlayers.remove(playerID);
                if (player.isOnline() && user != null) {
                    end(player);
                    SessionData sessionData = new BoxData(player, user);
                    playerToData.put(playerID, sessionData);
                    sessionData.start();
                }
            });
        }

    }

    public static void launchVirtualMail(Player player, User user) {
        end(player);
        SessionData sessionData = new BoxData(player, user);
        playerToData.put(player.getUniqueId(), sessionData);
        sessionData.start();
    }


    public static void launchEmptyPackage(Player player, Pack pack, int slot, ItemStack packItem) {
        end(player);
        pack.playOpenSound(player);
        UUID playerID = player.getUniqueId();
        SessionData sessionData = new EmptyData(player, pack, packItem, slot);
        playerToData.put(playerID, sessionData);
        sessionData.start();
    }

    public static void launchSealedPackage(Player player, Pack pack, UUID trackingNo, int slot) {
        end(player);
        UUID playerID = player.getUniqueId();
        ItemStack[] contents = new ItemStack[21];
        boolean expired = true;
        if (TrackingCache.isTracked(trackingNo)) {
            contents = TrackingCache.getContents(trackingNo).clone();
            expired = false;
        }
        SessionData sessionData = new SealedData(player, pack.getOpener().getPage(), trackingNo, contents, pack, slot, expired);
        playerToData.put(playerID, sessionData);
        sessionData.start();
    }

    public static void end(Player player) {
        if (hasSession(player)) {
            SessionData sessionData = getData(player);
            playerToData.remove(player.getUniqueId());
            player.closeInventory();
            sessionData.onEnd();
        }
    }

    public static boolean hasSession(HumanEntity player) {
        return playerToData.containsKey(player.getUniqueId());
    }

    public static SessionData getData(Player player) {
        return playerToData.get(player.getUniqueId());
    }

    public static void removeEntry(String type) {
        entryToEditor.remove(type);
    }

    public static String getEntryEditor(String type) {
        return entryToEditor.get(type);
    }

}
