package me.sword7.starmail.gui.data;

import me.sword7.starmail.box.Box;
import me.sword7.starmail.gui.page.FBoxSendAnimation;
import me.sword7.starmail.gui.page.PageType;
import me.sword7.starmail.post.Mail;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.user.User;
import me.sword7.starmail.util.MailUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FBoxData extends TipData implements IUpdateable {

    private Box box;
    private User owner;
    private Location location;
    private ItemStack mail;
    private boolean validMail = false;
    private FBoxSendAnimation animation;

    public FBoxData(Player player, Box box, User owner, Location location) {
        super(PageType.F_MAILBOX.getPage(), player);
        setTheme(box.getGlass());
        this.box = box;
        this.owner = owner;
        this.location = location;
    }

    public void setMail(ItemStack itemStack) {
        mail = itemStack;
        checkValid();
    }


    public void checkValid() {
        validMail = mail != null && Mail.isMail(player, mail);
    }

    public boolean isValidMail() {
        return validMail;
    }

    public ItemStack getMail() {
        return mail;
    }

    public Box getBox() {
        return box;
    }

    public User getOwner() {
        return owner;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isAnimating() {
        return animation != null;
    }

    public FBoxSendAnimation getAnimation() {
        return animation;
    }

    public void unRegisterAnimation() {
        animation = null;
    }

    public void registerAnimation(FBoxSendAnimation animation) {
        this.animation = animation;
    }

    public void changeAnimationMenu(Inventory menu) {
        if (isAnimating()) {
            animation.setMenu(menu);
        }
    }

    @Override
    public String getEffectiveTitle() {
        if (!doTip) {
            return titleBase + Language.LABEL_MAILBOX + ChatColor.DARK_GRAY + ChatColor.ITALIC.toString() + " - " + owner.getName();
        } else {
            return titleBase + Language.LABEL_MAILBOX + " " + tip;
        }
    }


    @Override
    public void updateContents(Inventory menu) {
        setMail(menu.getItem(27));
    }

    @Override
    public void onEnd(){
        ItemStack[] stacks = new ItemStack[1];
        stacks[0] = mail;
        MailUtil.giveItems(player, stacks);
        player.playSound(location, Box.getCloseSound(), 1f, 1.2f);
    }


}
