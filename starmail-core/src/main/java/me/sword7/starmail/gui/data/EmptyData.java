package me.sword7.starmail.gui.data;

import me.sword7.starmail.gui.page.PageType;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.pack.UpdateResult;
import me.sword7.starmail.util.MailUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EmptyData extends PackData implements IUpdateable {

    private boolean isSealing = false;
    private ItemStack packItem;
    private ItemStack[] itemStacks = new ItemStack[21];
    private Pack.ContentStatus contentStatus = Pack.ContentStatus.EMPTY;


    public EmptyData(Player player, Pack pack, ItemStack packItem, int slot) {
        super(player, PageType.EMPTY_PACKAGE.getPage(), slot, pack);
        setTheme(pack.getBorder());
        this.packItem = packItem;
    }

    public void setSealing(boolean sealing) {
        isSealing = sealing;
    }

    public ItemStack getPackItem() {
        return packItem;
    }

    public ItemStack[] getItemStacks() {
        return itemStacks;
    }

    public void setItemStacks(ItemStack[] itemStacks) {
        this.itemStacks = itemStacks;
    }

    public Pack.ContentStatus getContentStatus() {
        return contentStatus;
    }

    @Override
    public void updateContents(Inventory menu) {
        UpdateResult result = UpdateResult.getResult(menu);
        setItemStacks(result.getContents());
        contentStatus = result.getStatus();
    }

    @Override
    public void onEnd() {
        pack.playCloseSound(player);
        if (!isSealing) MailUtil.giveItems(player, itemStacks);
    }


}
