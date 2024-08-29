package me.shiry_recode.starmail.sadlibrary.files;

import org.bukkit.configuration.file.FileConfiguration;

public interface IFile {
    FileConfiguration get();
    boolean exixts();
    void reload();
    void save();
    boolean initialize() throws Exception;
}
