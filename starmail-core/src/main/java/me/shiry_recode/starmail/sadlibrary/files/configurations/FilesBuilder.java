package me.shiry_recode.starmail.sadlibrary.files.configurations;

import java.util.ArrayList;

import static me.shiry_recode.starmail.sadlibrary.SadLibrary.*;

public class FilesBuilder {

    private final ArrayList<IFileWrapper> DefaultFiles = new ArrayList<>();

    {
//        TODO: Insert here ONLY if the original starmail code didn't work with the file
//        DefaultFiles.add(new CConfig());
        DefaultFiles.add(new CMessages());
    }

    public FilesBuilder() {
        if (!Generics.getPluginFolder().exists())
            if (!Generics.getPluginFolder().mkdirs()) Messages.logConsoleError("&rThe directory: &f" + Generics.getPluginFolder() + "&r &ccan't &rbe created.");
        buildFiles();
    }

    private void buildFiles() {
        for (IFileWrapper fileIndex : DefaultFiles) {
            try {
                fileIndex.perform();
            } catch (Exception e) {
                Messages.logConsoleError("&rThe file: &f" + fileIndex.getName() + "&r &ccan't &rbe initialized.");
                throw new RuntimeException(e);
            }
        }
    }
}