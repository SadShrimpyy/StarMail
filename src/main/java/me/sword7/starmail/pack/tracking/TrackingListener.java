package me.sword7.starmail.pack.tracking;

import me.sword7.starmail.StarMail;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.sys.Version;
import me.sword7.starmail.warehouse.WarehouseCache;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class TrackingListener implements Listener {

    public TrackingListener() {
        Plugin plugin = StarMail.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        if (Version.current.value >= 112) {
            new TrackingListener_V1_12();
        } else {
            new TrackingListener_V1_8();
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack itemStack = e.getItemDrop().getItemStack();
        if (Pack.isSealedPack(itemStack)) {
            if (!WarehouseCache.isWarehousePack(Pack.getTrackingNo(itemStack))) {
                TrackingRunnable.sealedItems.add(e.getItemDrop().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onSpawn(ItemSpawnEvent e) {
        ItemStack itemStack = e.getEntity().getItemStack();
        if (Pack.isSealedPack(itemStack)) {
            if (!WarehouseCache.isWarehousePack(Pack.getTrackingNo(itemStack))) {
                TrackingRunnable.sealedItems.add(e.getEntity().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onDespawn(ItemDespawnEvent e) {
        Item item = e.getEntity();
        UUID itemID = item.getUniqueId();
        if (TrackingRunnable.sealedItems.contains(itemID)) {
            TrackingRunnable.sealedItems.remove(itemID);
            UUID trackingNo = Pack.getTrackingNo(item.getItemStack());
            TrackingCache.unTrack(trackingNo);
        }
    }

    @EventHandler
    public void onCombust(EntityCombustEvent e) {
        if (e.getEntity() instanceof Item) {
            Item item = (Item) e.getEntity();
            UUID itemID = item.getUniqueId();
            if (TrackingRunnable.sealedItems.contains(itemID)) {
                TrackingRunnable.sealedItems.remove(itemID);
                UUID trackingNo = Pack.getTrackingNo(item.getItemStack());
                TrackingCache.unTrack(trackingNo);
            }
        }
    }


    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntity() instanceof Item) {
            Item item = (Item) e.getEntity();
            UUID itemID = item.getUniqueId();
            if (TrackingRunnable.sealedItems.contains(itemID)) {
                TrackingRunnable.sealedItems.remove(itemID);
                UUID trackingNo = Pack.getTrackingNo(item.getItemStack());
                TrackingCache.unTrack(trackingNo);
            }
        }
    }

}
