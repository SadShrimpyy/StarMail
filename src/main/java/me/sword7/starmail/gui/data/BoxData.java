package me.sword7.starmail.gui.data;

import me.sword7.starmail.box.Box;
import me.sword7.starmail.box.BoxType;
import me.sword7.starmail.gui.page.PageType;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.user.User;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BoxData extends SessionData {

    private Box box;
    private User user;
    private Location location;
    private boolean virtual;

    public BoxData(Player player, Box box, User user, Location location) {
        super(PageType.MAIL_HOME.getPage(), player);
        setTheme(box.getGlass());
        this.box = box;
        this.user = user;
        this.location = location;
        this.virtual = false;
    }

    public BoxData(Player player, User user) {
        super(PageType.MAIL_HOME.getPage(), player);
        setTheme(BoxType.DEFAULT.getBox().getGlass());
        this.user = user;
        this.virtual = true;
    }

    public Box getBox() {
        return box;
    }

    public User getUser() {
        return user;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String getEffectiveTitle() {
        return titleBase + Language.LABEL_MAILBOX + ChatColor.DARK_GRAY + ChatColor.ITALIC.toString() + " - " + user.getName();
    }

    @Override
    public void onEnd() {
        if (!virtual) {
            player.playSound(location, Box.getCloseSound(), 1f, 1.2f);
        }
    }

}
