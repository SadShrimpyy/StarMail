package me.sword7.starmail.gui.page;

import me.sword7.starmail.gui.Icons;
import me.sword7.starmail.gui.LiveSessions;
import me.sword7.starmail.gui.MenuUtil;
import me.sword7.starmail.gui.data.PostData;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.gui.data.TipData;
import me.sword7.starmail.post.PostCache;
import me.sword7.starmail.postbox.HatBox;
import me.sword7.starmail.postbox.Postbox;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.user.User;
import me.sword7.starmail.user.UserCache;
import me.sword7.starmail.util.AnimationUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class PostboxSend extends PostboxMenu {

    @Override
    public void populatePostbox(Inventory menu, SessionData sessionData) {
        PostData postData = (PostData) sessionData;
        postData.changeAnimationMenu(menu);
        Postbox postbox = postData.getPostbox();
        ItemStack background = postbox.getXGlass().getSwiggle();
        for (int i = 1; i < 5; i++) {
            int rowStart = (i * 9);
            for (int j = 1; j < 8; j++) {
                menu.setItem(rowStart + j, background);
            }
        }

        if (postbox instanceof HatBox) {
            HatBox hatBox = (HatBox) postbox;
            ItemStack hatBase = hatBox.getHatBase().getDot();
            ItemStack hatHighlight = hatBox.getHatHighlight().getDot();
            menu.setItem(9, hatHighlight);
            menu.setItem(10, hatBase);
            menu.setItem(12, hatHighlight);
            menu.setItem(15, hatBase);
            menu.setItem(17, hatHighlight);
            menu.setItem(18, hatBase);
            menu.setItem(26, hatBase);

        }

        ItemStack[] mails = postData.getMails();
        int pageYOffset = 0;
        if (mails[0] == null && !postData.isAnimating(0) &&
                mails[1] == null && !postData.isAnimating(1)) pageYOffset = -1;
        if (mails[1] == null && !postData.isAnimating(1) &&
                mails[2] == null && !postData.isAnimating(2)) pageYOffset = 1;
        postData.setPageOffsetY(pageYOffset);


        ItemStack arrow = AnimationUtil.getArrow();
        ItemMeta meta = arrow.getItemMeta();
        meta.setLore(Collections.singletonList(ChatColor.DARK_GREEN + Language.ICON_SEND_LORE.toString()));
        arrow.setItemMeta(meta);

        for (int i = 0; i < mails.length; i++) {
            int row = 2 + i + postData.getPageOffsetY();
            if (postData.isAnimating(i)) {
                postData.getAnimation(i).doCurrent();
            } else {
                ItemStack mail = mails[i];
                if (mail != null) {
                    int rowStart = row * 9;
                    menu.setItem(rowStart + 1, mail);
                    menu.setItem(rowStart + 2, arrow);
                    menu.setItem(rowStart + 3, arrow);
                    menu.setItem(rowStart + 4, arrow);
                    menu.setItem(rowStart + 5, arrow);
                    menu.setItem(rowStart + 6, Icons.AIR);
                    menu.setItem(rowStart + 7, Icons.createMailbox(postData.getTo().getName()));
                }
            }
        }

    }

    @Override
    public void processPostboxClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType) {
        if (slot == 49) {
            LiveSessions.end(player);
            player.closeInventory();
            MenuUtil.playClickSound(player);
        } else {
            PostData postData = (PostData) sessionData;
            ItemStack[] mails = postData.getMails();
            for (int i = 0; i < mails.length; i++) {
                int row = 2 + i + postData.getPageOffsetY();
                if (!postData.isAnimating(i)) {
                    int rowStart = row * 9;
                    if (slot >= rowStart + 2 && slot < rowStart + 6) {
                        ItemStack itemStack = mails[i];
                        if (itemStack != null) {
                            if (!PostCache.isCooling(player)) {
                                User mailSender = UserCache.getCachedUser(player.getUniqueId());
                                if (mailSender != null) {
                                    int coolDuration = mailSender.getSendCooldown(player);
                                    PostCache.send(mailSender, postData.getTo().getID(), itemStack.clone(), coolDuration);
                                    mails[i] = null;
                                    PostboxSendAnimation animation = new PostboxSendAnimation(postData, menu, i, itemStack.clone());
                                    postData.registerAnimation(i, animation);
                                }
                            } else {
                                MenuUtil.playErrorSound(player);
                                postData.addTip(TipData.getWarnCooling(PostCache.getCooldownLeft(player)));
                            }
                        }
                        break;
                    }
                }
            }

        }
    }

}
