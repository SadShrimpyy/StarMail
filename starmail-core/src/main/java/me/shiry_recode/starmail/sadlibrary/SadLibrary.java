package me.shiry_recode.starmail.sadlibrary;

import me.shiry_recode.starmail.sadlibrary.files.configurations.FilesBuilder;

public class SadLibrary {
    public static SadConfigurations Configurations;
    public static SadPlaceholders Placeholders;
//    public static SadPermissions Permissions;
    public static SadGenerics Generics;
    public static SadMessages Messages;
    public static SadFiles Files;
//    public static SadDate Date;

    public void initialize() {
        Generics = new SadGenerics();
//        Permissions = new SadPermissions();
//        Date = new SadDate();
        Messages = new SadMessages();
        Files = new SadFiles();
        Configurations = new SadConfigurations();

        Generics.displayHeader();
        buildFiles();
    }

    public void destroy() {
        Configurations = null;
        Placeholders = null;
//        Permissions = null;
        Generics = null;
        Messages = null;
        Files = null;
//        Date = null;
    }

    public void buildFiles() {
        new FilesBuilder();
    }

}
