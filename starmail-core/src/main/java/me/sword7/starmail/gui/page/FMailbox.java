package me.sword7.starmail.gui.page;

import me.sword7.starmail.box.Box;
import me.sword7.starmail.gui.Icons;
import me.sword7.starmail.gui.MenuUtil;
import me.sword7.starmail.gui.data.FBoxData;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.gui.data.TipData;
import me.sword7.starmail.post.PostCache;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.user.User;
import me.sword7.starmail.user.UserCache;
import me.sword7.starmail.util.AnimationUtil;
import me.sword7.starmail.util.Head;
import me.sword7.starmail.util.Scheduler;
import me.sword7.starmail.util.X.XGlass;
import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class FMailbox implements IInsertable {

    @Override
    public Inventory populate(Inventory menu, SessionData sessionData) {

        FBoxData fBoxData = (FBoxData) sessionData;
        fBoxData.checkValid();
        fBoxData.changeAnimationMenu(menu);
        Box box = fBoxData.getBox();

        ItemStack base = box.getGlass().getDot();
        ItemStack highlight = box.getHighlight().getDot();
        ItemStack flag = box.getFlag().getDot();
        ItemStack voidStack = XGlass.BLACK.getDot();

        menu.setItem(8, Head.getPlayerHead(fBoxData.getOwner()));

        for (int i : baseSlots) {
            menu.setItem(i, base);
        }

        for (int i : voidSlots) {
            menu.setItem(i, voidStack);
        }

        for (int i : highlightSlots) {
            menu.setItem(i, highlight);
        }

        for (int i : flagSlots) {
            menu.setItem(i, flag);
        }

        ItemStack mailStack = fBoxData.getMail() != null ? fBoxData.getMail() : Icons.AIR;
        menu.setItem(27, mailStack);

        if (fBoxData.isAnimating()) {
            fBoxData.getAnimation().doCurrent();
        } else {
            ItemStack arrow = AnimationUtil.getArrow();
            ItemMeta meta = arrow.getItemMeta();
            meta.setLore(Collections.singletonList(ChatColor.DARK_GREEN + Language.ICON_SEND_LORE.toString()));
            arrow.setItemMeta(meta);
            menu.setItem(28, arrow);
            menu.setItem(29, arrow);
            menu.setItem(30, arrow);
        }

        menu.setItem(49, Icons.CLOSE);

        return menu;
    }


    @Override
    public void processClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType) {
        FBoxData fBoxData = (FBoxData) sessionData;
        //run later because slot items not updated yet
        Scheduler.runLater(() -> {
            if (sessionData.isValid()) {
                fBoxData.setMail(menu.getItem(27));
            }
        }, 1);
        if (slot == 49) {
            sessionData.exit();
        } else if (slot >= 28 && slot <= 30) {
            if (!fBoxData.isAnimating()) {
                if (fBoxData.isValidMail()) {
                    if (!PostCache.isCooling(player)) {
                        User mailSender = UserCache.getCachedUser(player.getUniqueId());
                        if (mailSender != null) {
                            int coolDuration = mailSender.getSendCooldown(player);
                            ItemStack itemStack = fBoxData.getMail();
                            PostCache.send(mailSender, fBoxData.getOwner().getID(), itemStack.clone(), coolDuration);
                            fBoxData.setMail(null);
                            menu.setItem(27, Icons.AIR);
                            FBoxSendAnimation animation = new FBoxSendAnimation(fBoxData, menu, itemStack.clone());
                            fBoxData.registerAnimation(animation);
                        }
                    } else {
                        MenuUtil.playErrorSound(player);
                        fBoxData.addTip(TipData.getWarnCooling(PostCache.getCooldownLeft(player)));
                    }
                } else {
                    MenuUtil.playErrorSound(player);
                    fBoxData.addTip(ChatColor.RED.toString() + ChatColor.BOLD + Language.WARN_INVALID_MAIL);
                }
            }

        }

    }


    private ImmutableList<Integer> baseSlots = new ImmutableList.Builder<Integer>()
            .add(0).add(1).add(7).add(9).add(18).add(35).add(36).add(44).add(45).add(53).build();

    private ImmutableList<Integer> voidSlots = new ImmutableList.Builder<Integer>()
            .add(12).add(13).add(14).add(20).add(21).add(22).add(23).add(24).add(31)
            .add(32).add(33).add(38).add(39).add(40).add(41).add(42).add(47).add(48).add(49).add(50)
            .add(51).build();

    private ImmutableList<Integer> highlightSlots = new ImmutableList.Builder<Integer>()
            .add(2).add(3).add(4).add(5).add(6).add(10).add(11).add(15).add(16)
            .add(19).add(25).add(34).add(37).add(43).add(46).add(52).build();


    private ImmutableList<Integer> flagSlots = new ImmutableList.Builder<Integer>()
            .add(17).add(26).build();


    @Override
    public boolean isInsertable(int slot) {
        return slot == 27;
    }

    private List<Integer> insertable = Collections.singletonList(27);

    @Override
    public List<Integer> getOrderedSlots() {
        return insertable;
    }

}
