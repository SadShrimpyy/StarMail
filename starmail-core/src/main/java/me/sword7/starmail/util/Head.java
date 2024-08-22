package me.sword7.starmail.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.sword7.starmail.sys.Version;
import me.sword7.starmail.user.User;
import me.sword7.starmail.util.X.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Head {

    private static int currentValue = Version.current.value;

    public static ItemStack createHeadItem(String data, UUID profileID, String name) {
        ItemStack head = Head.getHead(data, "Head", profileID);
        ItemMeta meta = head.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + name);
        head.setItemMeta(meta);
        return head;
    }

    private static Map<UUID, ItemStack> playerToHead = new HashMap<>();

    public static ItemStack getPlayerHead(User user) {
        UUID userID = user.getID();
        if (playerToHead.containsKey(userID)) {
            return playerToHead.get(userID).clone();
        } else {
            ItemStack head = Head.getHead(userID, user.getName());
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + user.getName());
            head.setItemMeta(meta);
            playerToHead.put(userID, head);
            return head.clone();
        }
    }

    public static ItemStack getHead(String url, String profileName, UUID profileID) {
        ItemStack head = currentValue >= 113 ? new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial()) : new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short) 3);
        if (!url.isEmpty()) {
            assignTexture(head, url, profileName, profileID);
        }
        return head;
    }

    public static void assignTexture(ItemStack head, String url, String profileName, UUID profileID) {
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(profileID, profileName);
        profile.getProperties().put("textures", new Property("textures", url));
        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        head.setItemMeta(skullMeta);
    }

    public static ItemStack getSteeveHead(String name) {
        ItemStack head = currentValue >= 113 ? new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial()) : new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short) 3);
        ItemMeta meta = head.getItemMeta();
        meta.setDisplayName(name);
        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack getHead(UUID playerID, String playerName) {
        ItemStack head = currentValue >= 113 ? new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial()) : new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(playerID, playerName);
        if (currentValue >= 117) {
            skullMeta.setOwningPlayer(Bukkit.getServer().getOfflinePlayer(profile.getId()));
        } else {
            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            } catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        head.setItemMeta(skullMeta);
        return head;
    }

    public static UUID getPlayerID(SkullMeta skullMeta) {
        try {
            if (currentValue >= 113) {
                return skullMeta.getOwningPlayer().getUniqueId();
            } else {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                GameProfile profile = (GameProfile) profileField.get(skullMeta);
                return profile.getId();
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static UUID getPlayerID(Skull skull) {
        try {
            if (currentValue >= 113) {
                return skull.getOwningPlayer().getUniqueId();
            } else {
                Field profileField = skull.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                GameProfile profile = (GameProfile) profileField.get(skull);
                return profile.getId();
            }
        } catch (Exception e) {
            return null;
        }
    }
}
