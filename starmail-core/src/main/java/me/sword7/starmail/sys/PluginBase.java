package me.sword7.starmail.sys;

import me.sword7.starmail.util.Dynmap;
import me.sword7.starmail.sys.config.IntegrationConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import static me.sword7.starmail.sys.Language.CONSOLE_PLUGIN_DETECT;

public class PluginBase {

    private static String dynmapNameSpace = "dynmap";

    private static Dynmap dynmap;
    private static boolean usingDynmap = false;

    public PluginBase() {
        loadDependencies();
    }

    public void loadDependencies() {
        if (IntegrationConfig.isUseDynmap()) loadDynmap();
    }

    private void loadDynmap() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(dynmapNameSpace);
        if (plugin != null && plugin.isEnabled()) {
            dynmap = new Dynmap(plugin);
            usingDynmap = true;
            ConsoleSender.sendMessage(CONSOLE_PLUGIN_DETECT.fromPlugin(dynmapNameSpace));
        }
    }


    public static Dynmap getDynmap() {
        return dynmap;
    }

    public static boolean isUsingDynmap() {
        return usingDynmap;
    }

    public static void shutdown() {
        if (usingDynmap) dynmap.shutdown();
    }

}
