package me.sword7.starmail.util;

import me.sword7.starmail.sys.Language;
import me.sword7.starmail.sys.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import static me.sword7.starmail.sys.Language.WARN_UNKNOWN_TYPE;

public class CommandLoot implements CommandExecutor {

    private ILootType lootType;

    public CommandLoot(ILootType lootType) {
        this.lootType = lootType;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (Permissions.canSummon(sender)) {
            if (args.length > 0) {
                String typeString = args[0].toUpperCase();
                ItemStack itemStack = lootType.getLootFrom(typeString);
                if (itemStack != null) {
                    if (args.length > 1) {
                        String argOne = args[1];
                        try {
                            int amount = Integer.parseInt(argOne);
                            if (amount > 255) amount = 255;
                            if (sender instanceof Player) {
                                giveLoot((Player) sender, itemStack, amount);
                            } else {
                                sendHelp(sender);
                            }
                        } catch (Exception e) {
                            Player player = Bukkit.getServer().getPlayer(argOne);
                            if (player != null) {
                                int amount = args.length > 2 ? MailUtil.parseAmount(args[2]) : 1;
                                if (amount > 255) amount = 255;
                                giveLoot(player, itemStack, amount);
                                sender.sendMessage(ChatColor.YELLOW + Language.SUCCESS_GIFT.fromPlayerAndAmount(player.getName(), amount));
                            } else {
                                player.sendMessage(ChatColor.RED + Language.WARN_PLAYER_NOT_FOUND.fromPlayer(argOne));
                            }
                        }
                    } else {
                        if (sender instanceof Player) {
                            giveLoot((Player) sender, itemStack, 1);
                        } else {
                            sendHelp(sender);
                        }
                    }
                } else {
                    if (args[0].equalsIgnoreCase("list")) {
                        sender.sendMessage(ChatColor.YELLOW + lootType.getListLabel() + ":");
                        for (String type : lootType.getLootTypes()) {
                            sender.sendMessage(ChatColor.GRAY + "- " + type);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + WARN_UNKNOWN_TYPE.toString());
                    }
                }
            } else {
                sendHelp(sender);
            }
        }

        return false;
    }

    private void giveLoot(Player player, ItemStack itemStack, int amount) {
        itemStack.setAmount(amount);
        Map<Integer, ItemStack> overflow = player.getInventory().addItem(itemStack);
        for (ItemStack overItem : overflow.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), overItem);
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + Language.INFO_FORMAT.fromFormat("/" + lootType.getRoot() + " [" + lootType.getType() + "] [" + Language.ARG_PLAYER + "] [" + Language.ARG_AMOUNT + "]"));
    }

}
