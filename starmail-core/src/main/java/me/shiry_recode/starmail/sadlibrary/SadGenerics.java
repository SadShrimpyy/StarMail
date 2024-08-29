package me.shiry_recode.starmail.sadlibrary;

import me.sword7.starmail.StarMail;
import org.bukkit.Bukkit;

import java.io.File;

import static me.shiry_recode.starmail.sadlibrary.SadLibrary.Messages;

public class SadGenerics {
    private StarMail plugin;
    private File PluginFolder;
    private final String version = "v2.0.3-dev-release";

    public SadGenerics() {
        this.plugin = (StarMail) Bukkit.getPluginManager().getPlugin("StarMail");
        assert plugin != null;
        this.PluginFolder = plugin.getDataFolder();
    }

    private void display() {
        Messages.consoleHeader("&e      _____ __  __ _____    &r");
        Messages.consoleHeader("&e     / ____|  \\/  |  __ \\   &r");
        Messages.consoleHeader("&e    | (___ | \\  / | |__) |  &r");
        Messages.consoleHeader("&e     \\___ \\| |\\/| |  _  /   &r");
        Messages.consoleHeader("&e     ____) | |  | | | \\ \\   &r");
        Messages.consoleHeader("&e    |_____/|_|  |_|_|  \\_\\  &r");
        Messages.consoleHeader("");
        Messages.consoleHeader("&r -> Retained & Recoded by: &9&osadshrimpy &r(&9Discord&r)");
        Messages.consoleHeader("&r -> The original (by sword7) is &ldiscontinued&r");
    }

    public String getVersion() {
        return this.version;
    }

    public StarMail getPlugin() {
        return this.plugin;
    }

    public File getPluginFolder() {
        return this.PluginFolder;
    }

    public void displayHeader() {
        this.display();
        Messages.consoleHeader("&r -> Current version: &a" + this.version + "&r\n");
    }
}