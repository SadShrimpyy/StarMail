package me.sword7.starmail.util.X;

import me.sword7.starmail.sys.Version;
import me.sword7.starmail.util.MailUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum XWool {

    BLACK(0),
    RED(1),
    GREEN(2),
    BROWN(3),
    BLUE(4),
    PURPLE(5),
    CYAN(6),
    LIGHT_GRAY(7),
    GRAY(8),
    PINK(9),
    LIME(10),
    YELLOW(11),
    LIGHT_BLUE(12),
    MAGENTA(13),
    ORANGE(14),
    WHITE(15),

    ;
    private byte aByte;

    XWool(int aByte) {
        this.aByte = (byte) aByte;
    }


    public Material parseMaterial() {
        Material material = MailUtil.materialFrom(this.toString() + "_WOOL");
        return material != null ? material : XMaterial.WHITE_WOOL.parseMaterial();
    }


    public ItemStack parseItemStack() {
        if (Version.current.hasExtendedEnums()) {
            return new ItemStack(parseMaterial());
        } else {
            return new ItemStack(parseMaterial(), 1, aByte);
        }
    }

    public byte getByte() {
        return aByte;
    }


}
