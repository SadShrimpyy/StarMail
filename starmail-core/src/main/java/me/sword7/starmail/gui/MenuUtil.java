package me.sword7.starmail.gui;

import me.sword7.starmail.gui.page.Page;
import me.sword7.starmail.util.X.XGlass;
import me.sword7.starmail.util.X.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuUtil {

    public static Inventory createMenu(String title, Page page, XGlass theme) {
        int rows = page.getRows();
        Inventory menu = createInventory(rows, title);

        //add border
        ItemStack themeItem = theme.getSwiggle();

        for (int i = 0; i < 9; i++) {
            menu.setItem(i, themeItem);
        }

        int offset = (rows - 1) * 9;
        for (int i = 0; i < 9; i++) {
            menu.setItem(i + offset, themeItem);
        }

        for (int i = 1; i < rows - 1; i++) {
            menu.setItem(9 * i, themeItem);
            menu.setItem(9 * i + 8, themeItem);
        }

        if (page.hasPrevious()) {
            menu.setItem(0, Icons.BACK_BUTTON);
        }

        return menu;
    }

    private static Inventory createInventory(int rows, String title) {
        return Bukkit.createInventory(null, rows * 9, title);
    }

    private static Sound clickSound = XSound.BLOCK_STONE_BUTTON_CLICK_ON.isSupported() ? XSound.BLOCK_STONE_BUTTON_CLICK_ON.parseSound() : XSound.UI_BUTTON_CLICK.parseSound();
    private static Sound pickUpSound = XSound.ENTITY_ITEM_PICKUP.parseSound();
    private static Sound errorSound = XSound.ENTITY_BLAZE_HURT.parseSound();

    public static void playClickSound(Player player) {
        player.playSound(player.getLocation(), clickSound, 0.5f, 1.2f);
    }

    public static void playPickupSound(Player player) {
        player.playSound(player.getLocation(), pickUpSound, 0.5f, 1.2f);
    }

    public static void playErrorSound(Player player) {
        player.playSound(player.getLocation(), errorSound, 0.5f, 0.8f);
    }

}
