package me.sword7.starmail.gui.page;

import me.sword7.starmail.gui.Icons;
import me.sword7.starmail.gui.LiveSessions;
import me.sword7.starmail.gui.MenuUtil;
import me.sword7.starmail.gui.data.EmptyData;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.pack.Crate;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.pack.tracking.TrackingCache;
import me.sword7.starmail.util.X.XGlass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static me.sword7.starmail.sys.Language.*;

public class EmptyPackage implements IInsertable {

    private static final ItemStack BLACK = XGlass.BLACK.getCustom(ChatColor.DARK_GRAY, "~");

    public EmptyPackage() {
        buildInsertables();
    }

    @Override
    public Inventory populate(Inventory menu, SessionData sessionData) {

        EmptyData emptyData = (EmptyData) sessionData;

        menu.setItem(4, emptyData.getPack().getBaseItemStack());


        ItemStack[] contents = emptyData.getItemStacks();

        int index = 10;
        for (int i = 0; i < 21; i++) {
            int mailIndex = i;
            ItemStack itemStack = (mailIndex < contents.length) ? contents[mailIndex] : Icons.AIR;
            if (itemStack == null) itemStack = Icons.AIR;
            menu.setItem(index, itemStack);
            index += ((index + 2) % 9 == 0 ? 3 : 1);
        }


        menu.setItem(38, BLACK);
        menu.setItem(39, Icons.CLOSE);
        menu.setItem(40, BLACK);


        ItemStack sealStack = emptyData.getPack().getSealBaseStack();
        ItemMeta meta = sealStack.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + ICON_SEAL.toString());
        sealStack.setItemMeta(meta);

        menu.setItem(41, sealStack);
        menu.setItem(42, BLACK);

        return menu;

    }

    @Override
    public void processClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType) {

        EmptyData emptyData = (EmptyData) sessionData;

        emptyData.updateContents(menu);
        ItemStack[] contents = emptyData.getItemStacks();

        if (slot == 39) {
            sessionData.exit();
        } else if (slot == 41) {
            Pack.ContentStatus status = emptyData.getContentStatus();
            if (status == Pack.ContentStatus.VALID) {
                Pack pack = emptyData.getPack();
                emptyData.setSealing(true);
                pack.playSealSound(player);
                UUID trackingNo = TrackingCache.track(contents);
                ItemStack sealedItem = emptyData.getPackItem().clone();
                sealedItem.setItemMeta(Pack.seal(sealedItem.getItemMeta(), trackingNo, contents));
                sealedItem.setAmount(1);
                if (pack instanceof Crate) {
                    ((Crate) pack).addStraps(sealedItem);
                }

                int handSlot = emptyData.getPackSlot();

                ItemStack handItem = player.getInventory().getItem(handSlot);
                if (handItem != null && handItem.getType() != Material.AIR) {
                    handItem.setAmount(handItem.getAmount() - 1);
                    player.getInventory().setItem(handSlot, handItem);
                }

                ItemStack newHandSlot = player.getInventory().getItem(handSlot);
                if (newHandSlot == null || newHandSlot.getType() == Material.AIR) {
                    player.getInventory().setItem(handSlot, sealedItem);
                } else {
                    Map<Integer, ItemStack> overFlow = player.getInventory().addItem(sealedItem);
                    if (overFlow.size() > 0) {
                        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), overFlow.get(0));
                    }
                }

                LiveSessions.end(player);
                player.closeInventory();
                MenuUtil.playClickSound(player);
            } else {
                LiveSessions.end(player);
                player.closeInventory();
                MenuUtil.playClickSound(player);
                String message = status == Pack.ContentStatus.EMPTY ? WARN_EMPTY_SEAL.toString() : WARN_FRACTAL_SEAL.toString();
                player.sendMessage(ChatColor.RED + message);
            }
        }
    }

    private List<Integer> insertableList = new ArrayList<>();
    private Set<Integer> insertableSet = new HashSet<>();

    private void buildInsertables() {
        for (int i = 10; i < 35; i++) {
            if (i % 9 != 0 && (i + 1) % 9 != 0) {
                insertableList.add(i);
                insertableSet.add(i);
            }
        }
    }

    @Override
    public boolean isInsertable(int slot) {
        return insertableSet.contains(slot);
    }

    @Override
    public List<Integer> getOrderedSlots() {
        return insertableList;

    }


}
