package me.shiry_recode.starmail.sadlibrary.files;

public class SadFiles {

    protected final String configName = "config.yml";
    protected final String messagesName = "messages.yml";
    protected final String[] allFiles = {
            this.configName, this.messagesName
    };

    public String getConfigName() {
        return this.configName;
    }

    public String getMessagesName() {
        return this.messagesName;
    }

    public String[] getAllFiles() {
        return this.allFiles;
    }
}
