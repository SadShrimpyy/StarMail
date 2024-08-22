package me.sword7.starmail.sys;

import org.bukkit.Bukkit;

public class ConsoleSender {

    public static void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage("[StarMail] " + message);
    }

}
