package me.sword7.starmail.sys;

import me.sword7.starmail.sys.config.PluginConfig;
import me.sword7.starmail.util.storage.StorageUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public enum Language {

    LABEL_COMMANDS("Label - commands", "Commands"),
    LABEL_PAGE("Label - page", "Page"),
    LABEL_CONTENTS("Label - contents", "Contents"),
    LABEL_LETTER("Label - letter", "Letter"),
    LABEL_LETTER_AND_QUILL("Label - letter and quill", "Letter and Quill"),
    LABEL_DYED_LETTER_AND_QUILL("Label - dyed letter and quill", "%color% Letter and Quill"),
    LABEL_FANCY_LETTER_AND_QUILL("Label - fancy letter and quill", "Fancy %color% Letter and Quill"),
    LABEL_POSTBOX("Label - postbox", "Postbox"),
    LABEL_MAILBOX("Label - mailbox", "Mailbox"),
    LABEL_DYED_MAILBOX("Label - dyed mailbox", "%color% Mailbox"),
    LABEL_NOTIFICATIONS("Label - notifications", "Notifications"),
    LABEL_PACKAGE("Label - package", "Package"),
    LABEL_CRATE("Label - crate", "Crate"),
    LABEL_WOODEN_CRATE("Label - wooden crate", "%wood% Crate"),
    LABEL_FANCY_CRATE("Label - fancy crate", "Fancy %color% Crate"),
    LABEL_CHEST("Label - chest", "Chest"),
    LABEL_WOODEN_CHEST("Label - wooden chest", "%wood% Chest"),
    LABEL_FANCY_CHEST("Label - fancy chest", "Fancy %color% Chest"),
    LABEL_GIFT("Label - gift", "Gift"),
    LABEL_DYED_GIFT("Label - dyed gift", "%color% Gift"),
    LABEL_FROM("Label - from", "From"),
    LABEL_WORLD("Label - world", "World"),
    LABEL_BOX_LOCATIONS("Label - box locations", "Mailbox Locaitons"),
    LABEL_TRACKING_NO("Label - tracking number", "TrackingNo"),
    LABEL_POSTBOX_TYPES("Label - postbox types", "Postbox Types"),
    LABEL_BOX_TYPES("Label - box types", "Box Types"),
    LABEL_LETTER_TYPES("Label - letter types", "Letter Types"),
    LABEL_PACK_TYPES("Label - pack types", "Package Types"),
    LABEL_ENABLED("Label - enabled", "Enabled"),
    LABEL_DISABLED("Label - disabled", "Disabled"),
    LABEL_SERVER("Label - server", "Server"),
    LABEL_WAREHOUSE("Label - warehouse", "Warehouse"),
    LABEL_BLACKLIST("Label - blacklist", "Blacklist"),
    LABEL_WAREHOUSE_ENTRIES("Label - warehouse entries", "Warehouse Entries"),
    LABEL_ITEM("Label - item", "Item"),
    LABEL_STYLE("Label - style", "Style"),
    LABEL_EXPIRED("Label - expired", "TrackingNo Expired"),

    TEXT_INTRO("Text - intro", "Adds mailboxes, letters, and packages.\\nRead more on the wiki: https://github.com/SadShrimpyy/StarMail/wiki"),
    TEXT_BOXES("Text - boxes", "list all mailbox locations"),
    TEXT_BREAK("Text - breakboxes", "break all mailboxes"),
    TEXT_MAIL("Text - mail", "open virtual mailbox"),
    TEXT_SENDTO("Text - sendto", "send mail to player"),
    TEXT_SUMMON("Text - summon item", "give items to player"),
    TEXT_WAREHOUSE_SEND("Text - warehouse send", "send mail to player"),
    TEXT_WAREHOUSE_SAVE("Text - warehouse save", "save hand item to warehouse"),
    TEXT_WAREHOUSE_RENAME("Text - warehouse rename", "rename warehouse entry"),
    TEXT_WAREHOUSE_EDIT("Text - warehouse edit", "edit warehouse entry"),
    TEXT_WAREHOUSE_DELETE("Text - warehouse list", "list all warehouse entries"),
    TEXT_WAREHOUSE_LIST("Text - warehouse delete", "delete warehouse entry"),
    TEXT_BLACKLIST_ADD("Text - blacklist add", "add the held item to the blacklist"),
    TEXT_BLACKLIST_REMOVE("Text - blacklist remove", "remove the held item to the blacklist"),
    TEXT_BLACKLIST_RELOAD("Text - blacklist remove", "reload the in-game configuration lists, to match the file one"),
    TEXT_BLACKLIST_LIST("Text - blacklist list", "list all the item blacklisted"),

    CONST_UNKNOWN("Const - unknown", "Unknown"),

    ARG_PLAYER("Arg - player", "player"),
    ARG_TYPE("Arg - type", "type"),
    ARG_AMOUNT("Arg - amount", "amount"),
    ARG_MAIL("Arg - mail", "mail"),
    ARG_NAME("Arg - name", "name"),
    ARG_FROM("Arg - from", "from"),
    ARG_TO("Arg - to", "to"),

    SUCCESS_SENT("Success - sent", "Mail sent to %player%"),
    SUCCESS_SENT_SHORT("Seccess - sent short", "Mail Sent!"),
    SUCCESS_GIFT("Success - gift", "%amount% items sent to %player%"),
    SUCCESS_BREAK("Success - break", "Mailboxes broken"),
    SUCCESS_WAREHOUSE_CREATE("Success - warehouse create", "Entry added"),
    SUCCESS_WAREHOUSE_RENAME("Success - warehouse rename", "Entry renamed"),
    SUCCESS_WAREHOUSE_DELETE("Success - warehouse delete", "Entry deleted"),
    SUCCESS_ADDED_ITEM_BLACKLIST("Added item to blacklist", "&eAdded item to blacklist with hash-code: %hash%"),
    SUCCESS_REMOVED_ITEM_BLACKLIST("Removed item to blacklist", "&eRemoved item to blacklist with hash-code: %hash%"),

    WARN_NOT_PERMITTED("Warn - no permission", "You do not have permission for this command."),
    WARN_NOT_PERMITTED_BLOCK("Warn - no permission block", "You do not have permission to use this block."),
    WARN_NOT_MAIL("Warn - not mail", "You are not holding any mail."),
    WARN_BOX_LIMIT("Warn - box limit", "You can not place any more mailboxes."),
    WARN_PLAYER_NOT_FOUND("Warn - player not found", "Player %player% not found."),
    WARN_UNKNOWN_TYPE("Warn - unknown type", "Unknown type."),
    WARN_TRACKING_EXPIRED("Warn - expired", "Tracking number is expired."),
    WARN_EMPTY_SEAL("Warn - empty seal", "Sealed package can not be empty."),
    WARN_FRACTAL_SEAL("Warn - fractal seal", "Sealed package can not contain another package."),
    WARN_BLACKLIST_WORLD("Warn - blacklist world", "Mailboxes are not allowed in this world."),
    WARN_CONSOLE_NOT_SUPPORTED("Warn - console not supported", "This command can not be used from the console."),
    WARN_ENTRY_OPEN("Warn - entry open", "%entry% is being edited by %player%"),
    WARN_COOLING("Warn - cooling", "Please wait before sending another item. (%seconds%s)"),
    WARN_COOLING_SHORT("Warn - cooling short", "Please wait (%seconds%s)"),
    WARN_INVALID_MAIL("Warn - invalid mail", "Invalid Mail"),
    WARN_INVALID_ITEM("Warn - invalid item", "Invalid Item"),
    WARN_ITEM_DUPLICATED_BLACKLIST("Found duplicated item to blacklist", "&eThe item you want to add is yet present in the blacklist (hash-code: %hash%). Skipping!"),
    WARN_ITEM_UNFOUNDED_BLACKLIST("Item to blacklist not found", "&eThe item you want to remove isn't present in the blacklist (hash-code: %hash%). Skipping!"),

    INFO_FORMAT("Info - format", "Format is %format%"),
    INFO_BOX("Info - box", "Mailbox ~ %player% ~"),
    INFO_MAIL("Info - mail", "&e[Mailbox] &7You have &6%amount% &7items in your mailbox."),
    INFO_RECEIVE("Info - receive", "&e[Mailbox] &7You received mail from %player%."),
    INFO_RECEIVE_MULTI("Info - receive multi", "&e[Mailbox] &7You received &6%amount% &7new items."),
    INFO_ITEM_FOUNDED_BLACKLIST("Info - item founded in blacklist", "&e[Blacklist] &7Listing all the items founded in the Blacklist:"),
    INFO_ITEM_FOUND_BLACKLIST("Info - specific item found in blacklist", "&e %item-count%) &6%item-hash%"),
    INFO_BLACKLIST_RELOADED("Info - blacklist's config reloaded", "&e[Blacklist] Configuration reloaded"),

    CONSOLE_PLUGIN_DETECT("Console - plugin detect", "%plugin% detected"),

    MISC_OVERFLOW("Misc - item overflow", "and %amount% more..."),
    MISC_DATE("Misc - date", "MM/dd/yyy"),
    MISC_SPECIAL_CHAR("Misc - special characters", "Names must consist of only a-z, A-Z, 0-9, _, and -"),
    MISC_TOO_LONG("Misc - too long", "Names must be 20 characters or less"),

    WOOD_OAK("Wood - oak", "Oak"),
    WOOD_SPRUCE("Wood - spruce", "Spruce"),
    WOOD_BIRCH("Wood - birch", "Birch"),
    WOOD_JUNGLE("Wood - jungle", "Jungle"),
    WOOD_ACACIA("Wood - acacia", "Acacia"),
    WOOD_DARK_OAK("Wood - dark oak", "Dark Oak"),
    WOOD_CRIMSON("Wood - crimson", "Crimson"),
    WOOD_WARPED("Wood - warped", "Warped"),

    COLOR_BLACK("Color - black", "Black"),
    COLOR_RED("Color - red", "Red"),
    COLOR_GREEN("Color - green", "Green"),
    COLOR_BROWN("Color - brown", "Brown"),
    COLOR_BLUE("Color - blue", "Blue"),
    COLOR_PURPLE("Color - purple", "Purple"),
    COLOR_CYAN("Color - cyan", "Cyan"),
    COLOR_LIGHT_GRAY("Color - light gray", "Light Gray"),
    COLOR_GRAY("Color - gray", "Gray"),
    COLOR_PINK("Color - pink", "Pink"),
    COLOR_LIME("Color - lime", "Lime"),
    COLOR_YELLOW("Color - yellow", "Yellow"),
    COLOR_LIGHT_BLUE("Color - light blue", "Light Blue"),
    COLOR_MAGENTA("Color - magenta", "Magenta"),
    COLOR_ORANGE("Color - orange", "Orange"),
    COLOR_WHITE("Color - white", "White"),

    LORE_ID_EMPTY_PACKAGE("Lore ID - empty package", "Empty"),
    LORE_ID_SEALED_PACKAGE("Lore ID - sealed package", "Sealed"),
    LORE_ID_GLOBAL_BOX("Lore ID - global box", "Global"),

    ICON_BACK("Icon - back", "Back"),
    ICON_CLOSE("Icon - close", "Close"),
    ICON_SEAL("Icon - seal", "Seal"),
    ICON_SEAL_LORE("Icon - seal lore", "» Click to open package"),
    ICON_COLLECT("Icon - collect", "Collect All"),
    ICON_SCROLL_UP("Icon - scroll up", "Scroll Up"),
    ICON_SCROLL_DOWN("Icon - scroll down", "Scroll Down"),
    ICON_DESTROY("Icon - destroy", "Destroy Item"),
    ICON_ON_JOIN("Icon - on join", "on join"),
    ICON_ON_RECEIVE("Icon - on receive", "on receive mail"),
    ICON_PLAYER_MAILBOX("Icon - player mailbox", "%player%'s Mailbox"),
    ICON_INSERT_MAIL("Icon - insert mail", "Insert Mail"),
    ICON_TO("Icon - to", "To: "),
    ICON_SEND_MAIL("Icon - send mail", "Send Mail"),
    ICON_BACKSPACE("Icon - backspace", "Backspace"),
    ICON_SEND_LORE("Icon - send lore", "» Click to send mail"),
    ICON_APPLY("Icon - apply", "Apply changes"),
    ;


    private static File file = new File("plugins/StarMail/Locale", PluginConfig.getLanguageFile() + ".yml");
    private static FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    public static void load() {
        if (file.exists()) {
            try {
                for (Language message : Language.values()) {
                    MessageSetting setting = message.getMessageSetting();
                    if (config.contains(setting.getLabel())) {
                        setting.setMessage(config.getString(setting.getLabel(), setting.getMessage()));
                    } else {
                        config.set(setting.getLabel(), setting.getMessage());
                    }
                }
                StorageUtil.save(config, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class MessageSetting {

        private String label;
        private String message;

        public MessageSetting(String label, String message) {
            this.label = label;
            this.message = message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getLabel() {
            return label;
        }

        public String getMessage() {
            return message;
        }

    }


    private MessageSetting messageSetting;

    Language(String messageSetting, String messageDefault) {
        this.messageSetting = new MessageSetting(messageSetting, messageDefault);
    }

    public MessageSetting getMessageSetting() {
        return messageSetting;
    }

    @Override
    public String toString() {
        return get();
    }

    private String get() {
        return ChatColor.translateAlternateColorCodes('&', messageSetting.getMessage());
    }

    public String fromPlayer(String playerName) {
        return get().replaceAll("%player%", playerName);
    }

    public String fromIndexAndItem(int count, long hashCode) {
        return get().replace("%item-count%", Integer.toString(count + 1))
                .replace("%item-hash%", String.valueOf(hashCode));

    }

    public String replaceHash(long hashCode) {
        return get().replace("%hash%", String.valueOf(hashCode));
    }

    public String fromAmount(int amount) {
        return get().replaceAll("%amount%", String.valueOf(amount));
    }

    public String fromFormat(String format) {
        return get().replaceAll("%format%", format);
    }

    public String fromPlugin(String plugin) {
        return get().replaceAll("%plugin%", plugin);
    }


    public String fromPlayerAndAmount(String playerName, int amount) {
        return get().replaceAll("%player%", playerName).replaceAll("%amount%", String.valueOf(amount));
    }

    public String fromColor(Language color) {
        return get().replaceAll("%color%", color.toString());
    }

    public String fromWood(Language wood) {
        return get().replaceAll("%wood%", wood.toString());
    }

    public String fromEntryAndPlayer(String entry, String player) {
        return get().replaceAll("%entry%", entry).replaceAll("%player%", player);
    }

    public String fromSeconds(int seconds) {
        return get().replaceAll("%seconds%", String.valueOf(seconds));
    }


    public String[] getLines() {
        return get().replaceAll("\\\\n\\\\n", "\\\\n \\\\n").split("\\\\n");
    }

}
