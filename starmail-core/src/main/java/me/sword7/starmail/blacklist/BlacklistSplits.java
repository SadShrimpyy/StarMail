package me.sword7.starmail.blacklist;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class BlacklistSplits {

    private final String NAME = "name";
    private final String TYPE = "type";
    private final String DESCRIPTION = "description";

    private final ArrayList<String> requestedSplits = new ArrayList<>(3);

    public void requestNewSplits(String arg) {
        for (String split : arg.split("\\|")) {
            switch (split.toLowerCase()) {
                case "name":
                    requestedSplits.add(NAME);
                    break;
                case "type":
                    requestedSplits.add(TYPE);
                    break;
                case "description":
                    requestedSplits.add(DESCRIPTION);
                    break;
            }
        }
    }

    @Deprecated
    public String getFromAnalysedLine(boolean isHash, @NotNull ItemStack clone) {
        StringBuilder sb = new StringBuilder();
        for (String split : requestedSplits) {
            ItemMeta meta = clone.getItemMeta();
            MaterialData data = clone.getData();
            if (meta == null || data == null || meta.getLore() == null || meta.getLore().isEmpty()) {
                return null;
            }
            switch (split) {
                case NAME:
                    String name = ChatColor.stripColor(meta.getDisplayName());
                    if (isHash) {
                        sb.append(1 + ":\"");
                        sb.append(encrypt(name));
                    } else {
                        sb.append("name:\"");
                        sb.append(name);
                    }
                    sb.append("\";");
                    break;
                case TYPE:
                    String type = ChatColor.stripColor(String.valueOf(data.getItemType()));
                    if (isHash) {
                        sb.append(2 + ":\"");
                        sb.append(encrypt(type));
                    } else {
                        sb.append("type:\"");
                        sb.append(type);
                    }
                    sb.append("\";");
                    break;
                case DESCRIPTION:
                    String lore = ChatColor.stripColor(String.valueOf(meta.getLore()));
                    if (isHash) {
                        sb.append(3 + ":\"");
                        sb.append(encrypt(lore));
                    } else {
                        sb.append("description:\"");
                        sb.append(lore);
                    }
                    sb.append("\";");
                    break;
            }
        }
        return sb.toString();
    }

    public boolean isEmpty() {
        return requestedSplits.isEmpty();
    }

    private String encrypt(String digest) {
        try {
            byte[] messageDigest = MessageDigest.getInstance("SHA-224").digest(digest.getBytes());
            StringBuilder hexDigest = new StringBuilder(new BigInteger(1, messageDigest).toString(16));
            while (hexDigest.length() < 32) {
                hexDigest.insert(0, "0");
            }
            return hexDigest.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}