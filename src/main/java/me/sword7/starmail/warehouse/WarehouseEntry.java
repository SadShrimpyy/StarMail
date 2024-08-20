package me.sword7.starmail.warehouse;

import me.sword7.starmail.letter.Letter;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.post.Mail;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.X.XMaterial;
import org.bukkit.inventory.ItemStack;

public class WarehouseEntry {

    private ItemStack itemStack;
    private Type type = Type.CUSTOM;
    private String from;

    public WarehouseEntry(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
        this.from = Language.LABEL_SERVER.toString();
        assignType();
    }

    public WarehouseEntry(ItemStack itemStack, String from) {
        this.itemStack = itemStack.clone();
        this.from = from;
        assignType();
    }

    private void assignType() {
        if (Pack.isSealedPack(itemStack)) {
            type = Type.PACK;
        } else if (Letter.isLetter(itemStack) && itemStack.getType() == XMaterial.WRITTEN_BOOK.parseMaterial()) {
            type = Type.LETTER;
        } else {
            type = Type.CUSTOM;
        }
    }


    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
        assignType();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Mail getMail() {
        return new Mail(itemStack.clone(), from);
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        PACK, LETTER, CUSTOM,
    }

}
