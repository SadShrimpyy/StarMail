package me.shiry_recode.starmail.commands;

import me.sword7.starmail.util.ILootType;
import org.bukkit.command.CommandSender;

public interface ICommandSyntax {
    abstract String getName();
    abstract String getPermission(String[] args);
    abstract boolean hasSubcommands();
    abstract int expectedArgs();
    abstract int possibleErrors();
    abstract void perform(CommandSender sender);
    abstract void applyLoot(ILootType lootType);
}
