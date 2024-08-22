package me.sword7.starmail.gui.page;

import me.sword7.starmail.gui.Icons;
import me.sword7.starmail.pack.Crate;
import me.sword7.starmail.pack.Pack;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class CrateSeal extends Seal {

    @Override
    public Inventory populate(Inventory menu, Pack pack) {
        Crate crate = (Crate) pack;
        ItemStack borderDot = crate.getBorderWood().getDot();
        ItemStack dot = crate.getWood().getDot();
        ItemStack seal = Icons.createSeal(new ItemStack(Material.STRING));

        for (int i = 0; i < 9; i++) {
            menu.setItem(i, borderDot);
            menu.setItem(i + 36, borderDot);
        }

        for (int i = 0; i < 5; i++) {
            menu.setItem(i * 9, borderDot);
            menu.setItem(i * 9 + 8, borderDot);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                menu.setItem((10 + 9 * i) + j, dot);
            }
        }

        for (int i : sealSlots) {
            menu.setItem(i, seal);
        }


        return menu;
    }

    private Set<Integer> sealSlots = new ImmutableSet.Builder<Integer>()
            .add(4)
            .add(13)
            .add(22)
            .add(31)
            .add(40)
            .add(18)
            .add(19)
            .add(20)
            .add(21)
            .add(23)
            .add(24)
            .add(25)
            .add(26)
            .build();

    @Override
    public Set<Integer> getSealedSlots() {
        return sealSlots;
    }

}
