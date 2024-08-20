package me.sword7.starmail.util.storage;

import me.sword7.starmail.util.LocationParts;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class StorageUtil {


    public static boolean isExpired(Timestamp date, int maxDays) {
        if (maxDays >= 0) {
            int maxSeconds = 86400 * maxDays;
            long seconds = ChronoUnit.SECONDS.between(date.toInstant(), Instant.now());
            return (seconds > maxSeconds);
        } else {
            return false;
        }
    }

    public static List<File> getYmlFiles(File folder) {
        List<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory() && getExtensionOfFile(fileEntry).equals("yml")) {
                files.add(fileEntry);
            }
        }
        return files;
    }

    public static String getExtensionOfFile(File file) {
        String fileExtension = "";
        String fileName = file.getName();
        if (fileName.contains(".") && fileName.lastIndexOf(".") != 0) {
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return fileExtension;
    }

    public static void save(FileConfiguration config, File file) {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getString(Location location) {
        String storageString = location.getWorld().getName();
        storageString += ("_" + location.getBlockX());
        storageString += ("_" + location.getBlockY());
        storageString += ("_" + location.getBlockZ());
        return storageString;
    }


    public static String getString(LocationParts locationParts) {
        String storageString = locationParts.getWorldName();
        storageString += ("_" + locationParts.getBlockX());
        storageString += ("_" + locationParts.getBlockY());
        storageString += ("_" + locationParts.getBlockZ());
        return storageString;
    }

    public static Location getLocation(String string) {
        try {
            String[] parts = string.split("_");
            int partsLength = parts.length;
            String worldName = parts[0];
            for (int i = 1; i < partsLength - 3; i++) {
                worldName += ("_" + parts[i]);
            }
            World world = Bukkit.getWorld(worldName);
            int x = Integer.parseInt(parts[partsLength - 3]);
            int y = Integer.parseInt(parts[partsLength - 2]);
            int z = Integer.parseInt(parts[partsLength - 1]);
            return new Location(world, x, y, z, 0, 0);
        } catch (Exception e) {
            return null;
        }
    }


    public static LocationParts getLocationParts(String string) {
        try {
            String[] parts = string.split("_");
            int partsLength = parts.length;
            String worldName = parts[0];
            for (int i = 1; i < partsLength - 3; i++) {
                worldName += ("_" + parts[i]);
            }
            int x = Integer.parseInt(parts[partsLength - 3]);
            int y = Integer.parseInt(parts[partsLength - 2]);
            int z = Integer.parseInt(parts[partsLength - 1]);
            return new LocationParts(worldName, x, y, z);
        } catch (Exception e) {
            return null;
        }
    }

}
