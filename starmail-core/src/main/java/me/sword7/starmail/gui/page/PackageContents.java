package me.sword7.starmail.gui.page;

import me.sword7.starmail.gui.data.SealedData;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.gui.Icons;
import me.sword7.starmail.gui.MenuUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import static me.sword7.starmail.sys.Language.ICON_COLLECT;

public class PackageContents implements IPageContents {

    @Override
    public Inventory populate(Inventory menu, SessionData sessionData) {

        SealedData sealedData = (SealedData) sessionData;

        menu.setItem(4, sealedData.getPack().getBaseItemStack());
        menu.setItem(9, Icons.createIcon(Material.FISHING_ROD, ChatColor.GRAY + ICON_COLLECT.toString()));

        ItemStack[] contents = sealedData.getContents();

        int index = 10;
        for (int i = 0; i < 21; i++) {
            int mailIndex = i;
            ItemStack itemStack = (mailIndex < contents.length) ? contents[mailIndex] : Icons.BACKGROUND_ITEM;
            if (itemStack == null) itemStack = Icons.BACKGROUND_ITEM;
            menu.setItem(index, itemStack);
            index += ((index + 2) % 9 == 0 ? 3 : 1);
        }

        return menu;
    }

    @Override
    public void processClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType) {
        SealedData sealedData = (SealedData) sessionData;
        if (slot == 9) {
            sessionData.exit();
        } else if (slot % 9 != 0 && (slot + 1) % 9 != 0 && slot > 9 && slot < 35) {
            ItemStack[] contents = sealedData.getContents();
            int itemIndex = (7 * ((slot / 9) - 1)) + (slot % 9) - 1;
            if (itemIndex < contents.length) {
                ItemStack itemStack = contents[itemIndex];
                if(itemStack != null){
                    MenuUtil.playPickupSound(player);
                    Map<Integer, ItemStack> overflow = player.getInventory().addItem(itemStack);
                    if (overflow.size() == 0) {
                        sealedData.getContents()[itemIndex] = null;
                    } else {
                        sealedData.getContents()[itemIndex] = overflow.get(0);
                    }
                    populate(menu, sessionData);
                }
            }
        }
    }

}
