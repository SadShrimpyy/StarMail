package me.shiry_recode.starmail.sadlibrary;

import me.shiry_recode.starmail.sadlibrary.files.YamlHandler;
import org.bukkit.configuration.file.FileConfiguration;

public class SadConfigurations extends SadFiles {
    private FileConfiguration ConfigConfiguration;
    private FileConfiguration MessagesConfiguration;

    public boolean reloadFiles() {
        this.ConfigConfiguration = new YamlHandler(super.configName).get();
        this.MessagesConfiguration = new YamlHandler(super.messagesName).get();
        return true;
    }

    public void setNewConfig() {
        this.ConfigConfiguration = new YamlHandler(super.configName).get();
    }

    public void setNewMessages() {
        this.MessagesConfiguration = new YamlHandler(super.messagesName).get();
    }

    public FileConfiguration getConfig() {
        return this.ConfigConfiguration;
    }

    public FileConfiguration getMessages() {
        return this.MessagesConfiguration;
    }
}
