package me.sword7.starmail.sys;

import org.bukkit.Bukkit;

public enum Version {

    V1_21(121),
    V1_20(120),
    V1_19(119),
    V1_18(118),
    V1_17(117),
    V1_16(116),
    V1_15(115),
    V1_14(114),
    V1_13(113),
    V1_12(112),
    V1_11(111),
    V1_10(110),
    V1_9(109),
    V1_8(108),
    UNKNOWN(0),
    ;

    public static Version current = getVersion();

    private static Version getVersion() {
        String versionString = Bukkit.getVersion();
        if (versionString.contains("1.21")) return V1_21;
        else if (versionString.contains("1.20")) return V1_20;
        else if (versionString.contains("1.19")) return V1_19;
        else if (versionString.contains("1.18")) return V1_18;
        else if (versionString.contains("1.17")) return V1_17;
        else if (versionString.contains("1.16")) return V1_16;
        else if (versionString.contains("1.15")) return V1_15;
        else if (versionString.contains("1.14")) return V1_14;
        else if (versionString.contains("1.13")) return V1_13;
        else if (versionString.contains("1.12")) return V1_12;
        else if (versionString.contains("1.11")) return V1_11;
        else if (versionString.contains("1.10")) return V1_10;
        else if (versionString.contains("1.9")) return V1_9;
        else if (versionString.contains("1.8")) return V1_8;
        else return UNKNOWN;
    }

    public final int value;

    Version(int value) {
        this.value = value;
    }

    public boolean isMethodCreatePlayerProfileSupported() {
        return value >= 121;
    }

    public boolean isAutoCompleteSupported() {
        return value >= 109;
    }

    public boolean isModelDataSupported() {
        return value >= 114;
    }

    public boolean hasOneHand() {
        return value <= 108;
    }

    public boolean hasLetter() {
        return isModelDataSupported();
    }

    public boolean hasNamespaceKey() {
        return value >= 112;
    }

    public boolean hasExtendedEnums() {
        return value >= 113;
    }

    public boolean isNormalItemConsume() {
        return value >= 111;
    }

    public boolean hasOffhand() {
        return value >= 109;
    }

}
