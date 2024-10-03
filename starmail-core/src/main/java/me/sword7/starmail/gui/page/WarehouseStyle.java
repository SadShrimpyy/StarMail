package me.sword7.starmail.gui.page;

import me.sword7.starmail.compatibility.versions.AssignTexture;
import me.sword7.starmail.gui.*;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.gui.data.WarehouseData;
import me.sword7.starmail.letter.Letter;
import me.sword7.starmail.pack.Crate;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.X.XDye;
import me.sword7.starmail.util.X.XMaterial;
import me.sword7.starmail.warehouse.WarehouseCache;
import me.sword7.starmail.warehouse.WarehouseEntry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static me.sword7.starmail.sys.Language.ICON_SCROLL_DOWN;
import static me.sword7.starmail.sys.Language.ICON_SCROLL_UP;

public class WarehouseStyle implements IPageContents {

    @Override
    public Inventory populate(Inventory menu, SessionData sessionData) {
        ItemStack styleStack = XDye.YELLOW.parseItemStack();
        ItemMeta meta = styleStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.WHITE.toString() + Language.LABEL_STYLE);
        styleStack.setItemMeta(meta);
        menu.setItem(4, styleStack);
        WarehouseData warehouseData = (WarehouseData) sessionData;
        WarehouseEntry entry = warehouseData.getEntry();
        menu.setItem(8, Icons.createMail(entry.getMail()));


        List<ItemStack> types = new ArrayList<>();
        if (entry.getType() == WarehouseEntry.Type.LETTER) {
            ItemStack itemStack = new ItemStack(Objects.requireNonNull(XMaterial.WRITTEN_BOOK.parseMaterial()));
            BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
            assert bookMeta != null;
            bookMeta.setAuthor(null);
            for (Letter letter : Letter.getAllLetters()) {
                bookMeta.setDisplayName(ChatColor.WHITE + letter.getName());
                ItemStack letterStack = letter.getLetter(bookMeta);
                types.add(letterStack);
            }
        } else {
            for (Pack pack : Pack.getAllPacks()) {
                ItemStack itemStack = pack.getBaseItemStack();
                if (pack instanceof Crate) {
                    Crate crate = (Crate) pack;
                    if (crate.isDoStraps()) {
                        itemStack = crate.getStrapStack();
                    }
                }
                types.add(itemStack);
            }
        }

        int index = 0;
        int offY = sessionData.getPageOffsetY();
        for (int row = 1; row < 4; row++) {
            for (int i = 1; i < 8; i++) {
                int effectiveIndex = index + (offY * 7);
                ItemStack stack = effectiveIndex < types.size() ? types.get(effectiveIndex) : Icons.BACKGROUND_ITEM;
                int slot = (row * 9) + i;
                menu.setItem(slot, stack);
                index++;
            }
        }

        menu.setItem(17, Icons.createIcon(Material.TRIPWIRE_HOOK, ICON_SCROLL_UP.toString()));
        menu.setItem(26, Icons.createIcon(Material.STONE_BUTTON, "+" + sessionData.getPageOffsetY()));
        menu.setItem(35, Icons.createIcon(Material.HOPPER, ICON_SCROLL_DOWN.toString()));

        menu.setItem(40, Icons.CLOSE);


        return menu;
    }

    @Override
    public void processClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType) {
        if (slot == 40) {
            sessionData.exit();
        } else if (slot == 17) {
            sessionData.scrollUp(this, menu);
        } else if (slot == 35) {
            sessionData.scrollDown(this, menu);
        } else if (slot > 9 && slot < 35) {
            WarehouseData warehouseData = (WarehouseData) sessionData;
            WarehouseEntry entry = warehouseData.getEntry();
            ItemStack entryStack = entry.getItemStack();
            ItemStack clickedStack = menu.getItem(slot);
            if (entry.getType() == WarehouseEntry.Type.LETTER) {
                if (Letter.isLetter(clickedStack)) {
                    MenuUtil.playClickSound(player);
                    assert clickedStack != null;
                    int modelData = Objects.requireNonNull(clickedStack.getItemMeta()).getCustomModelData();
                    ItemMeta meta = entryStack.getItemMeta();
                    assert meta != null;
                    meta.setCustomModelData(modelData);
                    entryStack.setItemMeta(meta);
                    entry.setItemStack(entryStack);
                    WarehouseCache.registerEdit(warehouseData.getType(), entry);
                    menu.setItem(8, Icons.createMail(entry.getMail()));
                }
            } else {
                Pack pack = Pack.getPack(clickedStack);
                if (pack != null) {
                    MenuUtil.playClickSound(player);
                    ItemMeta entryMeta = entryStack.getItemMeta();
                    Pack old = Pack.getPack(entryMeta);
                    assert old != null;
                    if ((ChatColor.WHITE + old.getDisplayName()).equals(entryMeta.getDisplayName())) {
                        entryMeta.setDisplayName(ChatColor.WHITE + pack.getDisplayName());
                        entryStack.setItemMeta(entryMeta);
                    }
                    String data = pack.getData();
                    UUID profileID = pack.getProfileID();
                    if (pack instanceof Crate) {
                        Crate crate = (Crate) pack;
                        if (crate.isDoStraps()) {
                            data = crate.getDataSeal();
                            profileID = crate.getProfileIDSeal();
                        }
                    }
                    AssignTexture.assignTexture(entryStack, data, pack.getDisplayName(), profileID);
                    entry.setItemStack(entryStack);
                    WarehouseCache.registerEdit(warehouseData.getType(), entry);
                    menu.setItem(8, Icons.createMail(entry.getMail()));
                }
            }
        }
    }
}