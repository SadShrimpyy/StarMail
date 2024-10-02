package me.sword7.starmail.sys.config;

import com.google.common.collect.ImmutableList;
import me.sword7.starmail.StarMail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigLoader {

    private static final String pluginFolder = "StarMail";

    private static ImmutableList<String> configs = new ImmutableList.Builder<String>()
            .add("config")
            .add("integrations")
            .add("items")
            .build();

    private static ImmutableList<String> languages = new ImmutableList.Builder<String>()
            .add("en")
            .add("pl")
            .add("ru")
            .add("fr")
            .add("de")
            .add("zh_s")
            .add("zh_tr")
            .build();

    public static void load() {
        try {
            for (String config : configs) {
                File target = new File("plugins/" + pluginFolder, config + ".yml");
                if (!target.exists()) {
                    load(config + ".yml", target);
                }
            }
            for (String lang : languages) {
                File target = new File("plugins/" + pluginFolder + "/Locale", lang + ".yml");
                if (!target.exists()) {
                    load("locale/" + lang + ".yml", target);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void load(String resource, File file) throws IOException {
        file.getParentFile().mkdirs();
        InputStream in = StarMail.getPlugin().getResource(resource);
        Files.copy(in, file.toPath());
        in.close();
    }


}
