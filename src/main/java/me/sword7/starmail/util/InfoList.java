package me.sword7.starmail.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

import static me.sword7.starmail.sys.Language.LABEL_PAGE;

public class InfoList {

    private String title;
    private List<String> items;
    private int pageItemCount;

    public InfoList(String title, List<String> items, int pageItemCount) {
        this.title = title;
        this.items = items;
        this.pageItemCount = pageItemCount;
    }

    public void displayTo(CommandSender sender, int page) {
        if (page < 1) {
            page = 1;
        }
        sender.sendMessage(title);
        sender.sendMessage(ChatColor.DARK_GRAY + "> " + LABEL_PAGE + " " + page);

        int startIndex = (page - 1) * pageItemCount;
        for (int i = startIndex; i < startIndex + pageItemCount; i++) {
            int num = i + 1;
            String prefix = num + ". ";
            if (i < items.size()) {
                sender.sendMessage(prefix + items.get(i));
            } else {
                sender.sendMessage(prefix + "-");
            }
        }
    }

}
