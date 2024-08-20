package me.sword7.starmail.gui.data;

import me.sword7.starmail.gui.LiveSessions;
import me.sword7.starmail.gui.MenuUtil;
import me.sword7.starmail.gui.page.IPageContents;
import me.sword7.starmail.gui.page.Page;
import me.sword7.starmail.gui.page.PageType;
import me.sword7.starmail.sys.Version;
import me.sword7.starmail.util.Scheduler;
import me.sword7.starmail.util.X.XGlass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class SessionData {

    protected static String titleBase = ChatColor.DARK_GRAY.toString() + ChatColor.BOLD.toString();
    private static final XGlass DEFAULT = XGlass.BLACK;

    private boolean clicking = false;
    private UUID cID;
    private boolean updating = false;
    private UUID uID;
    protected Player player;
    private UUID ID = UUID.randomUUID();
    protected Page current;
    private boolean transitioning = false;
    private int pageOffsetY;
    private XGlass theme;

    public SessionData(Page start, Player player) {
        this.player = player;
        this.pageOffsetY = 0;
        this.current = start;
        this.theme = DEFAULT;
    }

    public Player getPlayer() {
        return player;
    }

    public String getEffectiveTitle() {
        return current.getTitle();
    }

    public void start() {
        player.openInventory(current.getInventory(this));
    }

    public XGlass getTheme() {
        return theme;
    }

    public void setTheme(XGlass theme) {
        this.theme = theme;
    }

    public Page getCurrent() {
        return current;
    }

    public void transition(PageType to) {
        if (isValid()) {
            MenuUtil.playClickSound(player);
            transitionSilent(to);
        }
    }

    public void transitionSilent(PageType to) {
        current = to.getPage();
        updateSafe();
    }

    public void updateTitle() {
        updateSafe();
    }

    private void updateSafe() {
        if (isValid() && !updating) {
            if (!clicking) {
                registerUpdate(1);
                update();
            } else {
                registerUpdate(4);
                Scheduler.runLater(() -> {
                    update();
                }, 3);
            }

        }
    }

    private void update() {
        if (isValid()) {
            InventoryView view = player.getOpenInventory();
            ItemStack cursor = view.getCursor();
            view.setCursor(null);
            pageOffsetY = 0;
            transitioning = true;
            player.openInventory(current.getInventory(this));
            transitioning = false;
            view.setCursor(cursor);
        }
    }

    public boolean isTransitioning() {
        return transitioning;
    }

    public int getPageOffsetY() {
        return pageOffsetY;
    }


    public void scrollUp(IPageContents contents, Inventory menu) {
        pageOffsetY--;
        if (pageOffsetY < 0) {
            pageOffsetY = 0;
        }
        MenuUtil.playClickSound(player);
        contents.populate(menu, this);
    }

    public void scrollDown(IPageContents contents, Inventory menu) {
        pageOffsetY++;
        MenuUtil.playClickSound(player);
        contents.populate(menu, this);
    }

    public void setPageOffsetY(int pageOffsetY) {
        this.pageOffsetY = pageOffsetY;
    }

    public void fixOffHandGlitch() {
        if (!Version.current.hasOneHand()) {
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = inventory.getItemInOffHand() != null ? inventory.getItemInOffHand() : new ItemStack(Material.AIR);
            transitioning = true;
            player.closeInventory();
            MenuUtil.playErrorSound(player);
            inventory.setItemInOffHand(itemStack);
            player.openInventory(current.getInventory(this));
            transitioning = false;
        }
    }

    public UUID getID() {
        return ID;
    }


    public boolean isValid() {
        return LiveSessions.hasSession(player) && LiveSessions.getData(player).getID().equals(ID);
    }

    public void exit() {
        //run later to prevent visual glitch
        Scheduler.runLater(() -> {
            if (isValid()) {
                player.closeInventory();
                LiveSessions.end(player);
                MenuUtil.playClickSound(player);
            }
        }, 1);
    }

    public boolean isUpdating() {
        return updating;
    }

    public void registerClick() {
        clicking = true;
        UUID cID = UUID.randomUUID();
        this.cID = cID;
        Scheduler.runLater(() -> {
            if (cID.equals(this.cID)) {
                this.cID = null;
                clicking = false;
            }
        }, 2);
    }

    public void registerUpdate(int ticks) {
        updating = true;
        UUID uID = UUID.randomUUID();
        this.uID = uID;
        Scheduler.runLater(() -> {
            if (uID.equals(this.uID)) {
                this.uID = null;
                updating = false;
            }
        }, ticks);
    }

    public void onEnd() {

    }

}
