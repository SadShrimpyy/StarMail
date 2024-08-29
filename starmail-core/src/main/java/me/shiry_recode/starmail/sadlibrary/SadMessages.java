package me.shiry_recode.starmail.sadlibrary;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import static me.shiry_recode.starmail.sadlibrary.SadLibrary.Configurations;

public class SadMessages {
    private final ConsoleCommandSender Console = Bukkit.getServer().getConsoleSender();
    private final String DefaultPrefix = "&c[&eStarMail Reloaded&7] &8> &7";

    public String translateColors(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public String composeWithPrefix(String message) {
        return translateColors(Configurations.getMessages().getString("prefix") + message);
    }

    public String compose(String message) {
        return translateColors(message);
    }

    public void logConsoleError(String message) {
        String errorPrefix = "&4ERROR &e-> &r";
        Console.sendMessage(translateColors(DefaultPrefix + errorPrefix + message));
    }

    public void logConsole(String message) {
        Console.sendMessage(translateColors(DefaultPrefix + message));
    }

    public void consoleHeader(String message) {
        Console.sendMessage(translateColors(message));
    }
}
