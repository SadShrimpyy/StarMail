package me.shiry_recode.starmail.sadlibrary.files.configurations;

import me.shiry_recode.starmail.sadlibrary.files.YamlHandler;

import static me.shiry_recode.starmail.sadlibrary.SadLibrary.*;

public class CConfig implements IFileWrapper {

    @Override
    public String getName() {
        return Files.getConfigName();
    }

    @Override
    public void perform() throws Exception {
        if (Files.fileExists(null, getName())) return;
        if (new YamlHandler(Files.getConfigName()).initialize()) Configurations.setNewConfig();
    }
}