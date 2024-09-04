package me.shiry_recode.starmail.commands;

import me.sword7.starmail.util.ILootType;
import org.bukkit.command.CommandSender;

import static me.shiry_recode.starmail.sadlibrary.SadLibrary.Generics;
import static me.shiry_recode.starmail.sadlibrary.SadLibrary.Messages;

public class CreditsCommand implements ICommandSyntax {
    private final String message = new StringBuilder(555)
            .append("\n&7&m&l                   &7&l< &c[&eStarMail &oReloaded&7] &7&l>&m                   &r")
            .append("\n   &6Retained & Recoded &7with &c&l<3&7 by: &9&osadshrimpy &7on &9Discord&7.")
            .append("\n   &eVersion&7: &a").append(Generics.getVersion()).append("&r")
            .append("\n   &cThis is the maintained version of the original discontinued StarMail.").append("&r")
            .append("\n   &r")
            .append("\n   &7Please write &2/starmail help &7for a list of commands &r")
            .append("\n&7&m&l                   &7&l< &c[&eStarMail &oReloaded&7] &7&l>&m                   ")
            .toString();

    public CreditsCommand(String[] strings) {
    }

    @Override
    public String getName() {
        return "credits";
    }

    @Override
    public String getPermission(String[] args) {
        return "";
    }

    @Override
    public boolean hasSubcommands() {
        return false;
    }

    @Override
    public int expectedArgs() {
        return 1;
    }

    @Override
    public int possibleErrors() {
        return 0;
    }

    @Override
    public void perform(CommandSender sender) {
        sender.sendMessage(Messages.compose(message));
    }

    @Override
    public void applyLoot(ILootType lootType) {

    }
}
