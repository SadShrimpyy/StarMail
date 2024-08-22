package me.sword7.starmail.util.X;

import me.sword7.starmail.sys.Version;
import me.sword7.starmail.util.MailUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum XDye {

    BLACK(0, "INK_SAC"),
    RED(1),
    GREEN(2),
    BROWN(3, "COCOA", "COCOA_BEANS"),
    BLUE(4, "LAPIS_LAZULI"),
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
    WHITE(15, "BONE_MEAL"),

            ;
    private List<String> legacy = new ArrayList<>();
    private byte aByte;

    XDye(int aByte) {
        this.aByte = (byte) aByte;
    }

    XDye(int aByte, String legacyOne) {
        this.aByte = (byte) aByte;
        legacy.add(legacyOne);
    }

    XDye(int aByte, String legacyOne, String legacyTwo) {
        this.aByte = (byte) aByte;
        if (aByte == 3) {
            int currentValue = Version.current.value;
            if (currentValue >= 113) {
                legacy.add(legacyTwo);
                legacy.add(legacyOne);
            }
        }
    }

    public Material parseMaterial() {
        Material material = MailUtil.materialFrom(this.toString() + "_DYE");
        int index = 0;
        while (material == null && index < legacy.size()) {
            material = MailUtil.materialFrom(legacy.get(index));
            index++;
        }
        return material != null ? material : XMaterial.INK_SAC.parseMaterial();
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
