package me.sword7.starmail.gui.page;

import me.sword7.starmail.box.Box;
import me.sword7.starmail.box.BoxType;
import me.sword7.starmail.gui.*;
import me.sword7.starmail.gui.data.BoxData;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.post.Mail;
import me.sword7.starmail.post.PostCache;
import me.sword7.starmail.util.X.XSound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static me.sword7.starmail.sys.Language.*;

public class MailOverview implements IPageContents {


    @Override
    public Inventory populate(Inventory menu, SessionData sessionData) {

        BoxData boxData = (BoxData) sessionData;
        Box box = boxData.getBox();

        if (box == null) box = BoxType.DEFAULT.getBox();
        ItemStack headerIcon = box.getItemStack();
        ItemMeta meta = headerIcon.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + LABEL_MAILBOX.toString());
        headerIcon.setItemMeta(meta);
        menu.setItem(4, headerIcon);

        menu.setItem(7, Icons.createIcon(Material.NOTE_BLOCK, ChatColor.WHITE + LABEL_NOTIFICATIONS.toString()));
        menu.setItem(9, Icons.createIcon(Material.FISHING_ROD, ChatColor.WHITE + ICON_COLLECT.toString()));

        UUID userID = boxData.getUser().getID();
        List<Mail> mailList = PostCache.hasMail(userID) ? PostCache.getMail(userID) : Collections.emptyList();
        int offsetY = sessionData.getPageOffsetY();

        int index = 10;
        for (int i = 0; i < 21; i++) {
            int mailIndex = i + (7 * offsetY);
            ItemStack itemStack = (mailIndex < mailList.size()) ? Icons.createMail(mailList.get(mailIndex)) : Icons.BACKGROUND_ITEM;
            menu.setItem(index, itemStack);
            index += ((index + 2) % 9 == 0 ? 3 : 1);
        }


        menu.setItem(17, Icons.createIcon(Material.TRIPWIRE_HOOK, ICON_SCROLL_UP.toString()));
        menu.setItem(26, Icons.createIcon(Material.STONE_BUTTON, "+" + sessionData.getPageOffsetY()));
        menu.setItem(35, Icons.createIcon(Material.HOPPER, ICON_SCROLL_DOWN.toString()));


        menu.setItem(36, Icons.createIcon(Material.LAVA_BUCKET, ChatColor.RED + ICON_DESTROY.toString()));

        menu.setItem(40, Icons.CLOSE);

        return menu;
    }

    private Sound lavaSound = XSound.BLOCK_LAVA_EXTINGUISH.isSupported() ? XSound.BLOCK_LAVA_EXTINGUISH.parseSound() : XSound.BLOCK_LAVA_POP.parseSound();

    @Override
    public void processClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType) {
        BoxData boxData = (BoxData) sessionData;
        if (slot == 17) {
            sessionData.scrollUp(this, menu);
        } else if (slot == 7) {
            sessionData.transition(PageType.MAIL_NOTIFICATIONS);
        } else if (slot == 35) {
            sessionData.scrollDown(this, menu);
        } else if (slot == 36) {
            if (player.getItemOnCursor() != null && player.getItemOnCursor().getType() != Material.AIR) {
                player.setItemOnCursor(AIR);
                player.playSound(player.getLocation(), lavaSound, 0.7f, 1f);
            }
        } else if (slot == 40) {
            sessionData.exit();
        } else if (slot == 9) {
            MenuUtil.playClickSound(player);
            UUID playerID = boxData.getUser().getID();
            List<Mail> mailList = new ArrayList<>();
            mailList.addAll(PostCache.hasMail(playerID) ? PostCache.getMail(playerID) : Collections.emptyList());
            for (Mail mail : mailList) {
                Map<Integer, ItemStack> overflow = player.getInventory().addItem(mail.getItemStack());
                if (overflow.size() == 0) {
                    PostCache.removeMail(playerID, mail);
                } else {
                    ItemStack leftover = overflow.get(0);
                    mail.setItemStack(leftover);
                }
                populate(menu, sessionData);
            }
            mailList.clear();
        } else if (slot % 9 != 0 && (slot + 1) % 9 != 0 && slot > 9 && slot < 35) {
            UUID playerID = boxData.getUser().getID();
            List<Mail> mailList = new ArrayList<>();
            mailList.addAll(PostCache.hasMail(playerID) ? PostCache.getMail(playerID) : Collections.emptyList());
            int mailIndex = (7 * ((slot / 9) - 1)) + (slot % 9) - 1;
            if (mailIndex < mailList.size()) {
                MenuUtil.playPickupSound(player);
                Mail mail = mailList.get(mailIndex);
                Map<Integer, ItemStack> overflow = player.getInventory().addItem(mail.getItemStack());
                if (overflow.size() == 0) {
                    PostCache.removeMail(playerID, mail);
                    populate(menu, sessionData);
                } else {
                    ItemStack leftover = overflow.get(0);
                    mail.setItemStack(leftover);
                    menu.setItem(slot, Icons.createMail(mail));
                }
            }
        }
    }


    private ItemStack AIR = new ItemStack(Material.AIR);

}
