package me.sword7.starmail.gui.page;

import me.sword7.starmail.gui.Icons;
import me.sword7.starmail.pack.Gift;
import me.sword7.starmail.pack.Pack;
import com.google.common.collect.ImmutableSet;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class GiftSeal extends Seal {

    @Override
    public Inventory populate(Inventory menu, Pack pack) {
        Gift gift = (Gift) pack;
        ItemStack paper = gift.getPaper().getDot();
        ItemStack ribbon = gift.getRibbon().getDot();
        ItemStack seal = Icons.createSeal(ribbon);

        for (int i = 0; i < 45; i++) {
            menu.setItem(i, paper);
        }

        for (int i : sealSlots) {
            menu.setItem(i, seal);
        }

        return menu;

    }

    private Set<Integer> sealSlots = new ImmutableSet.Builder<Integer>()
            .add(4)
            .add(5)
            .add(13)
            .add(15)
            .add(22)
            .add(29)
            .add(31)
            .add(39)
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
