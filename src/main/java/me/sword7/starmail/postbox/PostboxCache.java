package me.sword7.starmail.postbox;

import me.sword7.starmail.StarMail;
import me.sword7.starmail.sys.PluginBase;
import me.sword7.starmail.util.Dynmap;
import me.sword7.starmail.util.LocationParts;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostboxCache implements Listener {

    private static List<LocationParts> unloadedLocations = new ArrayList<>();
    private static Map<LocationParts, Postbox> postBoxLocations = new HashMap<>();
    private static boolean usingDynmap = PluginBase.isUsingDynmap();
    private static Dynmap dynmap = PluginBase.getDynmap();

    private static PostboxFlatFile postboxFlatFile = new PostboxFlatFile();

    public PostboxCache() {
        Plugin plugin = StarMail.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        onEnable();
    }

    private void onEnable() {
        for (Map.Entry<LocationParts, Postbox> entry : postboxFlatFile.fetch().entrySet()) {
            LocationParts locationParts = entry.getKey();
            Location location = locationParts.toLocation();
            if (location == null) unloadedLocations.add(locationParts);
            if (isPostboxAt(location) || location == null) {
                Postbox postbox = entry.getValue();
                if (usingDynmap) dynmap.registerPostboxAt(locationParts, postbox);
                postBoxLocations.put(locationParts, postbox);
            }
        }
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
                if (postBoxLocations.containsKey(parts)) {
                    Location location = parts.toLocation();
                    if (!isPostboxAt(location)) {
                        postBoxLocations.remove(parts);
                    }
                }
            }
        }
    }

    private boolean isPostboxAt(Location location) {
        if (location != null && location.getWorld() != null) {
            return Postbox.getPostbox(location.getBlock().getState()) != null;
        }
        return false;
    }

    public static boolean hasPostbox(Location location) {
        return postBoxLocations.containsKey(new LocationParts(location));
    }

    public static void shutdown() {
        postboxFlatFile.store(postBoxLocations);
    }

    public static void register(Location location, Postbox postbox) {
        LocationParts locationParts = new LocationParts(location);
        postBoxLocations.put(locationParts, postbox);
        if (usingDynmap) dynmap.registerPostboxAt(locationParts, postbox);
    }

    public static void unRegister(Location location) {
        LocationParts locationParts = new LocationParts(location);
        postBoxLocations.remove(locationParts);
        if (usingDynmap) dynmap.deletePostboxAt(locationParts);
    }


}
