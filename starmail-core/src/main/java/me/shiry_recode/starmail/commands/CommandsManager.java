package me.shiry_recode.starmail.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.checkerframework.com.github.javaparser.quality.NotNull;

import static me.sword7.starmail.StarMail.getPlugin;

public class CommandsManager implements CommandExecutor {

    private CommandSender sender;
    private Command command;
    private String label;
    private String[] args;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            return true;
        }
        String requestedSubCommand = args[0].toLowerCase();
        this.command = command; this.sender = sender; this.label = label; this.args = args;

        if (isCommandRegistered(requestedSubCommand)) {
//            TODO: Future implementation
//            this.sender.sendMessage(Messages.composeWithPrefix(Configurations.getMessages().getString("command.not-found")
//                    .replace(Placeholders.getPlayerName(), this.sender.getName())
//                    .replace(Placeholders.getCommand(), commandPlaceholderBuilder())));
            return true;
        }

        ICommandSyntax subCommand = getPlugin().getRegisteredCommand().get(requestedSubCommand).apply(this.args);
        String permission = subCommand != null ? subCommand.getPermission(this.args) : null;
        if (isPermissionsNull(permission)) {
//            TODO: Future implementation
//            this.sender.sendMessage(Messages.composeWithPrefix(Configurations.getMessages().getString("command.not-found")
//                    .replace(Placeholders.getPlayerName(), this.sender.getName())
//                    .replace(Placeholders.getCommand(), commandPlaceholderBuilder())));
            return true;
        }

        if (isSenderAllowed(permission)) {
//            TODO: Future implementation
//            this.sender.sendMessage(Messages.composeWithPrefix(Configurations.getMessages().getString("player.no-permission")
//                    .replace(Placeholders.getPlayerName(), this.sender.getName())
//                    .replace(Placeholders.getCommand(), commandPlaceholderBuilder())
//                    .replace(Placeholders.getPermission(), permission)));
            return true;
        }

        if (wereArgsExpected(subCommand)) {
//            TODO: Future implementation
//            this.sender.sendMessage(Messages.composeWithPrefix(Configurations.getMessages().getString("command.not-complete")
//                    .replace(Placeholders.getPlayerName(), this.sender.getName())
//                    .replace(Placeholders.getCommand(), commandPlaceholderBuilder())));
            return true;
        }

        subCommand.perform(this.sender);
        return true;
    }

    private String commandPlaceholderBuilder() {
        StringBuilder stringBuilder = new StringBuilder(20);
        stringBuilder.append("/").append(label).append(" ");
        for (String arg : this.args) if (arg != null) stringBuilder.append(arg).append(" ");
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    private boolean wereArgsExpected(ICommandSyntax subCommand) {
        return (this.args.length != subCommand.expectedArgs())
                && (this.args.length != subCommand.expectedArgs() - subCommand.possibleErrors());
    }

    private boolean isSenderAllowed(String permission) {
        return !this.sender.hasPermission(permission);
    }

    private boolean isCommandRegistered(String requestedSubCommand) {
        return !getPlugin().getRegisteredCommand().containsKey(requestedSubCommand);
    }

    private boolean isPermissionsNull(String permission) {
        return permission == null;
    }
}