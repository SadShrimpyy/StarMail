package me.sword7.starmail.post;

import me.sword7.starmail.gui.LiveSessions;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.sys.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMail implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (Permissions.canAccess(player)) {
                if (args.length > 0) {
                    //Feature coming soon (edit other's mailbox)
                    /*
                    String userName = args[0];
                    User user = UserCache.getUser(userName);
                    if (user != null) {
                        if(!user.getID().equals(player.getUniqueId())){
                            LiveSessions.launchVirtualMail(player, user);
                        }else{
                            LiveSessions.launchVirtualMail(player);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + Language.WARN_PLAYER_NOT_FOUND.fromPlayer(userName));
                    }
                    */
                    LiveSessions.launchVirtualMail(player);
                } else {
                    LiveSessions.launchVirtualMail(player);
                }
            } else if (Permissions.canEBox(player)) {
                LiveSessions.launchVirtualMail(player);
            } else {
                player.sendMessage(ChatColor.RED + Language.WARN_NOT_PERMITTED.toString());
            }

        }

        return false;
    }
}
