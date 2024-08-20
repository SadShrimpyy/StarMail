package me.sword7.starmail.util;

import me.sword7.starmail.StarMail;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Scheduler {

    private static BukkitScheduler scheduler = Bukkit.getScheduler();
    private static Plugin plugin = StarMail.getPlugin();

    public static void run(Runnable r) {
        scheduler.runTask(plugin, r);
    }

    public static void runAsync(Runnable r) {
        scheduler.runTaskAsynchronously(plugin, r);
    }

    public static void runLater(Runnable r, int ticks) {
        scheduler.runTaskLater(plugin, r, ticks);
    }

    public static void runLaterAsync(Runnable r, int ticks) {
        scheduler.runTaskLaterAsynchronously(plugin, r, ticks);
    }

}
