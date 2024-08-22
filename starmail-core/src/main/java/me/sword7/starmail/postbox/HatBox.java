package me.sword7.starmail.postbox;

import me.sword7.starmail.util.X.XGlass;

import java.util.UUID;

public class HatBox extends Postbox {

    private XGlass hatBase;
    private XGlass hatHighlight;

    public HatBox(PostboxType type, String name, XGlass xGlass, XGlass hatBase, XGlass hatHighlight, UUID profileID, String data) {
        super(type, name, xGlass, profileID, data);
        this.hatBase = hatBase;
        this.hatHighlight = hatHighlight;
    }

    public XGlass getHatBase() {
        return hatBase;
    }

    public XGlass getHatHighlight() {
        return hatHighlight;
    }
}
