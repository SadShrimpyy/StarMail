package me.sword7.starmail.postbox;

import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.ILootType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PostboxLoot implements ILootType {

    @Override
    public ItemStack getLootFrom(String s) {
        Postbox postbox = Postbox.getPostbox(s);
        return postbox != null ? postbox.getItemStack() : null;
    }

    @Override
    public String getListLabel() {
        return Language.LABEL_POSTBOX_TYPES.toString();
    }

    @Override
    public List<String> getLootTypes() {
        List<String> strings = new ArrayList<>();
        for (Postbox postbox : Postbox.getAllPostboxes()) {
            strings.add(postbox.getName());
        }
        return strings;
    }

    @Override
    public String getRoot() {
        return "postbox";
    }

    @Override
    public String getType() {
        return Language.LABEL_POSTBOX.toString();
    }

}
