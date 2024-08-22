package me.sword7.starmail.post.notifications;

import me.sword7.starmail.util.BulkTask;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static me.sword7.starmail.sys.Language.INFO_RECEIVE;
import static me.sword7.starmail.sys.Language.INFO_RECEIVE_MULTI;

public class Alert extends BulkTask {

    private Player player;
    private Notifications notifications;
    private String firstSender;
    private int items = 1;

    public Alert(Player player, String firstSender, Notifications notifications) {
        super(3, 9, State.COMBINING);
        this.player = player;
        this.notifications = notifications;
        this.firstSender = firstSender;
        Alerts.registerAlert(player, this);
    }

    @Override
    protected void runTask() {
        Alerts.unRegisterAlert(player);
        if (player.isOnline() && notifications.isOnReceive()) {
            if (items == 1) {
                player.sendMessage(ChatColor.YELLOW + INFO_RECEIVE.fromPlayer(firstSender));
            } else {
                player.sendMessage(ChatColor.YELLOW + INFO_RECEIVE_MULTI.fromAmount(items));
            }
        }
        cancel();
    }

    @Override
    public void onExtend() {
        super.onExtend();
        items++;
    }

}
