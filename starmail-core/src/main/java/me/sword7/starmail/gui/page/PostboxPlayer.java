package me.sword7.starmail.gui.page;

import com.google.common.collect.ImmutableMap;
import me.sword7.starmail.gui.MenuUtil;
import me.sword7.starmail.gui.data.PostData;
import me.sword7.starmail.gui.data.SessionData;
import me.sword7.starmail.util.Key;
import me.sword7.starmail.util.X.XGlass;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PostboxPlayer extends PostboxMenu {

    @Override
    public void populatePostbox(Inventory menu, SessionData sessionData) {
        PostData postData = (PostData) sessionData;
        ItemStack keyboardBackground = XGlass.BLACK.getDot();
        if (postData.getState() == PostData.keyState.LETTERS) {
            for (int i = 9; i < 45; i++) {
                ItemStack itemStack = slotToKeyLetters.containsKey(i) ? slotToKeyLetters.get(i).getItemStack() : keyboardBackground;
                menu.setItem(i, itemStack);
            }
        } else {
            for (int i = 9; i < 45; i++) {
                ItemStack itemStack = slotToKeyNumbers.containsKey(i) ? slotToKeyNumbers.get(i).getItemStack() : keyboardBackground;
                menu.setItem(i, itemStack);
            }
        }
    }


    @Override
    public void processPostboxClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType) {
        PostData postData = (PostData) sessionData;
        if (postData.getState() == PostData.keyState.LETTERS) {
            if (slotToKeyLetters.containsKey(slot)) {
                MenuUtil.playClickSound(player);
                postData.doKey(slotToKeyLetters.get(slot));
                sessionData.updateTitle();
            }
        } else {
            if (slotToKeyNumbers.containsKey(slot)) {
                MenuUtil.playClickSound(player);
                postData.doKey(slotToKeyNumbers.get(slot));
                sessionData.updateTitle();
            }
        }
    }


    private final Map<Integer, Key> slotToKeyLetters = new ImmutableMap.Builder<Integer, Key>()
            .put(10, Key.Q)
            .put(11, Key.E)
            .put(12, Key.R)
            .put(13, Key.Y)
            .put(14, Key.I)
            .put(15, Key.O)
            .put(16, Key.BACK)

            .put(18, Key.A)
            .put(19, Key.W)
            .put(20, Key.S)
            .put(21, Key.T)
            .put(22, Key.F)
            .put(23, Key.G)
            .put(24, Key.U)
            .put(25, Key.J)
            .put(26, Key.P)

            .put(28, Key.Z)
            .put(29, Key.D)
            .put(30, Key.X)
            .put(31, Key.H)
            .put(32, Key.K)
            .put(33, Key.L)
            .put(34, Key.N)

            .put(38, Key.NUMBERS)
            .put(39, Key.C)
            .put(40, Key.V)
            .put(41, Key.B)
            .put(42, Key.M)

            .build();


    private final Map<Integer, Key> slotToKeyNumbers = new ImmutableMap.Builder<Integer, Key>()
            .put(19, Key.NUM_1)
            .put(20, Key.NUM_2)
            .put(21, Key.NUM_3)
            .put(22, Key.NUM_4)
            .put(23, Key.NUM_5)
            .put(24, Key.NUM_6)
            .put(25, Key.BACK)

            .put(28, Key.LETTERS)
            .put(29, Key.NUM_7)
            .put(30, Key.NUM_8)
            .put(31, Key.NUM_9)
            .put(32, Key.NUM_0)
            .put(33, Key.UNDERSCORE)

            .build();

}
