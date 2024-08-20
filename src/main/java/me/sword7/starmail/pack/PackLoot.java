package me.sword7.starmail.pack;

import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.ILootType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PackLoot implements ILootType {

    @Override
    public ItemStack getLootFrom(String s) {
        Pack pack = Pack.getPack(s);
        return pack != null ? pack.getEmptyPack() : null;
    }

    @Override
    public String getListLabel() {
        return Language.LABEL_PACK_TYPES.toString();
    }

    @Override
    public List<String> getLootTypes() {
        List<String> strings = new ArrayList<>();
        for (Pack pack : Pack.getAllPacks()) {
            strings.add(pack.getName());
        }
        return strings;
    }

    @Override
    public String getRoot() {
        return "pack";
    }


    @Override
    public String getType() {
        return Language.LABEL_PACKAGE.toString();
    }

}
