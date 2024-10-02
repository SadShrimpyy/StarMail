package me.sword7.starmail.sys;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static me.sword7.starmail.sys.Language.*;

public class PluginHelp {

    private static boolean hasLetter = Version.current.hasLetter();

    public static void sendHelp(CommandSender sender) {

        sender.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "__Star Mail_______");

        sender.sendMessage("");
        for (String line : TEXT_INTRO.getLines()) {
            sender.sendMessage(ChatColor.GRAY + line);
        }
        sender.sendMessage("");

        sender.sendMessage(ChatColor.YELLOW + "------- " + ChatColor.GOLD + LABEL_COMMANDS + ChatColor.YELLOW + " -------");

        sendCommand(sender, "&6/boxes: &r" + TEXT_BOXES);
        sendCommand(sender, "&6/breakboxes: &r" + TEXT_BREAK);
        if (Permissions.canEBox(sender)) sendCommand(sender, "&6/mail: &r" + TEXT_MAIL);
        if (Permissions.canESend(sender)) sendCommand(sender, "&6/sendto &c[" + ARG_PLAYER + "]&6: &r" + TEXT_SENDTO);
        if (Permissions.canSummon(sender)) {
            if (hasLetter) {
                sendCommand(sender, "&6/letter &c[" + ARG_TYPE + "] [" + ARG_PLAYER + "] [" + ARG_AMOUNT + "]&6: &r" + TEXT_SUMMON);
            }
            sendCommand(sender, "&6/pack &c[" + ARG_TYPE + "] [" + ARG_PLAYER + "] [" + ARG_AMOUNT + "]&6: &r" + TEXT_SUMMON);
            sendCommand(sender, "&6/box &c[" + ARG_TYPE + "] [" + ARG_PLAYER + "] [" + ARG_AMOUNT + "]&6: &r" + TEXT_SUMMON);
            sendCommand(sender, "&6/globalbox &c[" + ARG_TYPE + "] [" + ARG_PLAYER + "] [" + ARG_AMOUNT + "]&6: &r" + TEXT_SUMMON);
            sendCommand(sender, "&6/postbox &c[" + ARG_TYPE + "] [" + ARG_PLAYER + "] [" + ARG_AMOUNT + "]&6: &r" + TEXT_SUMMON);
        }
        if (Permissions.canWarehouse(sender)) {
            sender.sendMessage(ChatColor.GOLD + "- - - - - - -");
            sendWarehouse(sender);
        }
        if (Permissions.canBlacklist(sender)) {
            sender.sendMessage(ChatColor.GOLD + "- - - - - - -");
            sendBlacklist(sender);
        }

    }

    private static void sendCommand(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private static void sendWarehouse(CommandSender sender) {
        sendCommand(sender, "&6/warehouse send &c[" + ARG_PLAYER + "] [" + ARG_TYPE + "]&6: &r" + TEXT_WAREHOUSE_SEND);
        sendCommand(sender, "&6/warehouse save &c[" + ARG_NAME + "]&6: &r" + TEXT_WAREHOUSE_SAVE);
        sendCommand(sender, "&6/warehouse rename &c[" + ARG_FROM + "] [" + ARG_TO + "]&6: &r" + TEXT_WAREHOUSE_RENAME);
        sendCommand(sender, "&6/warehouse edit &c[" + ARG_TYPE + "]&6: &r" + TEXT_WAREHOUSE_EDIT);
        sendCommand(sender, "&6/warehouse delete &c[" + ARG_TYPE + "]&6: &r" + TEXT_WAREHOUSE_DELETE);
        sendCommand(sender, "&6/warehouse list: &r" + TEXT_WAREHOUSE_LIST);
    }

    public static void sendWarehouseHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + LABEL_WAREHOUSE);
        sender.sendMessage(ChatColor.YELLOW + "-------" + ChatColor.GOLD + LABEL_COMMANDS + ChatColor.YELLOW + "-------");
        sendWarehouse(sender);
    }

    public static void sendBlacklistHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + LABEL_BLACKLIST);
        sender.sendMessage(ChatColor.YELLOW + "-------" + ChatColor.GOLD + LABEL_COMMANDS + ChatColor.YELLOW + "-------");
        sendBlacklist(sender);
    }

    public static void sendBlacklist(CommandSender sender) {
        sendCommand(sender, "&6/blacklist add&6: &r" + TEXT_BLACKLIST_ADD);
        sendCommand(sender, "&6/blacklist list&6: &r" + TEXT_BLACKLIST_LIST);
        sendCommand(sender, "&6/blacklist remove&6: &r" + TEXT_BLACKLIST_REMOVE);
        sendCommand(sender, "&6/blacklist reload&6: &r" + TEXT_BLACKLIST_RELOAD);
    }

}
