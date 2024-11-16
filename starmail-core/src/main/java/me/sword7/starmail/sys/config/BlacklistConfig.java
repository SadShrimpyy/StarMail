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

    private static final String blacklistString = "Items Blacklisted";
    private static List<String> blacklist = new ArrayList<>();

    public BlacklistConfig() {
        load();
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
        blacklist = config.getStringList(blacklistString);
    }

    @SuppressWarnings("unused")
    public static Path getFilePath() {
        return file.toPath();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void load() {
        if (file.exists()) {
            try {
                blacklist = config.getStringList(blacklistString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addExceptLine(String requestedException) {
        try {
            final List<String> lines = Files.readAllLines(file.toPath());
            FileWriter fw = new FileWriter(file);
            for (String l : lines) {
                fw.append(l).append("\n");
            }
            fw.append("  - ").append(String.valueOf(requestedException));
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        config = YamlConfiguration.loadConfiguration(file);
        blacklist = config.getStringList(blacklistString);
    }

    public static void removeExceptLine(String requestedException) {
        try {
            final List<String> lines = Files.readAllLines(file.toPath());
            FileWriter fw = new FileWriter(file);
            for (String l : lines) {
                System.out.println(l);
                if (l.equals("  - " + requestedException)) {
                    continue;
                }
                fw.append(l).append("\n");
            }
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        config = YamlConfiguration.loadConfiguration(file);
        blacklist = config.getStringList(blacklistString);
    }

    public static String[] getList() {
        return blacklist.toArray(new String[0]);
    }

    public static boolean contains(String line) {
        return blacklist.contains(line);
    }

    public static String getAt(int index) {
        return blacklist.get(index);
    }
}
