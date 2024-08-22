package me.sword7.starmail.gui.data;

import me.sword7.starmail.gui.page.Page;
import me.sword7.starmail.pack.Pack;
import org.bukkit.entity.Player;

public class PackData extends SessionData {

    protected int packSlot;
    protected Pack pack;

    public PackData(Player player, Page start, int packSlot, Pack pack) {
        super(start, player);
        this.packSlot = packSlot;
        this.pack = pack;
    }

    public int getPackSlot() {
        return packSlot;
    }

    public Pack getPack() {
        return pack;
    }

    @Override
    public String getEffectiveTitle(){
        return titleBase + pack.getDisplayName();
    }

}
