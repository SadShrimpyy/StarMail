package me.sword7.starmail.sys;

import me.sword7.starmail.sys.config.PluginConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Collection;

public class Permissions {

    private static String GLOBAL = "mail.*";

    private static String CRAFT = "mail.craft.*";
    private static String CRAFT_LETTER = "mail.craft.letter";
    private static String CRAFT_PACKAGE = "mail.craft.package.*";
    private static String CRAFT_CRATE = "mail.craft.package.crate";
    private static String CRAFT_CHEST = "mail.craft.package.chest";
    private static String CRAFT_GIFT = "mail.craft.package.gift";
    private static String CRAFT_BOX = "mail.craft.box";

    private static String WAREHOUSE = "mail.warehouse";
    private static String LOOT = "mail.loot";
    private static String CUSTOM = "mail.custom";
    private static String ACCESS = "mail.access";

    private static String EMAIL = "mail.email.*";
    private static String E_SEND = "mail.email.send";
    private static String E_BOX = "mail.email.box";

    private static String BLOCK = "mail.block.*";
    private static String BLOCK_MAILBOX = "mail.block.mailbox";
    private static String BLOCK_GLOBALBOX = "mail.block.globalbox";
    private static String BLOCK_POSTBOX = "mail.block.postbox";

    private static String BLACKLIST = "mail.blacklist";

    public static boolean canCraftLetter(Collection<HumanEntity> senders) {
        return canCraft(senders, CRAFT_LETTER);
    }

    private static boolean canCraftPackage(Collection<HumanEntity> senders, String permString) {
        return canCraft(senders, CRAFT_PACKAGE) || canCraft(senders, permString);
    }

    public static boolean canCraftCrate(Collection<HumanEntity> senders) {
        return canCraftPackage(senders, CRAFT_CRATE);
    }

    public static boolean canCraftChest(Collection<HumanEntity> senders) {
        return canCraftPackage(senders, CRAFT_CHEST);
    }

    public static boolean canCraftGift(Collection<HumanEntity> senders) {
        return canCraftPackage(senders, CRAFT_GIFT);
    }

    public static boolean canCraftBox(Collection<HumanEntity> senders) {
        return canCraft(senders, CRAFT_BOX);
    }

    private static boolean canCraft(Collection<HumanEntity> senders, String permString) {
        for (CommandSender sender : senders) {
            if (sender.hasPermission(GLOBAL) || sender.hasPermission(CRAFT) || sender.hasPermission(permString)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canAccess(CommandSender sender) {
        return hasPermission(sender, ACCESS);
    }

    public static boolean canSummon(CommandSender sender) {
        return hasPermission(sender, LOOT);
    }

    public static boolean canSendCustom(CommandSender sender) {
        return hasPermission(sender, CUSTOM);
    }

    public static int getMaxBoxes(CommandSender sender) {
        if (!sender.isOp()) {
            int boxAmount = PluginConfig.getMaxMailBoxes();
            for (PermissionAttachmentInfo info : sender.getEffectivePermissions()) {
                String perm = info.getPermission();
                try {
                    if (perm.startsWith("mail.boxes.")) {
                        int newAmount = Integer.parseInt(perm.split("ail\\.boxes\\.")[1]);
                        if (newAmount > boxAmount) boxAmount = newAmount;
                    }
                } catch (Exception e) {
                    //do nothing
                }
            }
            return boxAmount;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public static boolean canESend(CommandSender sender) {
        return canE(sender, E_SEND);
    }

    public static boolean canEBox(CommandSender sender) {
        return canE(sender, E_BOX);
    }

    private static boolean canE(CommandSender sender, String permString) {
        return sender.hasPermission(GLOBAL) || sender.hasPermission(EMAIL) || sender.hasPermission(permString);
    }

    public static boolean canMailboxBlock(CommandSender sender) {
        return canBlock(sender, BLOCK_MAILBOX);
    }

    public static boolean canGlobalboxBlock(CommandSender sender) {
        return canBlock(sender, BLOCK_GLOBALBOX);
    }

    public static boolean canPostboxBlock(CommandSender sender) {
        return canBlock(sender, BLOCK_POSTBOX);
    }

    private static boolean canBlock(CommandSender sender, String permString) {
        return !PluginConfig.isRequireBlockPermission() || sender.hasPermission(GLOBAL) || sender.hasPermission(BLOCK) || sender.hasPermission(permString);
    }

    public static boolean canWarehouse(CommandSender sender) {
        return hasPermission(sender, WAREHOUSE);
    }

    public static boolean canBlacklist(CommandSender sender) {
        return sender.hasPermission(GLOBAL) || sender.hasPermission("mail.blacklist");
    }

    private static boolean hasPermission(CommandSender sender, String permString) {
        return sender.hasPermission("mail.*") || sender.hasPermission(permString);
    }

    public static int getSendCooldown(Player player) {
        int cooldown = player.isOp() ? 0 : PluginConfig.getSendCooldown();
        if (cooldown > 0) {
            for (PermissionAttachmentInfo info : player.getEffectivePermissions()) {
                String perm = info.getPermission();
                try {
                    if (perm.startsWith("mail.cooldown.")) {
                        int newTime = Integer.parseInt(perm.split("ail\\.cooldown\\.")[1]);
                        if (newTime < cooldown) cooldown = newTime;
                    }
                } catch (Exception e) {
                    //do nothing
                }
            }
        }
        return cooldown;
    }
}
