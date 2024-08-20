package me.sword7.starmail.postbox;

import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.Head;
import me.sword7.starmail.util.X.XGlass;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class Postbox {

    private static Map<UUID, Postbox> iDToPostbox = new HashMap<>();
    private static Map<String, Postbox> nameToPostbox = new HashMap<>();

    public static void init() {
        List<Postbox> postboxes = new ArrayList<>();
        for (PostboxType postboxType : PostboxType.values()) {
            if (postboxType != PostboxType.CUSTOM) {
                postboxes.add(postboxType.getPostbox());
            }
        }
        //If I ever add custom postboxes
        //postboxes.addAll(ItemsConfig.getCustomPostboxes());
        for (Postbox postbox : postboxes) {
            iDToPostbox.put(postbox.getProfileID(), postbox);
            nameToPostbox.put(postbox.getName(), postbox);
        }
        postboxes.clear();
    }

    private PostboxType type;
    private String name;
    private ItemStack itemStack;
    private UUID profileID;
    private XGlass xGlass;

    public Postbox(PostboxType type, String name, XGlass xGlass, UUID profileID, String data) {
        this.type = type;
        this.name = name;
        this.xGlass = xGlass;
        this.profileID = profileID;
        this.itemStack = Head.createHeadItem(data, profileID, Language.LABEL_POSTBOX.toString());
    }

    public PostboxType getType() {
        return type;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    public UUID getProfileID() {
        return profileID;
    }

    public String getName() {
        return name;
    }

    public XGlass getXGlass() {
        return xGlass;
    }

    public static boolean isPostbox(ItemStack itemStack) {
        return getPostbox(itemStack) != null;
    }

    public static Postbox getPostbox(BlockState blockState) {
        if (blockState instanceof Skull) {
            UUID playerID = Head.getPlayerID((Skull) blockState);
            return iDToPostbox.get(playerID);
        }
        return null;
    }

    public static Postbox getPostbox(ItemStack itemStack) {
        return itemStack != null ? getPostbox(itemStack.getItemMeta()) : null;
    }

    public static Postbox getPostbox(ItemMeta meta) {
        if (meta instanceof SkullMeta) {
            return iDToPostbox.get(Head.getPlayerID((SkullMeta) meta));
        }
        return null;
    }

    public static Postbox getPostbox(String string) {
        return nameToPostbox.get(string);
    }

    public static Collection<Postbox> getAllPostboxes() {
        return nameToPostbox.values();
    }

}
