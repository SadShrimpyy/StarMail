package me.sword7.starmail;

import me.sword7.starmail.letter.Letter;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.pack.PackType;
import me.sword7.starmail.pack.tracking.TrackingCache;
import me.sword7.starmail.post.Mail;
import me.sword7.starmail.post.PostCache;
import me.sword7.starmail.warehouse.WarehouseCache;
import me.sword7.starmail.warehouse.WarehouseEntry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class StarMailAPI {

    private static StarMailAPI api = new StarMailAPI();

    private StarMailAPI() {
    }

    public static StarMailAPI getInstance() {
        return api;
    }

    public void sendMail(UUID playerID, Mail mail) {
        PostCache.send(playerID, mail);
    }

    public ItemStack getSignedLetter(Letter letterType, BookMeta bookMeta) {
        if (letterType != null && bookMeta != null) {
            return letterType.getLetter(bookMeta);
        } else {
            return null;
        }
    }

    public ItemStack getSealedPackage(PackType packType, ItemStack[] itemStacks) {
        if (packType != null && itemStacks != null) {
            ItemStack pack = packType.getPack().getEmptyPack();
            UUID trackingNo = TrackingCache.track(itemStacks);
            ItemMeta meta = pack.getItemMeta();
            meta = Pack.seal(meta, trackingNo, itemStacks);
            pack.setItemMeta(meta);
            return pack;
        } else {
            return null;
        }
    }

    public WarehouseEntry getWarehouseEntry(String type) {
        return WarehouseCache.getEntry(type.toUpperCase());
    }


}
