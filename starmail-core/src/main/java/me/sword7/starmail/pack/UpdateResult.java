package me.sword7.starmail.pack;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class UpdateResult {

    public ItemStack[] contents;
    public Pack.ContentStatus status;

    public UpdateResult(ItemStack[] contents, Pack.ContentStatus status) {
        this.contents = contents;
        this.status = status;
    }

    public Pack.ContentStatus getStatus() {
        return status;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public static UpdateResult getResult(Inventory menu) {
        Pack.ContentStatus status = Pack.ContentStatus.EMPTY;

        int mailIndex = 0;
        int size = 0;
        boolean fractal = false;
        ItemStack[] contents = new ItemStack[21];
        for (int i = 10; i < 35; i++) {
            if (!(i % 9 == 0) && !((i + 1) % 9 == 0)) {
                ItemStack menuItem = menu.getItem(i);
                boolean empty = isEmpty(menuItem);
                if (!empty && !fractal) {
                    fractal = Pack.isPack(menuItem);
                    size++;
                }
                contents[mailIndex] = empty ? null : menuItem;
                mailIndex++;
            }
        }

        if (size > 0) {
            status = fractal ? Pack.ContentStatus.FRACTAL : Pack.ContentStatus.VALID;
        }

        return new UpdateResult(contents, status);

    }

    private static boolean isEmpty(ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR;
    }

}
