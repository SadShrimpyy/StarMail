package me.sword7.starmail.util;

import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.X.XDye;
import me.sword7.starmail.util.X.XGlass;
import org.bukkit.ChatColor;

public enum MailColor {

    BLACK(Language.COLOR_BLACK, XDye.BLACK, XGlass.BLACK, ChatColor.DARK_GRAY, "\uE100"),
    RED(Language.COLOR_RED, XDye.RED, XGlass.RED, ChatColor.RED, "\uE101"),
    GREEN(Language.COLOR_GREEN, XDye.GREEN, XGlass.GREEN, ChatColor.DARK_GREEN, "\uE102"),
    BROWN(Language.COLOR_BROWN, XDye.BROWN, XGlass.BROWN, ChatColor.DARK_GRAY, "\uE103"),
    BLUE(Language.COLOR_BLUE, XDye.BLUE, XGlass.BLUE, ChatColor.BLUE, "\uE104"),
    PURPLE(Language.COLOR_PURPLE, XDye.PURPLE, XGlass.PURPLE, ChatColor.DARK_PURPLE, "\uE105"),
    CYAN(Language.COLOR_CYAN, XDye.CYAN, XGlass.CYAN, ChatColor.DARK_AQUA, "\uE106"),
    LIGHT_GRAY(Language.COLOR_LIGHT_GRAY, XDye.LIGHT_GRAY, XGlass.GRAY, ChatColor.GRAY, "\uE107"),
    GRAY(Language.COLOR_GRAY, XDye.GRAY, XGlass.GRAY, ChatColor.DARK_GRAY, "\uE108"),
    PINK(Language.COLOR_PINK, XDye.PINK, XGlass.PINK, ChatColor.RED, "\uE109"),
    LIME(Language.COLOR_LIME, XDye.LIME, XGlass.LIME, ChatColor.GREEN, "\uE10A"),
    YELLOW(Language.COLOR_YELLOW, XDye.YELLOW, XGlass.YELLOW, ChatColor.YELLOW, "\uE10B"),
    LIGHT_BLUE(Language.COLOR_LIGHT_BLUE, XDye.LIGHT_BLUE, XGlass.LIGHT_BLUE, ChatColor.AQUA, "\uE10C"),
    MAGENTA(Language.COLOR_MAGENTA, XDye.MAGENTA, XGlass.MAGENTA, ChatColor.LIGHT_PURPLE, "\uE10D"),
    ORANGE(Language.COLOR_ORANGE, XDye.ORANGE, XGlass.ORANGE, ChatColor.GOLD, "\uE10E"),
    WHITE(Language.COLOR_WHITE, XDye.WHITE, XGlass.WHITE, ChatColor.WHITE, "\uE10F"),

    ;
    private Language language;
    private XDye xDye;
    private XGlass xGlass;
    private ChatColor chatColor;
    private String boxSymbol;

    MailColor(Language language, XDye xDye, XGlass xGlass, ChatColor chatColor, String boxSymbol) {
        this.language = language;
        this.xDye = xDye;
        this.xGlass = xGlass;
        this.chatColor = chatColor;
        this.boxSymbol = boxSymbol;
    }

    public Language getLanguage() {
        return language;
    }

    public XDye getXDye() {
        return xDye;
    }

    public XGlass getXGlass() {
        return xGlass;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public String getBoxSymbol() {
        return boxSymbol;
    }

    public static MailColor fromString(String s) {
        try {
            return valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

}
