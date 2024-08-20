package me.sword7.starmail.gui.data;

import me.sword7.starmail.gui.page.Page;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.Scheduler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TipData extends SessionData {

    private static String SUCCESS_SEND = ChatColor.GREEN.toString() + ChatColor.BOLD + Language.SUCCESS_SENT_SHORT;

    public static String getSuccessSend() {
        return SUCCESS_SEND;
    }

    public static String getWarnCooling(int duration) {
        return ChatColor.RED.toString() + ChatColor.BOLD + Language.WARN_COOLING_SHORT.fromSeconds(duration);
    }

    public TipData(Page start, Player player) {
        super(start, player);
    }

    protected String tip;
    protected boolean doTip = false;
    private UUID tipPid;

    public void addTip(String tip) {
        UUID pid = UUID.randomUUID();
        doTip = true;
        tipPid = pid;
        if (!tip.equals(this.tip)) {
            this.tip = tip;
            updateTitle();
        }
        Scheduler.runLater(() -> {
            if (isValid()) removeTip(pid);
        }, 30);
    }


    public void removeTip(UUID pid) {
        if (pid.equals(tipPid)) {
            tip = "";
            tipPid = null;
            doTip = false;
            updateTitle();
        }
    }


}
