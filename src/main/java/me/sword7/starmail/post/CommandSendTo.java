package me.sword7.starmail.post;

import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.sys.Permissions;
import me.sword7.starmail.user.User;
import me.sword7.starmail.user.UserCache;
import me.sword7.starmail.util.X.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import static me.sword7.starmail.sys.Language.*;

public class CommandSendTo implements CommandExecutor {

    private static final ItemStack AIR = new ItemStack(Material.AIR);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (Permissions.canESend(sender)) {
                if (args.length > 0) {
                    String targetName = getName(args[0]).toUpperCase();
                    UserCache.getUser(targetName, (User target) -> {
                        if (target != null && player.isOnline()) {
                            PlayerInventory inventory = player.getInventory();
                            int handSlot = inventory.getHeldItemSlot();
                            ItemStack itemStack = player.getInventory().getItem(handSlot);
                            if (!isEmpty(itemStack) && (Permissions.canSendCustom(sender) || isMail(itemStack))) {
                                if (!PostCache.isCooling(player)) {
                                    player.getInventory().setItem(handSlot, AIR);
                                    User mailSender = UserCache.getCachedUser(player.getUniqueId());
                                    if (mailSender != null) {
                                        int coolDuration = mailSender.getSendCooldown(player);
                                        PostCache.send(mailSender, target.getID(), itemStack.clone(), coolDuration);
                                        player.sendMessage(ChatColor.YELLOW + SUCCESS_SENT.fromPlayer(target.getName()));
                                        player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1f, 0.7f);
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + WARN_COOLING.fromSeconds(PostCache.getCooldownLeft(player)));
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + WARN_NOT_MAIL.toString());
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + WARN_PLAYER_NOT_FOUND.fromPlayer(targetName));
                        }
                    });
                } else {
                    player.sendMessage(ChatColor.RED + INFO_FORMAT.fromFormat("/sendto [" + ARG_PLAYER + "]"));
                }
            } else {
                player.sendMessage(ChatColor.RED + WARN_NOT_PERMITTED.toString());
            }

        }

        return false;
    }

    private boolean isEmpty(ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR;
    }

    private String getName(String s) {
        Player player = Bukkit.getPlayer(s);
        if (player != null) {
            return player.getName();
        } else {
            return s;
        }
    }

    private Material writtenBook = XMaterial.WRITTEN_BOOK.parseMaterial();

    private boolean isMail(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        return stack.getType() == writtenBook || Pack.isSealedPack(meta);
    }

}
