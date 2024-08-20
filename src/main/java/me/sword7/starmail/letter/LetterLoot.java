package me.sword7.starmail.letter;

import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.ILootType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LetterLoot implements ILootType {

    @Override
    public ItemStack getLootFrom(String s) {
        Letter letter = Letter.getLetter(s);
        return letter != null ? letter.getLetterAndQuill() : null;
    }

    @Override
    public String getListLabel() {
        return Language.LABEL_LETTER_TYPES.toString();
    }

    @Override
    public List<String> getLootTypes() {
        List<String> strings = new ArrayList<>();
        for (Letter letter : Letter.getAllLetters()) {
            strings.add(letter.getName());
        }
        return strings;
    }

    @Override
    public String getRoot() {
        return "letter";
    }


    @Override
    public String getType() {
        return Language.LABEL_LETTER.toString();
    }

}
