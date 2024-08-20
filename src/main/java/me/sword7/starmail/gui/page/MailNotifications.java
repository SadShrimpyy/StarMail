package me.sword7.starmail.gui.page;

import me.sword7.starmail.gui.Icons;
import me.sword7.starmail.gui.MenuUtil;
import me.sword7.starmail.gui.data.BoxData;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.post.notifications.Notifications;
import me.sword7.starmail.user.User;
import me.sword7.starmail.user.UserCache;
import me.sword7.starmail.util.X.XDisc;
import me.sword7.starmail.util.X.XGlass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static me.sword7.starmail.sys.Language.*;

public class MailNotifications implements IPageContents {

    public static Material onMaterial = XDisc.MUSIC_DISC_STAL.getMaterial();
    public static Material offMaterial = XDisc.MUSIC_DISC_11.getMaterial();

    @Override
    public Inventory populate(Inventory menu, SessionData sessionData) {

        BoxData boxData = (BoxData) sessionData;
        menu.setItem(4, Icons.createIcon(Material.NOTE_BLOCK, ChatColor.GRAY + LABEL_NOTIFICATIONS.toString()));

        for (int i = 9; i < 45; i++) {
            menu.setItem(i, Icons.BACKGROUND_ITEM);
        }

        Notifications notifications = boxData.getUser().getNotifications();

        boolean enabled = notifications.isOnJoin();
        ItemStack onJoinStack = Icons.createDisc(enabled ? onMaterial : offMaterial, ChatColor.GRAY + ICON_ON_JOIN.toString());
        menu.setItem(20, onJoinStack);
        ItemStack enabledStack = enabled ? Icons.createMenuGlass(XGlass.LIME, ChatColor.GREEN, LABEL_ENABLED.toString()) :
                Icons.createMenuGlass(XGlass.RED, ChatColor.RED, LABEL_DISABLED.toString());
        menu.setItem(21, enabledStack);
        menu.setItem(22, enabledStack);
        menu.setItem(23, enabledStack);
        menu.setItem(24, enabledStack);


        enabled = notifications.isOnReceive();
        ItemStack onReceiveStack = Icons.createDisc(enabled ? onMaterial : offMaterial, ChatColor.GRAY + ICON_ON_RECEIVE.toString());
        menu.setItem(29, onReceiveStack);
        enabledStack = enabled ? Icons.createMenuGlass(XGlass.LIME, ChatColor.GREEN, LABEL_ENABLED.toString()) :
                Icons.createMenuGlass(XGlass.RED, ChatColor.RED, LABEL_DISABLED.toString());
        menu.setItem(30, enabledStack);
        menu.setItem(31, enabledStack);
        menu.setItem(32, enabledStack);
        menu.setItem(33, enabledStack);

        return menu;
    }

    @Override
    public void processClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType) {
        BoxData boxData = (BoxData) sessionData;
        User user = boxData.getUser();
        Notifications notifications = user.getNotifications();
        if (slot >= 20 && slot <= 24) {
            notifications.setOnJoin(!notifications.isOnJoin());
            UserCache.touch(user);
            populate(menu, sessionData);
            MenuUtil.playClickSound(player);
        } else if (slot >= 29 && slot <= 33) {
            notifications.setOnReceive(!notifications.isOnReceive());
            UserCache.touch(user);
            populate(menu, sessionData);
            MenuUtil.playClickSound(player);
        }
    }
}
