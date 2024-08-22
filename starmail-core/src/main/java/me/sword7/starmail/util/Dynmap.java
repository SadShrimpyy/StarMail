package me.sword7.starmail.util;

import me.sword7.starmail.StarMail;
import me.sword7.starmail.box.Box;
import me.sword7.starmail.box.BoxType;
import me.sword7.starmail.box.PlacedBox;
import me.sword7.starmail.postbox.Postbox;
import me.sword7.starmail.sys.config.IntegrationConfig;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static me.sword7.starmail.sys.Language.LABEL_MAILBOX;
import static me.sword7.starmail.sys.Language.LABEL_POSTBOX;

public class Dynmap {

    private Map<LocationParts, Marker> locationToMarker = new HashMap<>();
    private Map<UUID, MarkerIcon> boxToIcon = new HashMap<>();
    private Map<UUID, MarkerIcon> postboxToIcon = new HashMap<>();

    private MarkerAPI markerAPI;
    private DynmapAPI dynmapAPI;
    private MarkerIcon defaultMailIcon;
    private MarkerIcon defaultPostIcon;
    private MarkerSet mailboxIconSet;

    private int mailMinZoom;
    private int mailMaxZoom;

    private int globalMinZoom;
    private int globalMaxZoom;

    private int postMinZoom;
    private int postMaxZoom;

    public Dynmap(Plugin plugin) {
        this.dynmapAPI = (DynmapAPI) plugin;
        this.markerAPI = dynmapAPI.getMarkerAPI();
        load();
    }

    private void load() {

        mailMinZoom = IntegrationConfig.getBoxMinZoom();
        mailMaxZoom = IntegrationConfig.getBoxMaxZoom();
        globalMinZoom = IntegrationConfig.getGlobalBoxMinZoom();
        globalMaxZoom = IntegrationConfig.getGlobalBoxMaxZoom();
        postMinZoom = IntegrationConfig.getPostboxMinZoom();
        postMaxZoom = IntegrationConfig.getPostboxMaxZoom();

        Set<MarkerIcon> icons = new HashSet<>();

        InputStream is = StarMail.getPlugin().getResource("icons/mailbox.png");
        if (markerAPI.getMarkerIcon("mailbox") != null) markerAPI.getMarkerIcon("mailbox").deleteIcon();
        defaultMailIcon = markerAPI.createMarkerIcon("mailbox", "mailbox", is);
        icons.add(defaultMailIcon);

        for (Box box : Box.getAllBoxes()) {
            String resID = box.getType().toString().toLowerCase() + "-box";
            if (box.getType() == BoxType.CUSTOM) resID = box.getGlass().toString().toLowerCase() + "-box";
            is = StarMail.getPlugin().getResource("icons/" + resID + ".png");
            if (markerAPI.getMarkerIcon(resID) != null) markerAPI.getMarkerIcon(resID).deleteIcon();
            MarkerIcon markerIcon = markerAPI.createMarkerIcon(resID, resID, is);
            icons.add(markerIcon);
            boxToIcon.put(box.getProfileID(), markerIcon);
        }

        is = StarMail.getPlugin().getResource("icons/postbox.png");
        if (markerAPI.getMarkerIcon("postbox") != null) markerAPI.getMarkerIcon("postbox").deleteIcon();
        defaultPostIcon = markerAPI.createMarkerIcon("postbox", "postbox", is);
        icons.add(defaultPostIcon);

        for (Postbox postbox : Postbox.getAllPostboxes()) {
            String resID = "postbox-" + postbox.getType().toString().toLowerCase();
            is = StarMail.getPlugin().getResource("icons/" + resID + ".png");
            if (markerAPI.getMarkerIcon(resID) != null) markerAPI.getMarkerIcon(resID).deleteIcon();
            MarkerIcon markerIcon = markerAPI.createMarkerIcon(resID, resID, is);
            icons.add(markerIcon);
            postboxToIcon.put(postbox.getProfileID(), markerIcon);
        }

        mailboxIconSet = markerAPI.createMarkerSet("starmail.markerset", "Mailboxes", icons, false);
        mailboxIconSet.setHideByDefault(!IntegrationConfig.isShowPlotMarkersByDefault());

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerPlayerBoxAt(LocationParts locationParts, String ownerName, PlacedBox placedBox) {
        registerBoxAt(locationParts, placedBox, " ~ " + ownerName + " ~", mailMinZoom, mailMaxZoom);
    }

    public void registerGlobalBoxAt(LocationParts locationParts, PlacedBox placedBox) {
        registerBoxAt(locationParts, placedBox, "", globalMinZoom, globalMaxZoom);
    }

    public void registerBoxAt(LocationParts locationParts, PlacedBox placedBox, String postfix, int minZoom, int maxZoom) {
        if (mailboxIconSet != null) {
            if (locationToMarker.containsKey(locationParts)) deleteBoxAt(locationParts);
            String markerID = "box-" + UUID.randomUUID();
            Box box = placedBox.getBox();
            UUID typeID = box.getProfileID();
            MarkerIcon markerIcon = boxToIcon.containsKey(typeID) ? boxToIcon.get(typeID) : defaultMailIcon;
            Marker marker = mailboxIconSet.createMarker(markerID, LABEL_MAILBOX.toString() + postfix, locationParts.getWorldName(), locationParts.getBlockX(), locationParts.getBlockY(), locationParts.getBlockZ(), markerIcon, false);
            if (minZoom > 0) marker.setMinZoom(minZoom);
            if (maxZoom > 0) marker.setMaxZoom(maxZoom);
            locationToMarker.put(locationParts, marker);
        }
    }

    public void deleteBoxAt(LocationParts locationParts) {
        if (locationToMarker.containsKey(locationParts)) {
            Marker marker = locationToMarker.get(locationParts);
            marker.deleteMarker();
            locationToMarker.remove(locationParts);
        }
    }

    public void registerPostboxAt(LocationParts locationParts, Postbox postbox) {
        if (mailboxIconSet != null) {
            if (locationToMarker.containsKey(locationParts)) deletePostboxAt(locationParts);
            String markerID = "postbox-" + UUID.randomUUID();
            UUID typeID = postbox.getProfileID();
            MarkerIcon markerIcon = postboxToIcon.containsKey(typeID) ? postboxToIcon.get(typeID) : defaultPostIcon;
            Marker marker = mailboxIconSet.createMarker(markerID, LABEL_POSTBOX.toString(), locationParts.getWorldName(), locationParts.getBlockX(), locationParts.getBlockY(), locationParts.getBlockZ(), markerIcon, false);
            if (postMinZoom > 0) marker.setMinZoom(postMinZoom);
            if (postMaxZoom > 0) marker.setMaxZoom(postMaxZoom);
            locationToMarker.put(locationParts, marker);
        }
    }

    public void deletePostboxAt(LocationParts locationParts) {
        if (locationToMarker.containsKey(locationParts)) {
            Marker marker = locationToMarker.get(locationParts);
            marker.deleteMarker();
            locationToMarker.remove(locationParts);
        }
    }

    public void shutdown() {
        for (Marker marker : locationToMarker.values()) {
            marker.deleteMarker();
        }
        locationToMarker.clear();
    }

}
