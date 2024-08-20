package me.sword7.starmail.pack;

import me.sword7.starmail.gui.page.PageType;
import me.sword7.starmail.sys.config.ItemsConfig;
import me.sword7.starmail.util.Head;
import me.sword7.starmail.util.MailUtil;
import me.sword7.starmail.util.X.XGlass;
import me.sword7.starmail.util.X.XSound;
import com.google.common.collect.ImmutableSet;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

import static me.sword7.starmail.sys.Language.*;

public abstract class Pack {

    private static Sound poofSound = XSound.ITEM_FIRECHARGE_USE.isSupported() ? XSound.ITEM_FIRECHARGE_USE.parseSound() : XSound.ENTITY_GHAST_SHOOT.parseSound();

    private static Map<UUID, Pack> iDToPack = new HashMap<>();
    private static Map<String, Pack> nameToPack = new HashMap<>();
    private static List<Pack> orderedPacks = new ArrayList<>();

    private static String EMPTY_LORE;
    private static String SEALED_LORE;

    private static Set<String> packLores;

    public static void init() {
        List<Pack> packs = new ArrayList<>();
        for (PackType packType : PackType.values()) {
            if (packType != PackType.CUSTOM && packType.isSupported()) {
                packs.add(packType.getPack());
            }
        }
        packs.addAll(ItemsConfig.getCustomPacks());
        for (Pack pack : packs) {
            if (pack instanceof Crate) {
                Crate crate = (Crate) pack;
                if (crate.isDoStraps()) iDToPack.put(crate.getProfileIDSeal(), pack);
            }
            iDToPack.put(pack.getProfileID(), pack);
            nameToPack.put(pack.getName(), pack);
            orderedPacks.add(pack);
        }
        packs.clear();

        EMPTY_LORE = ChatColor.GRAY.toString() + ChatColor.ITALIC + LORE_ID_EMPTY_PACKAGE.toString();
        SEALED_LORE = ChatColor.GRAY.toString() + ChatColor.ITALIC + LORE_ID_SEALED_PACKAGE.toString();

        packLores = new ImmutableSet.Builder<String>()
                .add(EMPTY_LORE)
                .add(SEALED_LORE)
                .build();
    }

    private PackType type;
    private String name;
    protected String displayName;
    private ItemStack baseItemStack;
    protected UUID profileID;
    protected String data;

    public Pack(PackType type, String name, String displayName, String profileID, String data) {
        this.type = type;
        this.name = name;
        this.displayName = displayName;
        this.profileID = UUID.fromString(profileID);
        this.baseItemStack = Head.createHeadItem(data, this.profileID, displayName);
        this.data = data;
    }

    public PackType getType() {
        return type;
    }

    public UUID getProfileID() {
        return profileID;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getBaseItemStack() {
        return baseItemStack;
    }

    public String getData() {
        return data;
    }

    public abstract XGlass getBorder();

    public ItemStack getEmptyPack() {
        ItemStack itemStack = baseItemStack.clone();
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Collections.singletonList(ChatColor.GRAY.toString() + ChatColor.ITALIC + LORE_ID_EMPTY_PACKAGE.toString()));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemMeta seal(ItemMeta meta, UUID trackingNo, ItemStack[] itemStacks) {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + LORE_ID_SEALED_PACKAGE.toString());
        lore.add(ChatColor.GRAY + LABEL_TRACKING_NO.toString() + ": ");
        lore.add(ChatColor.GOLD + trackingNo.toString().substring(0, 18));
        lore.add(ChatColor.GOLD + trackingNo.toString().substring(18, 36));
        lore.add(ChatColor.GRAY + LABEL_CONTENTS.toString() + ": ");
        int count = 0;
        int overflow = 0;
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null) {
                if (count < 2) {
                    count++;
                    String itemName = itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : itemStack.getType().toString();
                    String stackString = ChatColor.WHITE + MailUtil.getFormatted(itemName);
                    stackString += " x" + itemStack.getAmount();
                    lore.add(stackString);
                } else {
                    overflow++;
                }
            }
        }
        if (overflow > 0) {
            lore.add(ChatColor.WHITE + ChatColor.ITALIC.toString() + MISC_OVERFLOW.fromAmount(overflow));
        }
        meta.setLore(lore);
        return meta;
    }

    public static boolean isPack(ItemStack itemStack) {
        return (itemStack != null) ? isPack(itemStack.getItemMeta()) : false;
    }

    public static boolean isPack(ItemMeta meta) {
        return packLores.contains(MailUtil.getFirstLineLore(meta));
    }

    public static boolean isSealedPack(ItemStack itemStack) {
        return (itemStack != null) ? isSealedPack(itemStack.getItemMeta()) : false;
    }

    public static boolean isSealedPack(ItemMeta meta) {
        return MailUtil.getFirstLineLore(meta).equals(SEALED_LORE);
    }

    public static boolean isEmptyPack(ItemStack itemStack) {
        return (itemStack != null) ? isEmptyPack(itemStack.getItemMeta()) : false;
    }

    public static boolean isEmptyPack(ItemMeta meta) {
        return MailUtil.getFirstLineLore(meta).equals(EMPTY_LORE);
    }

    public static UUID getTrackingNo(ItemStack itemStack) {
        return itemStack != null ? getTrackingNo(itemStack.getItemMeta()) : null;
    }

    public static UUID getTrackingNo(ItemMeta meta) {
        try {
            return UUID.fromString(meta.getLore().get(2).split("§6")[1] + meta.getLore().get(3).split("§6")[1]);
        } catch (Exception e) {
            return null;
        }
    }

    public static Pack getPack(BlockState blockState) {
        if (blockState instanceof Skull) {
            UUID playerID = Head.getPlayerID((Skull) blockState);
            return iDToPack.get(playerID);
        }
        return null;
    }

    public static Pack getPack(ItemStack itemStack) {
        return itemStack != null ? getPack(itemStack.getItemMeta()) : null;
    }

    public static Pack getPack(ItemMeta meta) {
        if (meta instanceof SkullMeta) {
            return iDToPack.get(Head.getPlayerID((SkullMeta) meta));
        }
        return null;
    }

    public static Pack getPack(String name) {
        return nameToPack.get(name);
    }

    public static Collection<Pack> getAllPacks() {
        return orderedPacks;
    }

    public static Sound getPoofSound() {
        return poofSound;
    }

    public abstract void playOpenSound(Player player);

    public abstract void playCloseSound(Player player);

    public abstract PageType getOpener();

    public abstract boolean isSupported();

    public abstract ItemStack getSealBaseStack();

    public abstract void playSealSound(Player player);

    public abstract void playUnsealSound(Player player);

    public enum ContentStatus {
        EMPTY, FRACTAL, VALID;
    }

}
