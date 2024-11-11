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

public class CommandBlacklist implements CommandExecutor {

    private Player p;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        p = Bukkit.getServer().getPlayer(sender.getName());
        if (!Permissions.canBlacklist(sender)) {
            sender.sendMessage(ChatColor.RED + Language.WARN_NOT_PERMITTED.toString());
            return false;
        }

        if (args.length < 1) {
            PluginHelp.sendBlacklistHelp(sender);
            return false;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case ("add"):
                addNewItem(sender, args);
                break;
            case ("remove"):
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
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.RED + Language.WARN_CONSOLE_NOT_SUPPORTED.toString());
            return;
        }

        final ItemStack clone = p.getInventory().getItemInMainHand().clone();
        if (clone.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + Language.WARN_INVALID_ITEM.toString());
            return;
        }

        clone.setAmount(1);
        final long hashCode = clone.hashCode();
        BlacklistConfig.reload();
        if (!BlacklistConfig.contains(hashCode)) {
            sender.sendMessage(ChatColor.YELLOW + Language.SUCCESS_ADDED_ITEM_BLACKLIST.replaceHash(hashCode));
            BlacklistConfig.addHashCode(hashCode, clone.getType().name());
        } else {
            sender.sendMessage(ChatColor.YELLOW + Language.WARN_ITEM_DUPLICATED_BLACKLIST.replaceHash(hashCode));
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
        final ItemStack clone = p.getInventory().getItemInMainHand().clone();
        if (clone.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + Language.WARN_INVALID_ITEM.toString());
            return;
        }

        clone.setAmount(1);
        BlacklistConfig.reload();
        final long hashCode = clone.hashCode();
        if (BlacklistConfig.contains(hashCode)) {
            sender.sendMessage(ChatColor.YELLOW + Language.SUCCESS_REMOVED_ITEM_BLACKLIST.replaceHash(hashCode));
            BlacklistConfig.removeHashCode(hashCode);
        } else {
            sender.sendMessage(ChatColor.YELLOW + Language.WARN_ITEM_UNFOUNDED_BLACKLIST.replaceHash(hashCode));
        }
    }
}
