package me.sword7.starmail.util.X;

import me.sword7.starmail.sys.Version;
import me.sword7.starmail.util.MailUtil;
import org.bukkit.Material;

public enum XPlanks {

    OAK(0),
    SPRUCE(1),
    BIRCH(2),
    JUNGLE(3),
    ACACIA(4),
    DARK_OAK(5),
    CRIMSON(116, 6),
    WARPED(116, 7),

    ;

    private byte abyte;
    private int version;

    XPlanks(int abyte) {
        this.version = 0;
        this.abyte = (byte) abyte;
    }

    XPlanks(int version, int abyte) {
        this.version = version;
        this.abyte = (byte) abyte;
    }

    public Material parseMaterial() {
        Material material = MailUtil.materialFrom(this.toString() + "_PLANKS");
        return material != null ? material : XMaterial.OAK_PLANKS.parseMaterial();
    }

    public Material parseSlabMaterial() {
        Material material = MailUtil.materialFrom(this.toString() + "_SLAB");
        if(material == null) material = MailUtil.materialFrom("WOOD_STEP");
        if(material == null) material = MailUtil.materialFrom("WOODED_SLAB");
        return material != null ? material : XMaterial.OAK_SLAB.parseMaterial();
    }

    public byte getByte() {
        return abyte;
    }

    public boolean isSupported() {
        return Version.current.value >= this.version;
    }


}
