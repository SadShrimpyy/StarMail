package me.sword7.starmail.sys.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class IntegrationConfig {

    private static File file = new File("plugins/StarMail", "integrations.yml");
    private static FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    private static String useDynmapString = "Enable Dynmap";
    private static boolean useDynmap = true;

    private static String showPlotsMarkersByDefaultString = "Show Markers by Default";
    private static boolean showPlotMarkersByDefault = true;

    private static String boxMinZoomString = "Mailbox Min Zoom";
    private static int boxMinZoom = 4;

    private static String boxMaxZoomString = "Mailbox Max Zoom";
    private static int boxMaxZoom = -1;

    private static String globalBoxMinZoomString = "Global Mailbox Min Zoom";
    private static int globalBoxMinZoom = -1;

    private static String globalBoxMaxZoomString = "Global Mailbox Max Zoom";
    private static int globalBoxMaxZoom = -1;

    private static String postboxMinZoomString = "Postbox Min Zoom";
    private static int postboxMinZoom = -1;

    private static String postboxMaxZoomString = "Postbox Max Zoom";
    private static int postboxMaxZoom = -1;

    public IntegrationConfig() {
        load();
    }

    private void load() {
        if (file.exists()) {
            try {
                useDynmap = config.getBoolean(useDynmapString, useDynmap);
                showPlotMarkersByDefault = config.getBoolean(showPlotsMarkersByDefaultString, showPlotMarkersByDefault);
                boxMinZoom = config.getInt(boxMinZoomString, boxMinZoom);
                boxMaxZoom = config.getInt(boxMaxZoomString, boxMaxZoom);
                globalBoxMinZoom = config.getInt(globalBoxMinZoomString, globalBoxMinZoom);
                globalBoxMaxZoom = config.getInt(globalBoxMaxZoomString, globalBoxMaxZoom);
                postboxMinZoom = config.getInt(postboxMinZoomString, postboxMinZoom);
                postboxMaxZoom = config.getInt(postboxMaxZoomString, postboxMaxZoom);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isUseDynmap() {
        return useDynmap;
    }

    public static boolean isShowPlotMarkersByDefault() {
        return showPlotMarkersByDefault;
    }

    public static int getBoxMinZoom() {
        return boxMinZoom;
    }

    public static int getBoxMaxZoom() {
        return boxMaxZoom;
    }

    public static int getGlobalBoxMinZoom() {
        return globalBoxMinZoom;
    }

    public static int getGlobalBoxMaxZoom() {
        return globalBoxMaxZoom;
    }

    public static int getPostboxMinZoom() {
        return postboxMinZoom;
    }

    public static int getPostboxMaxZoom() {
        return postboxMaxZoom;
    }
}
