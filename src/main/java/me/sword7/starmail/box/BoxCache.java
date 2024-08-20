package me.sword7.starmail.box;

import me.sword7.starmail.StarMail;
import me.sword7.starmail.sys.PluginBase;
import me.sword7.starmail.user.User;
import me.sword7.starmail.user.UserCache;
import me.sword7.starmail.util.Dynmap;
import me.sword7.starmail.util.LocationParts;
import me.sword7.starmail.util.storage.Saves;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class BoxCache implements Listener {

    private static List<LocationParts> unloadedLocations = new ArrayList<>();
    private static List<LocationParts> requestedBreaks = new ArrayList<>();

    private static Set<UUID> placedBoxPlayers = new HashSet<>();
    private static Map<LocationParts, PlacedBox> locationToBox = new HashMap<>();
    private static Dynmap dynmap = PluginBase.getDynmap();
    private static boolean usingDynmap = PluginBase.isUsingDynmap();

    private static BoxFlatFile boxFlatFile = new BoxFlatFile();
    private static Saves<LocationParts, PlacedBox> saves = new Saves<>(boxFlatFile);

    public BoxCache() {
        Plugin plugin = StarMail.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        onEnable();
    }

    private void onEnable() {
        boxFlatFile.fetchAllSync((Map<LocationParts, PlacedBox> boxes) -> {
            for (Map.Entry<LocationParts, PlacedBox> entry : boxes.entrySet()) {
                LocationParts locationParts = entry.getKey();
                Location location = locationParts.toLocation();
                if (location == null) unloadedLocations.add(locationParts);
                if (isBoxAt(location) || location == null) {
                    PlacedBox placedBox = entry.getValue();
                    locationToBox.put(locationParts, placedBox);
                    if (!placedBox.isGlobal()) {
                        placedBoxPlayers.add(placedBox.getOwnerId());
                    } else {
                        if (usingDynmap) dynmap.registerGlobalBoxAt(locationParts, placedBox);
                    }
                } else {
                    saves.add(locationParts, null);
                }
            }
            saves.commit();
        });
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        World world = e.getWorld();
        String worldName = world.getName();
        for (int i = unloadedLocations.size() - 1; i >= 0; i--) {
            LocationParts parts = unloadedLocations.get(i);
            if (parts.getWorldName().equals(worldName)) {
                parts.initialize(world);
                unloadedLocations.remove(parts);
                if (locationToBox.containsKey(parts)) {
                    Location location = parts.toLocation();
                    if (!isBoxAt(location)) {
                        locationToBox.remove(parts);
                        saves.add(parts, null);
                    }
                }
            }
        }
        saves.commit();
        for (int i = requestedBreaks.size() - 1; i >= 0; i--) {
            LocationParts parts = requestedBreaks.get(i);
            if (parts.getWorldName().equals(worldName)) {
                parts.initialize(world);
                requestedBreaks.remove(parts);
                CommandBreakBoxes.dropBox(parts.toLocation());
            }
        }
    }

    private boolean isBoxAt(Location location) {
        if (location != null && location.getWorld() != null) {
            return Box.getBox(location.getBlock().getState()) != null;
        }
        return false;
    }

    public static void requestBreak(LocationParts locationParts) {
        requestedBreaks.add(locationParts);
    }

    public static void shutdown() {
        saves.shutdown();
        locationToBox.clear();
        placedBoxPlayers.clear();
    }

    public static void registerBox(Location location, User user, Box box) {
        LocationParts locationParts = new LocationParts(location);
        UUID playerID = user.getID();
        placedBoxPlayers.add(playerID);
        PlacedBox placedBox = new PlacedBox(box, playerID);
        user.incrementPlacedBoxes();
        if (usingDynmap) dynmap.registerPlayerBoxAt(locationParts, user.getName(), placedBox);
        locationToBox.put(locationParts, placedBox);
        saves.commit(locationParts, placedBox);
    }

    public static void registerBox(Location location, Box box) {
        LocationParts locationParts = new LocationParts(location);
        PlacedBox placedBox = new PlacedBox(box);
        if (usingDynmap) dynmap.registerGlobalBoxAt(locationParts, placedBox);
        locationToBox.put(locationParts, placedBox);
        saves.commit(locationParts, placedBox);
    }

    public static void unRegister(Location location) {
        unRegister(new LocationParts(location));
    }

    public static void unRegister(LocationParts locationParts) {
        if (locationToBox.containsKey(locationParts)) {
            PlacedBox placedBox = locationToBox.get(locationParts);
            if (!placedBox.isGlobal()) {
                UUID playerID = placedBox.getOwnerId();
                User user = UserCache.getCachedUser(playerID);
                if (user != null) user.decrementPlacedBoxes();
            }
            if (usingDynmap) dynmap.deleteBoxAt(locationParts);
            locationToBox.remove(locationParts);
            saves.commit(locationParts, null);
        }
    }

    public static boolean hasBox(Location location) {
        return locationToBox.containsKey(new LocationParts(location));
    }

    public static PlacedBox getPlacedBox(Location location) {
        return locationToBox.get(new LocationParts(location));
    }

    public static Map<LocationParts, PlacedBox> getRelevantBoxes(UUID playerID) {
        Map<LocationParts, PlacedBox> placedBoxes = new HashMap<>();
        for (Map.Entry<LocationParts, PlacedBox> entry : locationToBox.entrySet()) {
            if (playerID.equals(entry.getValue().getOwnerId())) {
                placedBoxes.put(entry.getKey(), entry.getValue());
            }
        }
        return placedBoxes;
    }

    public static List<LocationParts> getBoxLocations(UUID playerID) {
        List<LocationParts> placements = new ArrayList<>();
        for (Map.Entry<LocationParts, PlacedBox> entry : locationToBox.entrySet()) {
            if (playerID.equals(entry.getValue().getOwnerId())) {
                placements.add(entry.getKey());
            }
        }
        return placements;
    }

    public static void initializeUsers(Map<UUID, User> users) {
        for (Map.Entry<LocationParts, PlacedBox> entry : locationToBox.entrySet()) {
            PlacedBox placedBox = entry.getValue();
            UUID placerID = placedBox.getOwnerId();
            if (users.containsKey(placerID)) {
                users.get(placerID).incrementPlacedBoxes();
                if (usingDynmap) dynmap.registerPlayerBoxAt(entry.getKey(), users.get(placerID).getName(), placedBox);
            }
        }
    }

    public static boolean isPlacedBoxUser(UUID userID) {
        return placedBoxPlayers.contains(userID);
    }

    public static Collection<UUID> getPlacedBoxUsers() {
        return placedBoxPlayers;
    }

}
