package me.sword7.starmail.util.X;

import me.sword7.starmail.sys.Version;
import me.sword7.starmail.util.MailUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum XGlass {

    WHITE(0, ChatColor.WHITE),
    ORANGE(1, ChatColor.GOLD),
    MAGENTA(2, ChatColor.LIGHT_PURPLE),
    LIGHT_BLUE(3, ChatColor.AQUA),
    YELLOW(4, ChatColor.YELLOW),
    LIME(5, ChatColor.GREEN),
    PINK(6, ChatColor.RED),
    GRAY(7, ChatColor.DARK_GRAY),
    LIGHT_GRAY(8, ChatColor.GRAY),
    CYAN(9, ChatColor.DARK_AQUA),
    PURPLE(10, ChatColor.DARK_PURPLE),
    BLUE(11, ChatColor.BLUE),
    BROWN(12, ChatColor.DARK_GRAY),
    GREEN(13, ChatColor.DARK_GREEN),
    RED(14, ChatColor.DARK_RED),
    BLACK(15, ChatColor.DARK_GRAY),

    ;

    private int abyte;
    private ChatColor color;
    private Material material;
    private ItemStack itemStack;
    private ItemStack dot;
    private ItemStack swiggle;

    XGlass(int abyte, ChatColor color) {
        this.abyte = abyte;
        this.color = color;
        this.material = parseMaterial();
        this.itemStack = parseItemStack();
        this.dot = getCustom(color, "•");
        this.swiggle = getCustom(color, "~");
    }

    public Material parseMaterial() {
        Material material = MailUtil.materialFrom(this.toString() + "_STAINED_GLASS_PANE");
        return material != null ? material : XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial();
    }

    public ItemStack parseItemStack() {
        if (Version.current.hasExtendedEnums()) {
            return new ItemStack(material);
        } else {
            return new ItemStack(material, 1, (short) abyte);
        }
    }

    public int getAbyte() {
        return abyte;
    }

    public ChatColor getColor() {
        return color;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemStack getDot() {
        return dot;
    }

    public ItemStack getSwiggle() {
        return swiggle;
    }

    public ItemStack getCustom(ChatColor color, String name) {
        ItemStack custom = itemStack.clone();
        ItemMeta meta = custom.getItemMeta();
        meta.setDisplayName(color + name);
        custom.setItemMeta(meta);
        return custom;
    }

}
