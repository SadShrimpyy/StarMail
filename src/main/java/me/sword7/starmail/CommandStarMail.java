package me.sword7.starmail;

import me.sword7.starmail.sys.PluginHelp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStarMail implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PluginHelp.sendHelp(sender);
        return false;
    }
}
