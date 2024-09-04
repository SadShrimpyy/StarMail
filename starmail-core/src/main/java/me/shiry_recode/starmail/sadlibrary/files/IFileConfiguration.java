package me.shiry_recode.starmail.sadlibrary.files;

import org.bukkit.configuration.file.FileConfiguration;

interface IFileConfiguration {
    FileConfiguration get();
    boolean exixts();
    void reload();
    void save();
    boolean initialize() throws Exception;
}
