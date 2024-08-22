package me.sword7.starmail.gui.page;

import me.sword7.starmail.StarMail;
import me.sword7.starmail.box.Box;
import me.sword7.starmail.gui.Icons;
import me.sword7.starmail.gui.data.PostData;
import me.sword7.starmail.gui.data.TipData;
import me.sword7.starmail.util.AnimationUtil;
import me.sword7.starmail.util.Scheduler;
import me.sword7.starmail.util.X.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PostboxSendAnimation extends BukkitRunnable {

    private static ItemStack arrow = AnimationUtil.getArrow();
    private static ItemStack arrowHighlight = AnimationUtil.getArrowHighlight();

    private final PostData postData;
    private final Player player;
    private final int slot;
    private final ItemStack mailStack;
    private Inventory menu;
    private int frame = 0;

    public PostboxSendAnimation(PostData postData, Inventory menu, int slot, ItemStack mailStack) {
        this.postData = postData;
        this.player = postData.getPlayer();
        this.menu = menu;
        this.slot = slot;
        this.mailStack = mailStack;
        runTaskTimer(StarMail.getPlugin(), 0, 2);
    }

    @Override
    public void run() {
        if (frame < 18 && postData.isValid() && postData.getCurrent().getType() == PageType.POSTBOX_SEND) {
            doFrame(frame);
            if (frame == 0) {
                postData.addTip(TipData.getSuccessSend());
                AnimationUtil.playWings(player);
            } else if (frame == 5) {
                player.playSound(player.getLocation(), Box.getOpenSound(), 0.7f, 1.2f);
            } else if (frame == 11) {
                player.playSound(player.getLocation(), Box.getCloseSound(), 0.7f, 1.2f);
            }
        } else {
            end();
        }
        frame++;
    }

    private void doFrame(int frame) {
        if (postData.isValid() && postData.getCurrent().getType() == PageType.POSTBOX_SEND) {
            if (frame >= 0 && frame < 4) {
                setBase();
                if (XMaterial.SPECTRAL_ARROW.isSupported()) {
                    setItem(2 + frame, arrowHighlight);
                } else {
                    setItem(2 + frame, mailStack);
                }
            } else if (frame >= 4 && frame < 8) {
                setBase();
                setItem(6, mailStack);
            } else if (frame >= 8 && frame < 13) {
                setBase();
                setItem(6, Icons.AIR);
            } else if (frame >= 13 && frame < 18) {
                ItemStack background = postData.getPostbox().getXGlass().getSwiggle();
                setItem(1, background);
                setItem(2, background);
                setItem(3, background);
                setItem(4, background);
                setItem(5, background);
                setItem(6, background);
                setItem(7, background);
            }
        } else {
            end();
        }
    }

    public void doCurrent() {
        doFrame(frame);
    }

    private void setBase() {
        setItem(1, Icons.AIR);
        setItem(2, arrow);
        setItem(3, arrow);
        setItem(4, arrow);
        setItem(5, arrow);
        setItem(6, Icons.AIR);
        setItem(7, Icons.createMailbox(postData.getTo().getName()));
    }

    public void setMenu(Inventory menu) {
        this.menu = menu;
    }

    private void setItem(int index, ItemStack itemStack) {
        menu.setItem((slot + 2 + postData.getPageOffsetY()) * 9 + index, itemStack);
    }

    private void end() {
        cancel();
        postData.removeAnimation(slot);
        if (postData.isValid()) postData.transitionSilent(postData.getCurrent().getType());
        Scheduler.runLater(() -> {
            if (postData.isValid()) {
                if (postData.noAnimationsOrMail() && postData.getCurrent().getType() == PageType.POSTBOX_SEND) {
                    postData.reset();
                    postData.transitionSilent(PageType.POSTBOX_MAIL);
                }
            }
        }, 2 * 9);
    }
}
