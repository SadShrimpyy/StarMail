package me.sword7.starmail.pack.tracking;

import me.sword7.starmail.StarMail;
import me.sword7.starmail.pack.Pack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrackingRunnable extends BukkitRunnable {

    protected static List<UUID> sealedItems = new ArrayList<>();

    private static TrackingRunnable instance;

    public static TrackingRunnable getInstance() {
        return instance;
    }

    private TrackingRunnable() {
    }

    public static void start() {
        instance = new TrackingRunnable();
        new TrackingListener();
        instance.runTaskTimer(StarMail.getPlugin(), 20 * 60 * 1, 20 * 60 * 1);
    }

    public static void shutdown() {
        for (int i = sealedItems.size() - 1; i >= 0; i--) {
            UUID ID = sealedItems.get(i);
            Entity entity = Bukkit.getEntity(ID);
            if (entity != null) {
                Item item = (Item) entity;
                TrackingCache.unTrackSync(Pack.getTrackingNo(item.getItemStack().getItemMeta()));
                item.remove();
            }
        }
        sealedItems.clear();
        instance.cancel();
    }

    @Override
    public void run() {
        for (int i = sealedItems.size() - 1; i >= 0; i--) {
            UUID ID = sealedItems.get(i);
            Entity entity = Bukkit.getEntity(ID);
            if (entity != null) {
                Item item = (Item) entity;
                if (!item.isValid() || item.isDead() || item.getLocation().getY() < 0) {
                    TrackingCache.unTrack(Pack.getTrackingNo(item.getItemStack().getItemMeta()));
                    item.remove();
                    sealedItems.remove(i);
                }
            } else {
                sealedItems.remove(i);
            }
        }
    }


}