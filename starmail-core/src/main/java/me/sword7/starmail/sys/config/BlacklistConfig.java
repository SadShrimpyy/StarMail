package me.sword7.starmail.sys.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BlacklistConfig {
    private static final File file = new File("plugins/StarMail", "blacklist.yml");
    private static FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    private static String blacklistString = "Items Blacklisted";
    private static List<Long> blacklist = new ArrayList<>();

    public BlacklistConfig() {
        load();
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
        blacklist = config.getLongList(blacklistString);
    }

    public static Path getFilePath() {
        return file.toPath();
    }

    private void load() {
        if (file.exists()) {
            try {
                blacklist = config.getLongList(blacklistString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addHashCode(long hashCode, String itemName) {
        try {
            final List<String> lines = Files.readAllLines(file.toPath());
            FileWriter fw = new FileWriter(file);
            for (String l : lines) {
                fw.append(l).append("\n");
            }
            fw.append("  - ").append(String.valueOf(hashCode)).append(' ').append("# ").append(itemName);
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        config = YamlConfiguration.loadConfiguration(file);
        blacklist = config.getLongList(blacklistString);
    }

    public static void removeHashCode(long hashCode) {
        try {
            final List<String> lines = Files.readAllLines(file.toPath());
            FileWriter fw = new FileWriter(file);
            int lineNumber = 0;
            for (int index = 0; index < lines.size(); index++) {
                if (lines.get(index).contains(Long.toString(hashCode))) {
                    lineNumber = index;
                }
            }
            lines.remove(lineNumber);
            for (String line : lines) {
                fw.append(line).append("\n");
            }
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        config = YamlConfiguration.loadConfiguration(file);
        blacklist = config.getLongList(blacklistString);
    }

    public static Long[] getList() {
        return blacklist.toArray(new Long[0]);
    }

    public static boolean contains(long hashCode) {
        return blacklist.contains(hashCode);
    }

    public static Long getAt(int index) {
        return blacklist.get(index);
    }
}
