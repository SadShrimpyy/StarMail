package me.sword7.starmail.gui.page;

import me.sword7.starmail.StarMail;
import me.sword7.starmail.gui.data.FBoxData;
import me.sword7.starmail.gui.data.TipData;
import me.sword7.starmail.util.AnimationUtil;
import me.sword7.starmail.util.X.XGlass;
import me.sword7.starmail.util.X.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FBoxSendAnimation extends BukkitRunnable {

    private static ItemStack arrow = AnimationUtil.getArrow();
    private static ItemStack arrowHighlight = AnimationUtil.getArrowHighlight();
    private static ItemStack voidStack = XGlass.BLACK.getDot();

    private final FBoxData fBoxData;
    private final Player player;
    private final ItemStack mailStack;
    private Inventory menu;
    private int frame = 0;

    public FBoxSendAnimation(FBoxData fBoxData, Inventory menu, ItemStack mailStack) {
        this.fBoxData = fBoxData;
        this.player = fBoxData.getPlayer();
        this.menu = menu;
        this.mailStack = mailStack;
        runTaskTimer(StarMail.getPlugin(), 0, 2);
    }

    @Override
    public void run() {
        if (frame < 11 && fBoxData.isValid()) {
            doFrame(frame);
            if (frame == 0) {
                fBoxData.addTip(TipData.getSuccessSend());
                AnimationUtil.playWings(player);
            }
        } else {
            end();
        }
        frame++;
    }

    private void doFrame(int frame) {
        if (fBoxData.isValid()) {
            if (frame >= 0 && frame < 3) {
                setBase();
                if (XMaterial.SPECTRAL_ARROW.isSupported()) {
                    setItem(1 + frame, arrowHighlight);
                } else {
                    setItem(1 + frame, mailStack);
                }
            } else if (frame >= 3 && frame < 7) {
                setBase();
                setItem(4, mailStack);
            } else if (frame >= 9 && frame < 11) {
                setBase();
                setItem(4, voidStack);
            }
        } else {
            end();
        }

    }

    public void doCurrent() {
        doFrame(frame);
    }

    private void setBase() {
        setItem(1, arrow);
        setItem(2, arrow);
        setItem(3, arrow);
        setItem(4, voidStack);
    }

    public void setMenu(Inventory menu) {
        this.menu = menu;
    }

    private void setItem(int index, ItemStack itemStack) {
        menu.setItem(27 + index, itemStack);
    }


    private void end() {
        cancel();
        fBoxData.unRegisterAnimation();
    }


}
