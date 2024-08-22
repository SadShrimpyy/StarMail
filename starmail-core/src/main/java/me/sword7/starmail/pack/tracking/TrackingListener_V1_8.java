package me.sword7.starmail.pack.tracking;

import me.sword7.starmail.StarMail;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class TrackingListener_V1_8 implements Listener {

    public TrackingListener_V1_8() {
        Plugin plugin = StarMail.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        UUID itemID = e.getItem().getUniqueId();
        if (TrackingRunnable.sealedItems.contains(itemID)) {
            TrackingRunnable.sealedItems.remove(itemID);
        }
    }


}