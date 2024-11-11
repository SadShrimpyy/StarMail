package me.sword7.starmail.blacklist;

import me.sword7.starmail.sys.Language;
import me.sword7.starmail.sys.Permissions;
import me.sword7.starmail.sys.PluginHelp;
import me.sword7.starmail.sys.config.BlacklistConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static me.sword7.starmail.sys.Language.INFO_FORMAT;

public class CommandBlacklist implements CommandExecutor {

    private Player p;
    private String exception;;
    private BlacklistSplits splits;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        p = Bukkit.getServer().getPlayer(sender.getName());
        if (!Permissions.canBlacklist(sender)) {
            sender.sendMessage(ChatColor.RED + Language.WARN_NOT_PERMITTED.toString());
            return false;
        }

        if (args.length == 0) {
            PluginHelp.sendBlacklistHelp(sender);
            return false;
        }

        if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                sendBlacklistFormat(sender, args);
                return false;
            }
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case ("add"):
                splits = new BlacklistSplits();
                addNewItem(sender, args);
                break;
            case ("remove"):
                splits = new BlacklistSplits();
                removeItem(sender, args);
                break;
            case ("reload"):
                BlacklistConfig.reload();
                sender.sendMessage(ChatColor.YELLOW + Language.INFO_BLACKLIST_RELOADED.toString());
                break;
            case ("list"):
                listItems(sender, args);
                break;
            default:
                PluginHelp.sendBlacklistHelp(sender);
                break;
        }

        return false;
    }

    @SuppressWarnings("unused")
    private void addNewItem(CommandSender sender, String[] args) {
        if (isConsole(sender)) {
            return;
        }

        splits.requestNewSplits(args[1]);
        if (splits.isEmpty()) {
            sendBlacklistFormat(sender, args);
            return;
        }
        boolean isHash = isHash(args);

        final ItemStack clone = p.getInventory().getItemInMainHand().clone();
        if (clone.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + Language.WARN_INVALID_ITEM.toString());
            return;
        }

        clone.setAmount(1);
        BlacklistConfig.reload();
        if ((exception = splits.getFromAnalysedLine(isHash, clone)) == null) {
            sender.sendMessage(ChatColor.RED + Language.WARN_INVALID_ITEM.toString() + ": need valid attribute(s)!");
            return;
        }

        if (!BlacklistConfig.contains(exception)) {
            sender.sendMessage(ChatColor.YELLOW + Language.SUCCESS_ADDED_ITEM_BLACKLIST.replaceHash(exception));
            BlacklistConfig.addExceptLine(exception);
        } else {
            sender.sendMessage(ChatColor.YELLOW + Language.WARN_ITEM_DUPLICATED_BLACKLIST.replaceHash(exception));
        }
    }

    @SuppressWarnings("unused")
    private void listItems(CommandSender sender, String[] args) {
        BlacklistConfig.reload();
        sender.sendMessage(ChatColor.YELLOW + Language.INFO_ITEM_FOUNDED_BLACKLIST.toString());
        for (int count = 0; count < BlacklistConfig.getList().length; count++) {
            sender.sendMessage(ChatColor.GRAY + Language.INFO_ITEM_FOUND_BLACKLIST.fromIndexAndItem(
                    count, BlacklistConfig.getAt(count)));
        }
    }

    @SuppressWarnings("unused")
    private void removeItem(CommandSender sender, String[] args) {
        if (isConsole(sender)) return;

        splits.requestNewSplits(args[1]);
        if (splits.isEmpty()) {
            sendBlacklistFormat(sender, args);
            return;
        }
        boolean isHash = isHash(args);

        final ItemStack clone = p.getInventory().getItemInMainHand().clone();
        if (clone.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + Language.WARN_INVALID_ITEM.toString());
            return;
        }

        clone.setAmount(1);
        BlacklistConfig.reload();
        if ((exception = splits.getFromAnalysedLine(isHash, clone)) == null) {
            sender.sendMessage(ChatColor.RED + Language.WARN_INVALID_ITEM.toString() + ": need valid attribute(s)!");
            return;
        }
        if (BlacklistConfig.contains(exception)) {
            sender.sendMessage(ChatColor.YELLOW + Language.SUCCESS_REMOVED_ITEM_BLACKLIST.replaceHash(exception));
            BlacklistConfig.removeHashCode(exception);
        } else {
            sender.sendMessage(ChatColor.YELLOW + Language.WARN_ITEM_UNFOUNDED_BLACKLIST.replaceHash(exception));
        }
    }

    private static boolean isHash(String[] args) {
        return args.length > 2 && args[2].equalsIgnoreCase("hash");
    }

    private static boolean isConsole(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.RED + Language.WARN_CONSOLE_NOT_SUPPORTED.toString());
            return true;
        }
        return false;
    }

    private static void sendBlacklistFormat(@NotNull CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.RED + INFO_FORMAT.fromFormat("/blacklist " + args[0] + " " + Language.ARG_ALL_PIPES + " <" +  Language.ARG_HASH + ">"));
    }
}
