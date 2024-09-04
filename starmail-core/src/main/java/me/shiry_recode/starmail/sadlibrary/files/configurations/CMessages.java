package me.shiry_recode.starmail.sadlibrary.files.configurations;

import me.shiry_recode.starmail.sadlibrary.files.YamlHandler;

import static me.shiry_recode.starmail.sadlibrary.SadLibrary.*;

public class CMessages implements IFileWrapper {

    @Override
    public String getName() {
        return Files.getMessagesName();
    }

    @Override
    public void perform() throws Exception {
        if (Files.fileExists(null, getName())) return;
        if (new YamlHandler(Files.getMessagesName()).initialize()) Configurations.setNewMessages();
    }
}
