package me.sword7.starmail.warehouse;

import me.sword7.starmail.gui.LiveSessions;
import me.sword7.starmail.post.PostCache;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.sys.Permissions;
import me.sword7.starmail.sys.PluginHelp;
import me.sword7.starmail.sys.Version;
import me.sword7.starmail.user.User;
import me.sword7.starmail.user.UserCache;
import me.sword7.starmail.util.NameValidation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class CommandWarehouse implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (Permissions.canWarehouse(sender)) {
            if (args.length > 0) {
                String subCommand = args[0];
                if (subCommand.equalsIgnoreCase("help")) {
                    PluginHelp.sendWarehouseHelp(sender);
                } else if (subCommand.equalsIgnoreCase("send")) {
                    processSend(sender, args);
                } else if (subCommand.equalsIgnoreCase("save")) {
                    processSave(sender, args);
                } else if (subCommand.equalsIgnoreCase("edit")) {
                    processEdit(sender, args);
                } else if (subCommand.equalsIgnoreCase("rename")) {
                    processRename(sender, args);
                } else if (subCommand.equalsIgnoreCase("delete")) {
                    processDelete(sender, args);
                } else if (subCommand.equalsIgnoreCase("list")) {
                    processList(sender);
                } else {
                    PluginHelp.sendWarehouseHelp(sender);
                }
            } else {
                PluginHelp.sendWarehouseHelp(sender);
            }
        } else {
            sender.sendMessage(ChatColor.RED + Language.WARN_NOT_PERMITTED.toString());
        }

        return false;
    }

    private void processSend(CommandSender sender, String[] args) {
        if (args.length > 2) {
            String name = args[1];
            String entryName = args[2].toUpperCase();
            UserCache.getUser(name, (User target) -> {
                if (target != null) {
                    WarehouseEntry warehouseEntry = WarehouseCache.getEntry(entryName);
                    if (warehouseEntry != null) {
                        PostCache.send(target.getID(), warehouseEntry.getMail());
                        sender.sendMessage(ChatColor.YELLOW + Language.SUCCESS_SENT.fromPlayer(target.getName()));
                    } else {
                        sender.sendMessage(ChatColor.RED + Language.WARN_UNKNOWN_TYPE.toString());
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + Language.WARN_PLAYER_NOT_FOUND.fromPlayer(name));
                }
            });


        } else {
            sender.sendMessage(ChatColor.RED + Language.INFO_FORMAT.fromFormat("/warehouse send [" + Language.ARG_PLAYER + "] [" + Language.ARG_TYPE + "]"));
        }
    }

    private void processSave(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = Version.current.hasOffhand() ? inventory.getItemInMainHand() : inventory.getItemInHand();
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                if (args.length > 1) {
                    String name = args[1].toUpperCase();
                    NameValidation.Status status = NameValidation.clean(name);
                    if (status == NameValidation.Status.VALID) {
                        WarehouseCache.register(name, new WarehouseEntry(itemStack));
                        player.sendMessage(ChatColor.YELLOW + Language.SUCCESS_WAREHOUSE_CREATE.toString());
                    } else {
                        player.sendMessage(ChatColor.RED + status.message);
                    }
                } else {
                    WarehouseCache.register(new WarehouseEntry(itemStack));
                    player.sendMessage(ChatColor.YELLOW + Language.SUCCESS_WAREHOUSE_CREATE.toString());
                }
            } else {
                player.sendMessage(ChatColor.RED + Language.WARN_NOT_MAIL.toString());
            }
        } else {
            sender.sendMessage(ChatColor.RED + Language.WARN_CONSOLE_NOT_SUPPORTED.toString());
        }
    }

    private void processEdit(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 1) {
                String type = args[1].toUpperCase();
                WarehouseEntry entry = WarehouseCache.getEntry(type);
                if (entry != null) {
                    String editor = LiveSessions.getEntryEditor(type);
                    if (editor == null) {
                        LiveSessions.launchWarehouse(player, type, entry);
                    } else {
                        sender.sendMessage(ChatColor.RED + Language.WARN_ENTRY_OPEN.fromEntryAndPlayer(type, editor));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + Language.WARN_UNKNOWN_TYPE.toString());
                }
            } else {
                sender.sendMessage(ChatColor.RED + Language.INFO_FORMAT.fromFormat("/warehouse edit [" + Language.ARG_TYPE + "]"));
            }
        } else {
            sender.sendMessage(ChatColor.RED + Language.WARN_CONSOLE_NOT_SUPPORTED.toString());
        }

    }

    private void processRename(CommandSender sender, String[] args) {
        if (args.length > 2) {
            String type = args[1].toUpperCase();
            WarehouseEntry entry = WarehouseCache.getEntry(type);
            if (entry != null) {
                String to = args[2].toUpperCase();
                NameValidation.Status status = NameValidation.clean(to);
                if (status == NameValidation.Status.VALID) {
                    String editor = LiveSessions.getEntryEditor(type);
                    if (editor == null) {
                        WarehouseCache.delete(type);
                        WarehouseCache.register(to, entry);
                        sender.sendMessage(ChatColor.YELLOW + Language.SUCCESS_WAREHOUSE_RENAME.toString());
                    } else {
                        sender.sendMessage(ChatColor.RED + Language.WARN_ENTRY_OPEN.fromEntryAndPlayer(type, editor));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + status.message);
                }
            } else {
                sender.sendMessage(ChatColor.RED + Language.WARN_UNKNOWN_TYPE.toString());
            }
        } else {
            sender.sendMessage(ChatColor.RED + Language.INFO_FORMAT.fromFormat("/warehouse rename [" + Language.ARG_FROM + "] [" + Language.ARG_TO + "]"));
        }
    }

    private void processDelete(CommandSender sender, String[] args) {
        if (args.length > 1) {
            String type = args[1].toUpperCase();
            if (WarehouseCache.hasEntry(type)) {
                String editor = LiveSessions.getEntryEditor(type);
                if (editor == null) {
                    WarehouseCache.delete(type);
                    sender.sendMessage(ChatColor.YELLOW + Language.SUCCESS_WAREHOUSE_DELETE.toString());
                } else {
                    sender.sendMessage(ChatColor.RED + Language.WARN_ENTRY_OPEN.fromEntryAndPlayer(type, editor));
                }
            } else {
                sender.sendMessage(ChatColor.RED + Language.WARN_UNKNOWN_TYPE.toString());
            }
        } else {
            sender.sendMessage(ChatColor.RED + Language.INFO_FORMAT.fromFormat("/warehouse delete [" + Language.ARG_FROM + "]"));
        }
    }

    private void processList(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + Language.LABEL_WAREHOUSE_ENTRIES.toString() + ":");
        List<String> types = WarehouseCache.getEntryTypes();
        if (!types.isEmpty()) {
            for (String type : types) {
                sender.sendMessage(ChatColor.GRAY + "- " + type);
            }
        } else {
            sender.sendMessage(ChatColor.GRAY + "- ");
        }
    }

}
