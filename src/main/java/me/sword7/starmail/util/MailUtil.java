package me.sword7.starmail.util;

import me.sword7.starmail.util.X.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MailUtil {

    public static void giveItems(Player player, ItemStack[] items) {
        PlayerInventory inventory = player.getInventory();
        for (ItemStack itemStack : items) {
            if (itemStack != null) {
                if (!player.isDead() && player.isOnline()) {
                    Map<Integer, ItemStack> overFlow = inventory.addItem(itemStack);
                    if (overFlow.size() > 0) {
                        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), overFlow.get(0));
                    }
                } else {
                    player.getLocation().getWorld().dropItemNaturally(player.getLocation(), itemStack);
                }
            }
        }
    }

    public static int parseAmount(String string) {
        try {
            int amt = Integer.parseInt(string);
            if (amt < 0) {
                amt = 0;
            }
            return amt;
        } catch (Exception e) {
            return 1;
        }
    }

    public static String getFirstLineLore(ItemMeta meta) {
        if (meta != null) {
            List<String> lore = meta.getLore();
            if (lore != null && lore.size() > 0) {
                return lore.get(0);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getFormatted(String s) {
        String[] parts = s.split("_");
        if (parts.length > 0) {
            String word = parts[0];
            String formatted = (word.charAt(0) + word.substring(1).toLowerCase());
            for (int i = 1; i < parts.length; i++) {
                word = parts[i];
                formatted += (" " + word.charAt(0) + word.substring(1).toLowerCase());
            }
            return formatted;
        } else {
            return s;
        }
    }

    public static Material materialFrom(String s) {
        try {
            return Material.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    private static Set<Material> interactableMaterial = buildInteractableMaterials();

    private static Set<Material> buildInteractableMaterials() {
        Set<XMaterial> xMaterials = new HashSet<>();

        xMaterials.add(XMaterial.DISPENSER);
        xMaterials.add(XMaterial.NOTE_BLOCK);
        xMaterials.add(XMaterial.CHEST);
        xMaterials.add(XMaterial.CRAFTING_TABLE);
        xMaterials.add(XMaterial.FURNACE);
        xMaterials.add(XMaterial.OAK_TRAPDOOR);
        xMaterials.add(XMaterial.SPRUCE_TRAPDOOR);
        xMaterials.add(XMaterial.BIRCH_TRAPDOOR);
        xMaterials.add(XMaterial.JUNGLE_TRAPDOOR);
        xMaterials.add(XMaterial.ACACIA_TRAPDOOR);
        xMaterials.add(XMaterial.DARK_OAK_TRAPDOOR);
        xMaterials.add(XMaterial.WARPED_TRAPDOOR);
        xMaterials.add(XMaterial.CRIMSON_TRAPDOOR);
        xMaterials.add(XMaterial.OAK_DOOR);
        xMaterials.add(XMaterial.SPRUCE_DOOR);
        xMaterials.add(XMaterial.BIRCH_DOOR);
        xMaterials.add(XMaterial.JUNGLE_DOOR);
        xMaterials.add(XMaterial.ACACIA_DOOR);
        xMaterials.add(XMaterial.DARK_OAK_DOOR);
        xMaterials.add(XMaterial.WARPED_DOOR);
        xMaterials.add(XMaterial.CRIMSON_DOOR);
        xMaterials.add(XMaterial.OAK_FENCE_GATE);
        xMaterials.add(XMaterial.SPRUCE_FENCE_GATE);
        xMaterials.add(XMaterial.BIRCH_FENCE_GATE);
        xMaterials.add(XMaterial.JUNGLE_FENCE_GATE);
        xMaterials.add(XMaterial.ACACIA_FENCE_GATE);
        xMaterials.add(XMaterial.DARK_OAK_FENCE_GATE);
        xMaterials.add(XMaterial.WARPED_FENCE_GATE);
        xMaterials.add(XMaterial.CRIMSON_FENCE_GATE);
        xMaterials.add(XMaterial.ENCHANTING_TABLE);
        xMaterials.add(XMaterial.ENDER_CHEST);
        xMaterials.add(XMaterial.ANVIL);
        xMaterials.add(XMaterial.CHIPPED_ANVIL);
        xMaterials.add(XMaterial.DAMAGED_ANVIL);
        xMaterials.add(XMaterial.TRAPPED_CHEST);
        xMaterials.add(XMaterial.DAYLIGHT_DETECTOR);
        xMaterials.add(XMaterial.HOPPER);
        xMaterials.add(XMaterial.DROPPER);
        xMaterials.add(XMaterial.SHULKER_BOX);
        xMaterials.add(XMaterial.WHITE_SHULKER_BOX);
        xMaterials.add(XMaterial.ORANGE_SHULKER_BOX);
        xMaterials.add(XMaterial.MAGENTA_SHULKER_BOX);
        xMaterials.add(XMaterial.LIGHT_BLUE_SHULKER_BOX);
        xMaterials.add(XMaterial.YELLOW_SHULKER_BOX);
        xMaterials.add(XMaterial.LIME_SHULKER_BOX);
        xMaterials.add(XMaterial.PINK_SHULKER_BOX);
        xMaterials.add(XMaterial.GRAY_SHULKER_BOX);
        xMaterials.add(XMaterial.LIGHT_GRAY_SHULKER_BOX);
        xMaterials.add(XMaterial.CYAN_SHULKER_BOX);
        xMaterials.add(XMaterial.PURPLE_SHULKER_BOX);
        xMaterials.add(XMaterial.BLUE_SHULKER_BOX);
        xMaterials.add(XMaterial.BROWN_SHULKER_BOX);
        xMaterials.add(XMaterial.GREEN_SHULKER_BOX);
        xMaterials.add(XMaterial.RED_SHULKER_BOX);
        xMaterials.add(XMaterial.BLACK_SHULKER_BOX);
        xMaterials.add(XMaterial.REPEATER);
        xMaterials.add(XMaterial.COMPARATOR);
        xMaterials.add(XMaterial.WHITE_BED);
        xMaterials.add(XMaterial.ORANGE_BED);
        xMaterials.add(XMaterial.MAGENTA_BED);
        xMaterials.add(XMaterial.LIGHT_BLUE_BED);
        xMaterials.add(XMaterial.YELLOW_BED);
        xMaterials.add(XMaterial.LIME_BED);
        xMaterials.add(XMaterial.PINK_BED);
        xMaterials.add(XMaterial.GRAY_BED);
        xMaterials.add(XMaterial.LIGHT_GRAY_BED);
        xMaterials.add(XMaterial.CYAN_BED);
        xMaterials.add(XMaterial.PURPLE_BED);
        xMaterials.add(XMaterial.BLUE_BED);
        xMaterials.add(XMaterial.BROWN_BED);
        xMaterials.add(XMaterial.GREEN_BED);
        xMaterials.add(XMaterial.RED_BED);
        xMaterials.add(XMaterial.BLACK_BED);
        xMaterials.add(XMaterial.BREWING_STAND);
        xMaterials.add(XMaterial.LOOM);
        xMaterials.add(XMaterial.COMPOSTER);
        xMaterials.add(XMaterial.BARREL);
        xMaterials.add(XMaterial.SMOKER);
        xMaterials.add(XMaterial.BLAST_FURNACE);
        xMaterials.add(XMaterial.CARTOGRAPHY_TABLE);
        xMaterials.add(XMaterial.FLETCHING_TABLE);
        xMaterials.add(XMaterial.GRINDSTONE);
        xMaterials.add(XMaterial.LECTERN);
        xMaterials.add(XMaterial.SMITHING_TABLE);
        xMaterials.add(XMaterial.STONECUTTER);
        xMaterials.add(XMaterial.BELL);
        xMaterials.add(XMaterial.COMMAND_BLOCK);
        xMaterials.add(XMaterial.BEACON);
        xMaterials.add(XMaterial.REPEATING_COMMAND_BLOCK);

        Set<Material> interactableMaterials = new HashSet<>();

        for (XMaterial xMaterial : xMaterials) {
            if (xMaterial.isSupported()) interactableMaterials.add(xMaterial.parseMaterial());
        }

        return interactableMaterials;
    }

    public static boolean isInteractable(Material material) {
        return interactableMaterial.contains(material);
    }


}
