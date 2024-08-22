package me.sword7.starmail.box;

import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.ILootType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BoxLoot implements ILootType {

    @Override
    public ItemStack getLootFrom(String s) {
        Box box = Box.getBox(s);
        return box != null ? box.getItemStack() : null;
    }

    @Override
    public String getListLabel() {
        return Language.LABEL_BOX_TYPES.toString();
    }

    @Override
    public List<String> getLootTypes() {
        List<String> strings = new ArrayList<>();
        for (Box box : Box.getAllBoxes()) {
            strings.add(box.getName());
        }
        return strings;
    }

    @Override
    public String getRoot() {
        return "box";
    }


    @Override
    public String getType() {
        return Language.LABEL_MAILBOX.toString();
    }

}
