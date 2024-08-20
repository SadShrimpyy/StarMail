package me.sword7.starmail.gui.page;

import me.sword7.starmail.gui.*;
import me.sword7.starmail.gui.data.PostData;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.letter.LetterType;
import me.sword7.starmail.postbox.Postbox;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.Head;
import me.sword7.starmail.util.X.XGlass;
import me.sword7.starmail.util.X.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public abstract class PostboxMenu implements IPageContents {

    public abstract void populatePostbox(Inventory menu, SessionData sessionData);

    @Override
    public Inventory populate(Inventory menu, SessionData sessionData) {
        PostData postData = (PostData) sessionData;
        postData.checkValid();
        Postbox postbox = postData.getPostbox();
        menu.setItem(0, postbox.getItemStack());
        ItemStack book = new ItemStack(XMaterial.WRITABLE_BOOK.parseMaterial());
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setDisplayName(ChatColor.YELLOW + Language.ICON_INSERT_MAIL.toString());
        menu.setItem(1, LetterType.DEFAULT.getLetter().getLetter(bookMeta));
        ItemStack arrowBroken = Icons.createIcon(Material.BARRIER, ChatColor.RED + "=/=>");
        ItemStack arrowOne = postData.isValidMails() ? XGlass.LIME.getCustom(ChatColor.GREEN, "==>") : arrowBroken;
        ItemStack arrowTwo = postData.isValidMails() && postData.hasTo() ? XGlass.LIME.getCustom(ChatColor.GREEN, "==>") : arrowBroken;
        menu.setItem(2, arrowOne);
        menu.setItem(3, arrowOne);
        menu.setItem(4, Icons.createIcon(Material.NAME_TAG, ChatColor.YELLOW + Language.ICON_TO.toString()));
        menu.setItem(5, arrowTwo);
        menu.setItem(6, arrowTwo);
        menu.setItem(7, Icons.createIcon(Material.ARROW, ChatColor.YELLOW + Language.ICON_SEND_MAIL.toString()));
        ItemStack head = postData.hasTo() ? Head.getPlayerHead(postData.getTo()) : Head.getSteeveHead(ChatColor.RED.toString() + Language.CONST_UNKNOWN);
        menu.setItem(8, head);

        menu.setItem(49, Icons.CLOSE);

        populatePostbox(menu, sessionData);

        return menu;
    }

    public abstract void processPostboxClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType);


    @Override
    public void processClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType) {
        PostData postData = (PostData) sessionData;
        PageType currentPage = sessionData.getCurrent().getType();
        if (slot == 49) {
            sessionData.exit();
        } else if (slot >= 0 && slot <= 1) {
            if (currentPage != PageType.POSTBOX_MAIL) {
                sessionData.transition(PageType.POSTBOX_MAIL);
            }
        } else if (slot >= 2 && slot <= 4) {
            if (currentPage != PageType.POSTBOX_PLAYER) {
                if (postData.isValidMails()) {
                    sessionData.transition(PageType.POSTBOX_PLAYER);
                } else {
                    MenuUtil.playErrorSound(player);
                }
            }
        } else if (slot >= 5 && slot <= 8) {
            postData.acceptSuggestion();
            if (currentPage != PageType.POSTBOX_SEND) {
                if (postData.isValidMails() && postData.hasTo()) {
                    sessionData.transition(PageType.POSTBOX_SEND);
                } else {
                    MenuUtil.playErrorSound(player);
                }
            }
        } else {
            processPostboxClick(player, menu, sessionData, slot, clickType);
        }
    }


}
