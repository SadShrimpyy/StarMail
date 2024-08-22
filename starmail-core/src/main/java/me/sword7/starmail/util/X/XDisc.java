package me.sword7.starmail.util.X;

import me.sword7.starmail.sys.Version;
import me.sword7.starmail.util.MailUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public enum XDisc {

    MUSIC_DISC_13(500, "GOLD_RECORD"),
    MUSIC_DISC_CAT(501, "GREEN_RECORD"),
    MUSIC_DISC_BLOCKS(502, "RECORD_3"),
    MUSIC_DISC_CHIRP(503, "RECORD_4"),
    MUSIC_DISC_FAR(504, "RECORD_5"),
    MUSIC_DISC_MALL(505, "RECORD_6"),
    MUSIC_DISC_MELLOHI(506, "RECORD_7"),
    MUSIC_DISC_STAL(507, "RECORD_8"),
    MUSIC_DISC_STRAD(508, "RECORD_9"),
    MUSIC_DISC_WARD(509, "RECORD_10"),
    MUSIC_DISC_11(510, "RECORD_11"),
    MUSIC_DISC_WAIT(511, "RECORD_12"),
    MUSIC_DISC_PIGSTEP(116, 759);

    private int version;
    private byte aByte;
    private Material material;
    private List<String> legacy = new ArrayList<>();

    XDisc(int version, int aByte) {
        this.aByte = (byte) aByte;
        this.version = version;
        this.material = parseMaterial();
    }

    XDisc(int aByte, String legacyOne) {
        this.aByte = (byte) aByte;
        this.version = 0;
        legacy.add(legacyOne);
        this.material = parseMaterial();
    }

    public Material parseMaterial() {
        Material material = MailUtil.materialFrom(this.toString());
        int index = 0;
        while (material == null && index < legacy.size()) {
            material = MailUtil.materialFrom(legacy.get(index));
            index++;
        }
        return material;
    }

    public Material getMaterial() {
        return material;
    }

    public int getByte() {
        return aByte;
    }

    public boolean isSupported() {
        return Version.current.value >= version;
    }

}
