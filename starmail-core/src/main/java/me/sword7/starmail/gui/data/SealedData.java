package me.sword7.starmail.gui.data;

import me.sword7.starmail.util.Particle;
import me.sword7.starmail.gui.Icons;
import me.sword7.starmail.gui.page.Page;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.util.MailUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SealedData extends PackData {

    private ItemStack[] contents;
    private UUID trackingNo;
    private boolean opened = false;
    private boolean expired;
    private Particle particle;

    public SealedData(Player player, Page home, UUID trackingNo, ItemStack[] contents, Pack pack, int slot, boolean expired) {
        super(player, home, slot, pack);
        setTheme(pack.getBorder());
        this.contents = contents;
        this.trackingNo = trackingNo;
        this.expired = expired;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public UUID getTrackingNo() {
        return trackingNo;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isExpired() {
        return expired;
    }

    @Override
    public void onEnd() {
        if (opened) {
            ItemStack itemStack = player.getInventory().getItem(packSlot);
            if (Pack.isPack(itemStack)) {
                ItemStack toSet = itemStack.clone();
                int amount = toSet.getAmount() - 1;
                if (amount < 1) {
                    toSet = Icons.AIR;
                } else {
                    toSet.setAmount(amount);
                }
                player.getInventory().setItem(packSlot, toSet);
            }
            pack.playCloseSound(player);
            player.playSound(player.getLocation(), Pack.getPoofSound(), 2f, 1.2f);

            MailUtil.giveItems(player, contents);
//             TODO: Error - EventException @ ~.SessionData.lambda$exit$1(SessionData.java:172) ~[?:?]
//            Particle.playCloud(player);
        }
    }

}
