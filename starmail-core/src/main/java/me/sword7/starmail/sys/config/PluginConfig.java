package me.sword7.starmail.sys.config;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static me.sword7.starmail.StarMail.getPlugin;

public class PluginConfig {

    private static File file = new File("plugins/StarMail", "config.yml");
    private static FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    private static String languageFileString = "Language file";
    private static String languageFile = "en";

    private static String maxMailBoxesString = "Default Max Mailbox";
    private static int maxMailBoxes = 1;

    private static String mailExpirationString = "Mail Expiration Days";
    private static int mailExpiration = 30;

    private static String packageExpirationString = "Package Expiration Days";
    private static int packageExpiration = 30;

    private static String inactiveUserExpirationString = "Inactive User Expiration Days";
    private static int inactiveUserExpiration = 45;

    private static String blacklistWorldsString = "Mailbox World Blacklist";
    private static List<String> blacklistWorlds = buildBlacklistWorld();

    private static List<String> buildBlacklistWorld() {
        List<String> blacklistWorlds = new ArrayList<>();
        blacklistWorlds.add("example-world");
        return blacklistWorlds;
    }

    private static String onJoinDelayTicksString = "on Join Notification Delay (Ticks)";
    private static int onJoinDelayTicks = 3;

    private static String requireBlockPermissionString = "Require Block Permission";
    private static boolean requireBlockPermission = false;

    private static String showCrateStrapsString = "Show Crate Straps";
    private static boolean showCrateStraps = true;

    private static String sendCooldownString = "Default Send Cooldown (Seconds)";
    private static int sendCooldown = 0;

    private static String instantSendString = "Instant Mail Send";
    private static boolean instantSend = false;

    private static String automaticServerPack = "Automatic Server Resourcepack";
    private static boolean automaticPack = true;

    public PluginConfig() {
        load();
    }

    private void load() {
        if (file.exists()) {
            try {
                languageFile = config.getString(languageFileString, languageFile);
                maxMailBoxes = config.getInt(maxMailBoxesString, maxMailBoxes);
                mailExpiration = config.getInt(mailExpirationString, mailExpiration);
                packageExpiration = config.getInt(packageExpirationString, packageExpiration);
                inactiveUserExpiration = config.getInt(inactiveUserExpirationString, inactiveUserExpiration);
                blacklistWorlds = config.getStringList(blacklistWorldsString);
                onJoinDelayTicks = config.getInt(onJoinDelayTicksString, onJoinDelayTicks);
                if (onJoinDelayTicks < 0) onJoinDelayTicks = 0;
                requireBlockPermission = config.getBoolean(requireBlockPermissionString, requireBlockPermission);
                showCrateStraps = config.getBoolean(showCrateStrapsString, showCrateStraps);
                sendCooldown = config.getInt(sendCooldownString, sendCooldown);
                instantSend = config.getBoolean(instantSendString, instantSend);
                if (!config.contains(automaticServerPack)) getPlugin().saveResource("config.yml", true);
                automaticPack = config.getBoolean(automaticServerPack, automaticPack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int getMaxMailBoxes() {
        return maxMailBoxes;
    }

    public static int getMailExpirationDays() {
        return mailExpiration;
    }

    public static int getPackageExpirationDays() {
        return packageExpiration;
    }

    public static int getInactiveUserExpirationDays() {
        return inactiveUserExpiration;
    }

    public static String getLanguageFile() {
        return languageFile;
    }

    public static boolean isAllowedMailboxWorld(World world) {
        return !blacklistWorlds.contains(world.getName());
    }

    public static int getOnJoinDelayTicks() {
        return onJoinDelayTicks;
    }

    public static boolean isRequireBlockPermission() {
        return requireBlockPermission;
    }

    public static boolean isShowCrateStraps() {
        return showCrateStraps;
    }

    public static int getSendCooldown() {
        return sendCooldown;
    }

    public static boolean isInstantSend() {
        return instantSend;
    }

    public static boolean isAutomaticPack() {
        return automaticPack;
    }
}
