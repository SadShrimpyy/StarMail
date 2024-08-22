package me.sword7.starmail.post;

import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.sys.Permissions;
import me.sword7.starmail.util.X.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Timestamp;
import java.time.Instant;

public class Mail {

    private ItemStack itemStack;
    private Timestamp timestamp;
    private String from;

    public Mail(ItemStack itemStack, String from) {
        this.itemStack = itemStack;
        this.from = from;
        this.timestamp = Timestamp.from(Instant.now());
    }

    public Mail(ItemStack itemStack, String from, Timestamp timestamp) {
        this.itemStack = itemStack;
        this.from = from;
        this.timestamp = timestamp;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getFrom() {
        return from;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    private static Material writtenBook = XMaterial.WRITTEN_BOOK.parseMaterial();

    public static boolean isMail(Player player, ItemStack itemStack) {
        if(itemStack != null){
            ItemMeta meta = itemStack.getItemMeta();
            return itemStack.getType() == writtenBook || Pack.isSealedPack(meta) || Permissions.canSendCustom(player);
        }else{
            return false;
        }
    }

}
