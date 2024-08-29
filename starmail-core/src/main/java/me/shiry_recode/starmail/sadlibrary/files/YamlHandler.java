package me.shiry_recode.starmail.sadlibrary.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static me.shiry_recode.starmail.sadlibrary.SadLibrary.Generics;
import static me.shiry_recode.starmail.sadlibrary.SadLibrary.Messages;

public class YamlHandler implements IFile {

    private FileConfiguration Configuration;
    private final File file;

    public YamlHandler(String fileName) {
        this.file = new File(Generics.getPluginFolder(), fileName);
        this.Configuration = get();
    }

    @Override
    public boolean initialize() throws Exception {
        if (!file.exists()) return true;
        if (file.createNewFile()) {
            Generics.getPlugin().saveResource(file.getName(), true);
            Messages.logConsole("&rThe file &f" + file.getName() + "&r: &rwas created &acorrectly &rusing defaults values.");
            Configuration = get();
            return true;
        } else {
            Messages.logConsole("&eThe file &f" + file.getName() + "&r: &ccannot &rbe created.");
            Configuration = null;
            return false;
        }
    }

    @Override
    public void save() {
        try {
            this.Configuration = get();
            Configuration.save(file.getName());
        } catch (IOException e) {
            Messages.logConsoleError("&cError &rencountered while uploading the file &f" + file.getName() + "&r.");
        }
    }

    @Override
    public void reload() {
        Configuration = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public boolean exixts() {
        return new File(Generics.getPlugin().getDataFolder(), file.getName()).exists();
    }

    @Override
    public FileConfiguration get() {
        return YamlConfiguration.loadConfiguration(new File(Generics.getPluginFolder(), file.getName()));
    }
}