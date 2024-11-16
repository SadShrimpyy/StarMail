package me.sword7.starmail.blacklist;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BlacklistSplits {

    public String getBlacklistedItemData(@NotNull ItemStack clone, String arg1) {
        StringBuilder sb = new StringBuilder();
        for (String split : arg1.split("\\|")) {
            ItemMeta meta = clone.getItemMeta();
            switch (split) {
                case "name":
                    if (meta == null || meta.getDisplayName().isEmpty()) continue;
                    sb.append("name:\"");
                    sb.append(ChatColor.stripColor(meta.getDisplayName()));
                    sb.append("\";");
                    break;
                case "type":
                    sb.append("type:\"");
                    sb.append(clone.getType().name());
                    sb.append("\";");
                    break;
                case "description":
                    if (meta == null || meta.getLore() == null || meta.getLore().isEmpty()) continue;
                    sb.append("description:\"");
                    sb.append(joinDescription(clone));
                    break;
            }
        }
        return sb.toString();
    }

    public boolean hasValidAttributes(ItemStack clone, String arg1) {
        for (String split : arg1.split("\\|")) {
            ItemMeta meta = clone.getItemMeta();
            switch (split) {
                case "type":
                    break;
                case "name":
                    if (meta == null || meta.getDisplayName().isEmpty()) {
                        return false;
                    }
                    break;
                case "description":
                    if (meta == null || meta.getLore() == null || meta.getLore().isEmpty()) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    public @NotNull String getRequestedAttribute(ItemStack clone, String arg1) {
        assert clone.getData() != null;
        assert clone.getItemMeta() != null;
        assert clone.getItemMeta().getLore() != null;
        for (String split : arg1.split("\\|")) {
            switch (split.toLowerCase()) {
                case "name":
                    return clone.getItemMeta().getDisplayName();
                case "type":
                    return String.valueOf(clone.getData().getItemType());
                case "description":
                    return joinDescription(clone);
                default:
                    throw new RuntimeException("Requested Attribute INVALID: Please contact the developer.");
            }
        }
        throw new RuntimeException("Requested Attribute INVALID: Please contact the developer.");
    }

    public boolean isEmpty(String arg1) {
        ArrayList<String> requestedSplits = new ArrayList<>(3);
        for (String split : arg1.split("\\|")) {
            switch (split.toLowerCase()) {
                case "name":
                    requestedSplits.add("name");
                    break;
                case "type":
                    requestedSplits.add("type");
                    break;
                case "description":
                    requestedSplits.add("description");
                    break;
            }
        }
        return requestedSplits.isEmpty();
    }

    private @NotNull String joinDescription(ItemStack clone) {
        assert clone.getItemMeta() != null;
        assert clone.getItemMeta().getLore() != null;
        return String.join("-", clone.getItemMeta().getLore()
                        .stream().map(ChatColor::stripColor)
                        .toArray(String[]::new))
                .replace(":", "")
                .replace(";", "")
                .replace("\"", "");
    }

    public String checkAndJoinDescription(ItemStack clone) {
        if ((clone.getItemMeta() == null) || (clone.getItemMeta().getLore() == null)) return null;
        return String.join("-", clone.getItemMeta().getLore()
                        .stream().map(ChatColor::stripColor)
                        .toArray(String[]::new))
                .replace(":", "")
                .replace(";", "")
                .replace("\"", "");
    }

}