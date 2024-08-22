package me.sword7.starmail.util;

import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.X.XGlass;
import me.sword7.starmail.util.X.XPlanks;

public enum MailWood {

    OAK(Language.WOOD_OAK, XPlanks.OAK, XGlass.BROWN),
    SPRUCE(Language.WOOD_SPRUCE, XPlanks.SPRUCE, XGlass.BROWN),
    BIRCH(Language.WOOD_BIRCH, XPlanks.BIRCH, XGlass.BROWN),
    JUNGLE(Language.WOOD_JUNGLE, XPlanks.JUNGLE, XGlass.BROWN),
    ACACIA(Language.WOOD_ACACIA, XPlanks.ACACIA, XGlass.BROWN),
    DARK_OAK(Language.WOOD_DARK_OAK, XPlanks.DARK_OAK, XGlass.BROWN),
    CRIMSON(Language.WOOD_CRIMSON, XPlanks.CRIMSON, XGlass.RED),
    WARPED(Language.WOOD_WARPED, XPlanks.WARPED, XGlass.CYAN),

    ;

    private Language language;
    private XPlanks xPlanks;
    private XGlass xGlass;

    MailWood(Language language, XPlanks xPlanks, XGlass xGlass) {
        this.language = language;
        this.xPlanks = xPlanks;
        this.xGlass = xGlass;
    }

    public Language getLanguage() {
        return language;
    }

    public XPlanks getXPlanks() {
        return xPlanks;
    }

    public XGlass getXGlass() {
        return xGlass;
    }
}
