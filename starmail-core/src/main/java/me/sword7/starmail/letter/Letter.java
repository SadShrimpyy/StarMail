package me.sword7.starmail.letter;

import me.sword7.starmail.sys.Language;
import me.sword7.starmail.sys.Version;
import me.sword7.starmail.sys.config.ItemsConfig;
import me.sword7.starmail.util.MailColor;
import me.sword7.starmail.util.X.XDye;
import me.sword7.starmail.util.X.XMaterial;
import com.google.common.collect.ImmutableSet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Letter {

    public static Material WRITTEN_BOOK = XMaterial.WRITTEN_BOOK.parseMaterial();
    public static Material WRITABLE_BOOK = XMaterial.WRITABLE_BOOK.parseMaterial();

    public static ImmutableSet<Material> bookMats = new ImmutableSet.Builder<Material>().add(WRITTEN_BOOK).add(WRITABLE_BOOK).build();
    private static Map<String, Letter> nameToLetter = new HashMap<>();
    private static List<Letter> orderedLetters = new ArrayList<>();

    public static void init() {
        List<Letter> letters = new ArrayList<>();
        for (LetterType letterType : LetterType.values()) {
            if (letterType != LetterType.CUSTOM) {
                letters.add(letterType.getLetter());
            }
        }
        letters.addAll(ItemsConfig.getCustomLetters());
        for (Letter letter : letters) {
            nameToLetter.put(letter.getName(), letter);
            orderedLetters.add(letter);
        }
        letters.clear();
    }

    private LetterType type;
    private String name;
    private int modelData;
    private XDye xDye;
    private ItemStack quillStack;

    public Letter(LetterType type, MailColor mailColor, int modelData) {
        this.type = type;
        this.name = type.toString();
        this.modelData = modelData;
        this.xDye = mailColor.getXDye();
        this.quillStack = createQuillStack(modelData, Language.LABEL_DYED_LETTER_AND_QUILL.fromColor(mailColor.getLanguage()));
    }

    public Letter(LetterType type, String name, String letterAndQuillName, int modelData) {
        this.type = type;
        this.name = name;
        this.modelData = modelData;
        this.quillStack = createQuillStack(modelData, letterAndQuillName);
    }

    private static ItemStack createQuillStack(int model, String name) {
        ItemStack letter = new ItemStack(WRITABLE_BOOK);
        BookMeta meta = (BookMeta) letter.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + name);
        if (Version.current.isModelDataSupported()) meta.setCustomModelData(model);
        meta.setPages(Arrays.asList(""));
        letter.setItemMeta(meta);
        return letter;
    }

    public LetterType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getModelData() {
        return modelData;
    }

    public XDye getXDye() {
        return xDye;
    }

    public ItemStack getLetter(BookMeta bookMeta) {
        ItemStack signedLetter = new ItemStack(WRITTEN_BOOK);
        ItemMeta meta = bookMeta.clone();
        if (Version.current.isModelDataSupported()) meta.setCustomModelData(modelData + 1);
        signedLetter.setItemMeta(meta);
        return signedLetter;
    }


    public ItemStack getLetterAndQuill() {
        return quillStack.clone();
    }

    public static BookMeta sign(BookMeta meta) {
        try {
            meta.setDisplayName(ChatColor.WHITE + meta.getTitle());
            meta.setCustomModelData(meta.getCustomModelData() + 1);
            return meta;
        } catch (Exception e) {
            return meta;
        }
    }

    public static boolean isLetter(ItemStack itemStack) {
        if (!Version.current.hasLetter()) return false;
        if (itemStack != null) {
            return isLetter(itemStack.getItemMeta());
        }
        return false;
    }

    public static boolean isLetter(ItemMeta meta) {
        if (!Version.current.hasLetter()) return false;
        if (meta instanceof BookMeta && meta.hasCustomModelData()) {
            int model = meta.getCustomModelData();
            return model >= 220 && model < 400;
        }
        return false;
    }

    public static Letter getLetter(String string) {
        return nameToLetter.get(string);
    }

    public static Collection<Letter> getAllLetters() {
        return orderedLetters;
    }

}
